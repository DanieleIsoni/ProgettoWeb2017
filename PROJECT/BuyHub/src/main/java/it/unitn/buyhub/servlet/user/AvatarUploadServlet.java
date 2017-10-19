/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet.user;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.utils.Log;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author maxgiro96
 */
public class AvatarUploadServlet extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        
         
        String dirName = "uploadedContent/avatars/";
        
        String  realPath = getServletContext().getRealPath(dirName);
        // Use an advanced form of the constructor that specifies a character
        // encoding of the request (not of the file contents) and a file
        // rename policy.
        MultipartRequest multi = new MultipartRequest(request, "/tmp/", 1024 * 1024, "ISO-8859-1", new DefaultFileRenamePolicy());
        Enumeration params = multi.getParameterNames();
        boolean done=false;
        while (params.hasMoreElements() && !done) {
            String name = (String) params.nextElement();
            if (name == "avatar") {
                String value = multi.getParameter(name);
                Enumeration files = multi.getFileNames();
                while (files.hasMoreElements() && !done) {
                    
                    String filename = (String) files.nextElement();
                    File f = multi.getFile(filename);
                    String user = ((User) request.getSession().getAttribute("authenticatedUser")).getUsername();
                    
                    File avatar = new File(realPath+ user + ".jpg");
                    BufferedImage image = ImageIO.read(f);
                    
                    ImageIO.write(image, "jpg", avatar);
                    
                    //RIDIMENSIONARE??
                    done=true;

                }
            }
        }
       
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}