/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;

/**
 *
 * @author massimo
 */
public class Utility {
    /**
     * Use this function to translate a string using the Bundles
     * It's an equivalent of fmt:message JSP tag
     */
     public  static String getLocalizedString(PageContext pageContext,final String key) {
        String value = LocaleSupport.getLocalizedMessage(pageContext, key, "bundle.buyhubBundle");

        return value;
    }
     /**
      * To retrieve a url correct for the contest
      */
     public static String getUrl(PageContext pageContext,final String url)
     {
         HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
              return String.format("%s/"+url, request.getContextPath());
    
     }
     /**
      * To get the category name based on its ID and the locale
      */
     public static String getCategory(PageContext pageContext, int key)
     {
         return getLocalizedString(pageContext, "category_"+key);
     }
 
     
     /**
      * Enumerator to manage the users capability
      */
     static public enum CAPABILITY{INVALID,USER,SHOP,ADMIN};
     
     
     
     /**
      * Function to save a generic file to a given filename inside the uploadedContentFolder
      * @param is the InputFileStream to save
      * @param name the name of the file, with the extension
      * @return the file name
      */
     public static String saveFile(InputStream is,String name)
     {
        
         try {
             String uploads=PropertyHandler.getInstance().getValue("uploadedContentFolder");
             File uploadsfolder=new File(uploads);
             // if the directory does not exist, create it
             if (!uploadsfolder.exists()) {
                 Log.info("The uploadedContentFolder is not present. Creating it to "+uploadsfolder.getAbsolutePath());
                 
                 boolean result = false;
                 try{
                     uploadsfolder.mkdir();
                     result = true;
                 }
                 catch(SecurityException se){
                     Log.error("Error creating the directory: "+se.getMessage());
                 }
             }
             
             File file = new File(uploads,name);
             file.createNewFile();
             
             Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
             Log.info("File saved to "+file.getAbsolutePath());
             return "0";
         } catch (IOException ex) {
           Log.error("Error saving file: "+ex.getMessage());
          }
         
         return name;
     }
     
     
     /**
      * Function to save a generic file to a random file inside the uploadedContentFolder
      * @param is the InputFileStream to save
      * @param ext the extension of the original file
      * @return the file name
      */
      public static String saveFileUUID(InputStream is, String ext)
     {
        String name=UUID.randomUUID().toString()+"."+ext;
        return saveFile(is, name);
     }
      
      
     /**
      * Function to save a picture to a JPEG encoded file, with a random name, inside the uploadedContent folder
      * It should convert automatically other formats to JPEG, but the library have some bug with the alpha channel
      * and sometimes the colors aren't correct. To solve it, save only JPEG images
      * 
      * @param is the InputFileStream of the picture to save
      * @return the file name
      * 
      */
      
     
     public static String saveJPEG(InputStream is)
     {
         
         return saveJPEG(is,UUID.randomUUID().toString());
         
     }
     
     
     public static String saveJPEG(InputStream is,String filename)
     {
         String format="jpg";
         String name=filename+"."+format;
      
          try {
             String uploads=PropertyHandler.getInstance().getValue("uploadedContentFolder");
             File uploadsfolder=new File(uploads);
             // if the directory does not exist, create it
             if (!uploadsfolder.exists()) {
                 Log.info("The uploadedContentFolder is not present. Creating it to "+uploadsfolder.getAbsolutePath());
                 
                 boolean result = false;
                 try{
                     uploadsfolder.mkdir();
                     result = true;
                 }
                 catch(SecurityException se){
                     Log.error("Error creating the directory: "+se.getMessage());
                 }
             }
             
            File file = new File(uploads,name);
            file.createNewFile();
            ImageInputStream iis = ImageIO.createImageInputStream(is);
            BufferedImage image = ImageIO.read(iis);
            try (OutputStream os = new FileOutputStream(file)) {
                ImageOutputStream ios = ImageIO.createImageOutputStream(os);
                //Aggiungere qui eventuale codice per il ridimensionamento delle immagini
                
                ImageIO.write(image, format, ios);
                Log.info("Image save to: "+file.getAbsolutePath());
            } catch (Exception exp) {
                exp.printStackTrace();
            }
          } catch (IOException ex) {
           Log.error("Error saving file: "+ex.getMessage());
          }
         
         return name;
         
     }
     
}


