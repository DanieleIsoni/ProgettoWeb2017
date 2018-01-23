package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.dao.persistence.factories.jdbc.JDBCDAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Utility;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag to prunt the product table into the shop page
 * @author Daniso
 */
public class ProductTableTagHandler extends SimpleTagSupport {

    private int shopId;
    private boolean owner;

    PageContext pageContext;
    private ShopDAO shopDAO;
    private ProductDAO productDAO;
    List<Product> products;

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {

        JspWriter out = getJspContext().getOut();
        pageContext = (PageContext) getJspContext();
        try{
            init();
        } catch(Exception e){
            Log.error(e);
        }

        try {
           try {
               products = productDAO.getByShop(shopDAO.getByPrimaryKey(shopId));
            } catch (DAOException ex) {
                Log.error(ex);
            }

            out.println("<div class=\"row\">\n" +
"                <div class=\"row shop_page_shipment_info\">\n" +
                        Utility.getLocalizedString(pageContext,"all_products_shop")+
"                </div>\n" +
"                <table class=\"table table-striped table-bordered\" id=\"products_table\">\n" +
"                    <thead>\n" +
"                    <td> "+Utility.getLocalizedString(pageContext,"name")+"</td>\n" +
"                    <td> "+Utility.getLocalizedString(pageContext,"category")+"</td>\n" +
"                    <td> "+Utility.getLocalizedString(pageContext,"price")+"</td>\n");
            if (owner){
                out.println("<td>\n" +
"                        <a href=\"addProduct.jsp?shopId="+shopId+"\" title=\""+Utility.getLocalizedString(pageContext,"add_product")+"\" class=\"btn btn-primary a-btn-slide-text mybtn\">\n" +
"                            <span class=\"glyphicon myglyph glyphicon-plus\" aria-hidden=\"true\"></span>          \n" +
"                        </a>\n" +
"                    </td>\n");
            }
            out.println("</thead>");
            if (products != null){
                for(Product product: products){
                    out.println("<tr>\n" +
"                            <td><a href=\"product?id="+product.getId()+"\"/>"+product.getName()+"</a></td>\n" +
"                            <td>"+Utility.getCategory(pageContext, product.getCategory())+" </td>\n" +
"                            <td>\n" +
"                                € "+String.format("%.2f", product.getPrice())+"\n" +
"                            </td>\n");
                    if(owner){
                        out.println("<td>\n" +
"                                <a href=\"../EditProductServlet?code=1&prodId="+product.getId()+"\" title=\""+Utility.getLocalizedString(pageContext, "edit_product")+"\" class=\"btn btn-primary a-btn-slide-text mybtn\">\n" +
"                                    <span class=\"glyphicon myglyph glyphicon-edit\" aria-hidden=\"true\"></span>          \n" +
"                                </a>\n" +
"                                <a href=\"../DeleteProductServlet?prodId="+product.getId()+"\" title=\""+Utility.getLocalizedString(pageContext, "delete_product")+"\" class=\"btn btn-danger a-btn-slide-text mybtn\" onclick=\"return confirm('Are you sure you want to continue')\">\n" +
"                                    <span class=\"glyphicon myglyph glyphicon-remove\" aria-hidden=\"true\"></span>          \n" +
"                                </a>\n" +
"                                <a href=\"../productPhoto?id="+product.getId()+"\" title=\""+Utility.getLocalizedString(pageContext, "product_photo_title")+"\" class=\"btn btn-secondary a-btn-slide-text mybtn\">\n" +
"                                    <span class=\"glyphicon myglyph glyphicon-camera\" aria-hidden=\"true\"></span>          \n" +
"                                </a>\n" +
"                            </td>\n");
                    }
                    out.println("</tr>");
                }
            }
            out.println("</table>\n" +
"               </div>");
        } catch (java.io.IOException ex) {
            throw new JspException("Error in ProductTableTagHandler tag", ex);
        }
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    private void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) pageContext.getAttribute("daoFactory", PageContext.APPLICATION_SCOPE);
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for product table printer");
        }
        try {
            shopDAO = daoFactory.getDAO(ShopDAO.class);
            productDAO = daoFactory.getDAO(ProductDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for product table printer", ex);
        }
    }

}
