package it.unitn.buyhub.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * This tag check if thereis already a language parameter and updates it or add
 * it to the querystring Used in language change dropdown
 *
 * @author Massimo Girondi
 */
public class LanguageLinkTagHandler extends SimpleTagSupport {

    private String lang;

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        PageContext pageContext = (PageContext) getJspContext();
        try {
            HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

            out.println(changeLanguageParam(req.getQueryString(), lang));

        } catch (java.io.IOException ex) {
            throw new JspException("Error in LanguageLinkTagHandler tag", ex);
        }
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    private String changeLanguageParam(String queryString, String newlang) {
        queryString = "?" + queryString;
        if (queryString.contains("language=")) {
            queryString = queryString.replaceAll("language=([a-z])([a-z])", ("language=" + newlang));
        } else {
            if (queryString != null) {
                queryString += "&";
            }
            queryString += "language=" + newlang;
        }
        return queryString;

    }
}
