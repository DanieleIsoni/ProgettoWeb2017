package it.unitn.buyhub.servlet.user;


import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Utility;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * This servlet allow the upload of a picture related to a product.
 * @author maxgiro96
 */
public class productPictureUploadServlet extends HttpServlet {

    private PictureDAO pictureDao;
    private ProductDAO productDao;


    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for picture storage system");
            throw new ServletException("Impossible to get dao factory for picture storage system");
        }
        try {
            pictureDao = daoFactory.getDAO(PictureDAO.class);
            productDao = daoFactory.getDAO(ProductDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for picture storage system");
            throw new ServletException("Impossible to get dao factory for picture storage system", ex);
        }

        Log.info("AvatUploadServlet init done");
    }




        // location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "upload";

    // upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB

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


        User user = (User) request.getSession().getAttribute("authenticatedUser");
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        // checks if the request actually contains upload file
        if (!ServletFileUpload.isMultipartContent(request)) {

            // if not, we stop here
            PrintWriter writer = response.getWriter();
            writer.println("Error: Form must has enctype=multipart/form-data.");
            writer.flush();
            Log.warn("Avatar upload wrong enctype");
            return;

        }


        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        // sets maximum size of upload file
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // sets maximum size of request (include file + form data)
        upload.setSizeMax(MAX_REQUEST_SIZE);


        Product product=null;
        try {
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                //use iterator version with flag to avoid remove just submitted productPicture (user press remove and upload at same time)
                //for (FileItem item : formItems) {



                boolean done=false;

                for (Iterator<FileItem> iterator = formItems.iterator(); iterator.hasNext() && !done;) {
                    FileItem item = iterator.next();
                    if(item.isFormField() && item.getFieldName().equals("product") && item.getSize()>0)
                    {
                        int id= Integer.parseInt(item.getString());

                        product=productDao.getByPrimaryKey(id);
                        done=true;
                    }
                }
                done=false;
                if(product ==null)
                    throw new Exception("Missing product id!");

                for (Iterator<FileItem> iterator = formItems.iterator(); iterator.hasNext() && !done;) {
                    FileItem item = iterator.next();
                    // processes only fields that are not form fields
                    if (!item.isFormField() && item.getFieldName().equals("picture") && item.getSize()>0) {
                        String fileName = new File(item.getName()).getName();

                        String name = Utility.saveJPEG(item.getInputStream());
                        Picture p=new Picture();
                        p.setDescription(product.getName());
                        p.setName(product.getName());
                        p.setOwner(user);
                        p.setPath("UploadedContent/"+name);
                        pictureDao.insertProductPicture(product,p);

                        Log.info("User "+user.getUsername()+" uploaded product image for product #"+product.getId());
                        done=true;
                        response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/productPhoto?id="+product.getId()));

                    }
                }
            }

            //response.sendRedirect(response.encodeRedirectURL(contextPath + "productPhoto?id="+product.getId())));


        } catch (Exception ex) {
           Log.error("Error in productPicture upload:"+ ex.getMessage());
           Logger.getLogger(productPictureUploadServlet.class.getName()).log(Level.SEVERE, null, ex);

           response.sendRedirect(response.encodeRedirectURL("../common/error.jsp"));


        }


    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
