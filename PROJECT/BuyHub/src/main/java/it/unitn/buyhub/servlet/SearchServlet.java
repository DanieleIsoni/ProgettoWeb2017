package it.unitn.buyhub.servlet;

import com.google.gson.Gson;
import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a webservice, implemented with a servlet. It is used by the search
 * page to load the results async.
 *
 * @author Massimo Girondi
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    private ProductDAO productDAO;
    private ReviewDAO reviewDAO;
    private CoordinateDAO coordinateDAO;

    int ProductsPerPage = 10;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            if ((request.getParameter("q") != null && request.getParameter("q").length() > 0)
                    || (request.getParameter("q").equals("") && (!request.getParameter("q").equals("")
                    || !request.getParameter("min").equals("")
                    || !request.getParameter("max").equals("")
                    || !request.getParameter("minRev").equals("")))) {
                String q = request.getParameter("q");

                double min = 0;
                double max = Double.MAX_VALUE;

                int c = -1;
                int minRev = 0;
                //minimo e massimo
                if (request.getParameter("min") != null && Double.parseDouble(request.getParameter("min")) > 0) {
                    min = Double.parseDouble(request.getParameter("min"));
                }
                if (request.getParameter("max") != null && Double.parseDouble(request.getParameter("max")) > min) {
                    max = Double.parseDouble(request.getParameter("max"));
                }

                //categoria
                if (request.getParameter("c") != null && Integer.parseInt(request.getParameter("c")) >= 0) {
                    c = Integer.parseInt(request.getParameter("c"));
                }

                //minimo della media delle recensioni
                if (request.getParameter("minRev") != null && Integer.parseInt(request.getParameter("minRev")) > 0) {
                    minRev = Integer.parseInt(request.getParameter("minRev"));
                }

                double lat = 0;
                double lng = 0;
                int dist = 0;
                //ricerca geografica
                if (request.getParameter("lat") != null && Double.parseDouble(request.getParameter("lat")) > 0
                        && request.getParameter("lng") != null && Double.parseDouble(request.getParameter("lng")) > 0
                        && request.getParameter("dist") != null && Double.parseDouble(request.getParameter("dist")) > 0) {
                    lat = Double.parseDouble(request.getParameter("lat"));
                    lng = Double.parseDouble(request.getParameter("lng"));
                    dist = Integer.parseInt(request.getParameter("dist"));
                }

                List<Product> products;
                try {
                    if (min != 0 || max != Double.MAX_VALUE) {
                        products = productDAO.getByNameAndPriceRange(q, min, max);
                    } else {
                        products = productDAO.getByName(q);
                    }

                    //rimozioni non voluti
                    if (c != -1 || minRev > 0) {
                        products.removeIf(new productRemovePredicate(c, minRev));
                    }

                    if (lat != 0 && lng != 0 && dist != 0) {
                        products.removeIf(new productGeoRemovePredicate(lat, lng, dist));
                    }

                } catch (DAOException ex) {
                    Log.error("Error on retreving products: " + ex.toString());
                    throw new ServletException("Error on retreving products: " + ex.toString());
                }

                int count = products.size();

                int pages = count / ProductsPerPage + 1;

                int p = 1;

                int s = 0;

                //ordinamento, solo se il modo è >0, altrimenti è già ordinato alfabeticamente dal DBMS
                if (request.getParameter("s") != null && Integer.parseInt(request.getParameter("s")) > 0) {
                    s = Integer.parseInt(request.getParameter("s"));
                    productComparator comp = new productComparator(s);
                    products.sort(comp);
                }

                List<Product> p2 = null;
                if (Integer.parseInt(request.getParameter("s")) != 2) {
                    p2 = new ArrayList<Product>(products);
                    p2.sort(new productComparator(2));
                }
                /*
                //prezzo minimo e massimo dei prodotti cercati.
                //commentato poichè lo slider non ci consente di modificare dinamicamente i valori di minimo e massimo
                //Quindi la funzione, per il momento, è inutile
                int minPrice=0;
                int maxPrice=0;
                if(count>0)
                {
                    minPrice=(int) p2.get(0).getPrice();
                    maxPrice=(int) p2.get(p2.size()-1).getPrice();
                }
                 */

                //paginazione
                if (request.getParameter("p") != null && Integer.parseInt(request.getParameter("p")) > 0) {
                    p = Integer.parseInt(request.getParameter("p"));
                }

                int from = Math.max(0, (p - 1) * ProductsPerPage);
                int to = Math.min(products.size(), (p) * ProductsPerPage);

                products = products.subList(from, to);

                Gson gson = new Gson();
                String result = "{\"s\":" + s + ",\n\"p\":" + p + ",\n\"pages\":" + pages + ",\n";//+"\"min\":"+minPrice+",\n\"max\":"+maxPrice+",\n";
                //non voglio le informazioni sugli utenti nel JSON
                for (Product product : products) {
                    product.getShop().setOwner(null);
                }
                result += "\"products\": " + gson.toJson(products);
                result += "}";

                response.setContentType("Application/json;charset=UTF-8");

                out.println(result);

            } else {

                response.setContentType("Application/json;charset=UTF-8");
                out.println("{\"msg\": \"No query specified\"}");
            }

        } catch (Exception ex) {
            Log.error("Error getting product list:" + ex.getClass() + "-" + ex.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");

        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for product storage system");
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            reviewDAO = daoFactory.getDAO(ReviewDAO.class);
            coordinateDAO = daoFactory.getDAO(CoordinateDAO.class);

        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for prduct storage system");
            throw new ServletException("Impossible to get dao factory for prduct storage system", ex);
        }

//        Log.info("SearchServlet init done");
    }

    class productComparator implements Comparator {

        int mode = 0;

        @Override
        public int compare(Object o1, Object o2) {

            int retval = -1;
            Product p1 = (Product) o1;
            Product p2 = (Product) o2;

            switch (mode) {
                /*
                il metodo di ordinamento dei risultati,
                di default alfabetico (0),
                alfagetico inverso (1)
                prezzo crescente (2),
                prezzo decrescente(3),
                valutazione decrescente(4).
                numero recensioni decrescente(5).
                 */
                default:
                case 0:
                    retval = p1.getName().compareTo(p2.getName());
                    break;
                case 1:
                    retval = p2.getName().compareTo(p1.getName());
                    break;
                case 2:
                    retval = ((Double) p1.getPrice()).compareTo(p2.getPrice());
                    break;
                case 3:
                    retval = ((Double) p2.getPrice()).compareTo(p1.getPrice());
                    break;
                case 4:
                    retval = ((Double) p2.getAvgReview()).compareTo(p1.getAvgReview());
                    break;
                case 5:
                    retval = ((Integer) p2.getReviewCount()).compareTo(p1.getReviewCount());
                    break;

            }

            return retval;
        }

        public productComparator(int mode) {
            this.mode = mode;
        }

    }

    class productRemovePredicate implements Predicate<Product> {

        private final int minRev;
        private final int c;

        @Override
        public boolean test(Product t) {
            return t.getAvgReview() < minRev || (c != -1 && t.getCategory() != c);

        }

        public productRemovePredicate(int c, int minRev) {
            this.minRev = minRev;
            this.c = c;
        }

    }

    class productGeoRemovePredicate implements Predicate<Product> {

        private final int dist;
        private final double lat;
        private final double lng;

        public productGeoRemovePredicate(double lat, double lng, int dist) {
            this.lat = lat;
            this.lng = lng;
            this.dist = dist;
        }

        @Override
        public boolean test(Product t) {

            boolean test = false;
            try {
                Shop s = t.getShop();
                List<Coordinate> c = coordinateDAO.getByShop(s);

                /*
              Controllo se almeno un punto vendita di quel negozio rientra nel raggio della ricerca
              Se non ci sono punti vendita all'interno del raggio la funzione ritorna true, e applicando
              removeIf verrà eliminato il prodotto dalla lista
                 */
                for (Iterator<Coordinate> iterator = c.iterator(); iterator.hasNext();) {    // && test;) {
                    Coordinate coordinate = iterator.next();
                    test = test || distance(coordinate.getLatitude(), coordinate.getLongitude(), lat, lng) < dist;

                }

            } catch (DAOException ex) {
                Log.error("Error calculating distance for product " + t.getId() + ": " + ex.getMessage());
                test = false;
            }

            return !test;
        }

        /**
         * Calculate distance between two points in latitude and longitude. Uses
         * Haversine method as its base.
         *
         *
         * @returns Distance in KiloMeters
         */
        double distance(double lat1, double lng1, double lat2, double lng2) {
            double earthRadius = 6371.0;
            double dLat = Math.toRadians(lat2 - lat1);
            double dLng = Math.toRadians(lng2 - lng1);
            double sindLat = Math.sin(dLat / 2);
            double sindLng = Math.sin(dLng / 2);
            double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                    * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double dist = earthRadius * c;

            return dist;
        }

    }

}
