/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.filter;

import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.utils.Log;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Matteo Battilana
 */
public class AuthenticationFilter implements Filter {

    private static final boolean DEBUG = false;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured.
    private FilterConfig filterConfig = null;

    public AuthenticationFilter() {
    }

    private boolean doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (DEBUG) {
            log("AuthenticationFilter:DoBeforeProcessing");
        }

        if (request instanceof HttpServletRequest) {
            ServletContext servletContext = ((HttpServletRequest) request).getServletContext();
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            User user = null;
            if (session != null) {
                user = (User) session.getAttribute("authenticatedUser");
            }
            if (user == null) {
                String contextPath = servletContext.getContextPath();
                if (!contextPath.endsWith("/")) {
                    contextPath += "/";
                }

                //Save the target url to redirect after login
                HttpServletRequest req = ((HttpServletRequest) request);
                String qs = "";
                if (req.getQueryString() != null) {
                    qs = "?" + req.getQueryString();
                }
                req.getSession().setAttribute("origin", req.getRequestURI() + qs);
                //Log.info(((HttpServletRequest) request).getRequestURI());
                ((HttpServletResponse) response).sendRedirect(((HttpServletResponse) response).encodeRedirectURL(contextPath + "login.jsp"));
                return false;
            }
        }

        return true;
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (DEBUG) {
            log("AuthenticationFilter:DoAfterProcessing");
        }
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     *
     * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
     * @since 2017.04.25
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        if (DEBUG) {
            log("AuthenticationFilter:doFilter()");
        }

        if (!doBeforeProcessing(request, response)) {
            return;
        }

        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException | NullPointerException t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            log("Some problem in filter chain", t);
        }

        doAfterProcessing(request, response);

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     *
     * @return
     *
     * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
     * @since 2017.04.25
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     *
     * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
     * @since 2017.04.25
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     *
     * @param filterConfig
     *
     * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
     * @since 2017.04.25
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (DEBUG) {
                log("AuthenticationFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     *
     * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
     * @since 2017.04.25
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthenticationFilter()");
        }
        StringBuilder sb = new StringBuilder("AuthenticationFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                try (PrintStream ps = new PrintStream(response.getOutputStream()); PrintWriter pw = new PrintWriter(ps)) {
                    pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                    // PENDING! Localize this for next official release
                    pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                    pw.print(stackTrace);
                    pw.print("</pre></body>\n</html>"); //NOI18N
                }
                response.getOutputStream().close();
            } catch (IOException | NullPointerException ex) {
                log("Error during exception notification", ex);
            }
        } else {
            try {
                try (PrintStream ps = new PrintStream(response.getOutputStream())) {
                    t.printStackTrace(ps);
                }
                response.getOutputStream().close();
            } catch (IOException | NullPointerException ex) {
                log("Error during exception notification", ex);
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (IOException | NullPointerException ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
        Log.info(msg);
    }

    public void log(String msg, Throwable throwable) {
        filterConfig.getServletContext().log(msg, throwable);
        Log.error(msg + ":" + throwable.getMessage());
    }
}
