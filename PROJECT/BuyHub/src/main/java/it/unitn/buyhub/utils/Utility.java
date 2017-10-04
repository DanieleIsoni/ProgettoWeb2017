/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;

/**
 *
 * @author massimo
 */
public class Utility {
     public  static String getLocalizedString(PageContext pageContext,final String key) {
        String value = LocaleSupport.getLocalizedMessage(pageContext, key, "bundle.buyhubBundle");

        return value;
    }
     
     public static String getUrl(PageContext pageContext,final String url)
     {
         HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
              return String.format("%s/"+url, request.getContextPath());
    
     }
     public static String getCategory(PageContext pageContext, int key)
     {
         return getLocalizedString(pageContext, "category_"+key);
     }
 
     static public enum CAPABILITY{INVALID,USER,SHOP,ADMIN};
     
     
     
     
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
     
}


