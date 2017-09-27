/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.utils;

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
     
}
