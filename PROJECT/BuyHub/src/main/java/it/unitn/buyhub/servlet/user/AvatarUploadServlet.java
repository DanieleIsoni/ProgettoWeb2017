package it.unitn.buyhub.servlet.user;


import it.unitn.buyhub.dao.UserDAO;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * With this servlet a user can upload his avatar picture
 * @author maxgiro96
 */
public class AvatarUploadServlet extends HttpServlet {

    private UserDAO userDao;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for user storage system");
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            userDao = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for user storage system");
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
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

        try {
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                //use iterator version with flag to avoid remove just submitted avatar (user press remove and upload at same time)
                //for (FileItem item : formItems) {
                boolean done=false;
                for (Iterator<FileItem> iterator = formItems.iterator(); iterator.hasNext() && !done;) {
                    FileItem item = iterator.next();
                    // processes only fields that are not form fields
                    if (!item.isFormField() && item.getFieldName().equals("avatar") && item.getSize()>0) {
                        String fileName = new File(item.getName()).getName();

                        Utility.saveJPEG(item.getInputStream(),"avatars/"+user.getUsername());
                        user.setAvatar("UploadedContent/avatars/"+user.getUsername()+".jpg");
                        userDao.update(user);
                        Log.info("User "+user.getUsername()+" changed avatar image");
                        done=true;
                        response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/myself.jsp"));

                    }
                    else  if(item.isFormField() && item.getFieldName().equals("remove"))
                    {
                        //RIMOZIONE AVATAR
                        user.setAvatar("images/noimage.png");
                        try {
                            userDao.update(user);
                        } catch (DAOException ex) {

                        Log.error("User "+user.getUsername()+" error removing avatar "+ex.getMessage());
                        response.sendRedirect(response.encodeRedirectURL(contextPath + "common/error.jsp"));

                        }
                        Log.info("User "+user.getUsername()+" removed avatar");
                        done=true;
                        response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/myself.jsp"));


                    }
                }
            }

            response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/myself.jsp"));


        } catch (Exception ex) {
           Log.error("Error in avatar upload:"+ ex.getMessage());

        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
