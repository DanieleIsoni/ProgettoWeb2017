package it.unitn.buyhub.servlet;

import it.unitn.buyhub.utils.PropertyHandler;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to solve a UploadedContent url. It retrieves the file from the folder
 * (outside web container) and serve it inline
 *
 * @author Massimo Girondi
 */
public class UploadedContentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");

        File file = new File(PropertyHandler.getInstance().getValue("uploadedContentFolder"), filename);

        //if the image is not present, load the dummy image
        if (!file.exists()) {
            file = new File(getServletContext().getRealPath("images/noimage.png"));
        }
        //System.out.println(file.getAbsolutePath());

        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }
}
