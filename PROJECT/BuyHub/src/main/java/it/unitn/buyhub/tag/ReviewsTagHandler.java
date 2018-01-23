package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag to render a review into product page
 *
 * @author Massimo Girondi
 */
public class ReviewsTagHandler extends SimpleTagSupport {

    PageContext pageContext;
    ProductDAO productDAO;
    PictureDAO pictureDAO;
    ReviewDAO reviewDao;

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        pageContext = (PageContext) getJspContext();
        try {
            init();
        } catch (Exception e) {
        }

        SimpleDateFormat dt = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (pageContext.getRequest().getParameter("id") != null && !pageContext.getRequest().getParameter("id").equals("")) {
                setId(Integer.parseInt(pageContext.getRequest().getParameter("id").toString()));

                try {
                    Product product = productDAO.getByPrimaryKey(id);
                    out.println("<a href=\"product?id=" + product.getId() + "\" >" + product.getName() + "</a>");
                    out.println("<div class=\"row reviews\">");
                    List<Review> reviews = reviewDao.getByProduct(product, false);
                    if (reviews.size() == 0) {
                        out.println("<div>" + Utility.getLocalizedString(pageContext, "no_review") + "</div>");
                    } else {
                        for (Review rev : reviews) {

                            out.println("<div class=\"row review\">\n"
                                    + "\n"
                                    + "\n"
                                    + "\n"
                                    + "                        <div class=\"col-md-3\">\n"
                                    + "                            <div class=\"review_img_box\">\n"
                                    + "                                <img src=\"" + rev.getCreator().getAvatar() + "\" class=\"img-rounded img-responsive\">\n"
                                    + "                            </div>\n"
                                    + "                            <div class=\"review-block-name\"><a href=\"user/?id=" + rev.getCreator().getId() + "\">" + rev.getCreator().getFirstName() + "</a></div>\n"
                                    + "                            <div class=\"review-block-date\">" + dt.format(rev.getDateCreation()) + "\n"
                                    + "                                <br><time class=\"timeago\" datetime=\"" + dt2.format(rev.getDateCreation()) + "\"></time>\n"
                                    + "                            </div>\n"
                                    + "                        </div>\n"
                                    + "                        <div class=\"col-md-8\">\n"
                                    + "                            \n"
                                    + "                            <input type=\"hidden\" class=\"rating\" value=\"" + rev.getGlobalValue() + "\" data-readonly/>\n"
                                    + "                            <div class=\"review-block-title\"><a href=\"#rev" + rev.getId() + "\" id=\"rev" + rev.getId() + "\">\n"
                                    + "                                    " + rev.getTitle() + "\n"
                                    + "                                </a>\n"
                                    + "                            </div>\n"
                                    + "                            <div class=\"review-block-description\">" + rev.getDescription() + "\n"
                                    + "                                <br>   \n"
                                    + "                                <button class=\"btn\" data-toggle=\"collapse\" data-target=\"#collapsedRev" + rev.getId() + "\">" + Utility.getLocalizedString(pageContext, "show_more") + "</button>\n"
                                    + "\n"
                                    + "                                <div id=\"collapsedRev" + rev.getId() + "\" class=\"collapse\">\n"
                                    + "\n"
                                    + "                                    <div class=\"detailed_review_header\">\n"
                                    + "                                        \n"
                                    + "                                        " + Utility.getLocalizedString(pageContext, "quality") + ": <input type=\"hidden\" class=\"rating\" value=\"" + rev.getQuality() + "\" data-readonly/>\n"
                                    + "                                        <br>\n"
                                    + "                                        " + Utility.getLocalizedString(pageContext, "service") + ": <input type=\"hidden\" class=\"rating\" value=\"" + rev.getService() + "\" data-readonly/>\n"
                                    + "                                        <br>\n"
                                    + "                                        " + Utility.getLocalizedString(pageContext, "value_for_money") + ": <input type=\"hidden\" class=\"rating\" value=\"" + rev.getValueForMoney() + "\" data-readonly/>\n"
                                    + "\n"
                                    + "\n"
                                    + "                                    </div>\n"
                                    + "\n"
                                    + "                                </div>\n"
                                    + "                            </div>\n"
                                    + "\n"
                                    + "                        </div>\n"
                                    + "                        \n"
                                    + "\n"
                                    + "                    </div>"
                                    + "<hr>");
                        }
                    }

                } catch (DAOException ex) {
                    Logger.getLogger(ReviewsTagHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                out.println("</div>");
            }
        } catch (IOException ec) {
            Log.error("Error writing from CartTagHandler");
        }

    }

    private void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) pageContext.getAttribute("daoFactory", PageContext.APPLICATION_SCOPE);
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            reviewDao = daoFactory.getDAO(ReviewDAO.class);
            pictureDAO = daoFactory.getDAO(PictureDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for product storage system", ex);
        }
    }

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
