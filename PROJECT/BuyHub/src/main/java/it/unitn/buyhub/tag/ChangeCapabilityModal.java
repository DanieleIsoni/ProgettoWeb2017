package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.OrderDAO;
import it.unitn.buyhub.dao.OrderedProductDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Utility;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Prints a modal with buttons to change privileges for a user
 * @author massimo
 */
public class ChangeCapabilityModal extends SimpleTagSupport {

    private int id;

    PageContext pageContext;
    private ShopDAO shopDAO;
    private UserDAO userDAO;

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


            User u = userDAO.getByPrimaryKey(id);

            out.println("<button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#Modal"+id+"\">\n" +
                        Utility.getLocalizedString(pageContext, "change_capability")+"\n" +
                        "</button>");

            out.println(createModal(u));




        } catch(Exception e){
            Log.error(e);
            throw new JspException("Error creating DAOs:", e);
        }
        try {
                 out.println("</div>");
        } catch (Exception ex) {

            Log.error(ex);
            throw new JspException("Error in ChangeCapabilityModal tag: ", ex);

        }
    }

    public void setId(int id) {
        this.id = id;
    }

 private void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) pageContext.getAttribute("daoFactory", PageContext.APPLICATION_SCOPE);
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for capability modals");
        }
        try {
          userDAO=daoFactory.getDAO(UserDAO.class);

        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for capability modals", ex);
        }
    }




  private String createModal(User u)
    {



            String content="<!-- Modal -->\n" +
            "<div class=\"modal fade\" id=\"Modal"+u.getId()+"\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalCenterTitle\" aria-hidden=\"true\">\n" +
            "  <div class=\"modal-dialog modal-dialog-centered\" role=\"document\">\n" +
            "    <div class=\"modal-content\">\n" +
            "      <div class=\"modal-header\">\n" +
            "        <h5 class=\"modal-title\" id=\"exampleModalLongTitle\">"+Utility.getLocalizedString(pageContext, "change_capability")+": "
                        +u.getFirstName()+" "+u.getLastName()+"(#"+u.getId()+")</h5>\n" +
            "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
            "          <span aria-hidden=\"true\">&times;</span>\n" +
            "        </button>\n" +
            "      </div>\n" +
            "      <div class=\"modal-body\">\n"
                    + "<div class=\"container-fluid capability_modal\">";


            for(Utility.CAPABILITY c : Utility.CAPABILITY.values())
            {
                if(u.getCapability()!=c.ordinal())
                {
                    content+="<a class=\"btn btn-primary\" href=\"";
                    content+=Utility.getUrl(pageContext, "restricted/admin/ChangeCapability?id="+id+"&capability="+c.ordinal());
                    content+="\" role=\"button\">"+Utility.getLocalizedString(pageContext, "capability_"+c.ordinal())+"</a><br/>";
                }
                else
                {
                    content+="<a class=\"btn btn-success\" disabled  href=\"#";
                    content+="\" role=\"button\">"+Utility.getLocalizedString(pageContext, "capability_"+c.ordinal())+"</a><br/>";
                }
            }


            content+=
                    "</div>\n"+
            "      </div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>"+
            "</div>";



        return content;
    }


}
