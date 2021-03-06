package it.unitn.buyhub.utils;

/**
 * This servlet is used to send a mail to a given address. The mail is sent in
 * async mode with a HTML body. See the documentation in pdf to find more
 * information.
 *
 * @author Massimo Girondi
 */
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

class RunnableMailer implements Runnable {

    /**
     * Runnable class to send the mail in separate thread
     */
    private String from;
    private String to;
    private String subject;
    private String body;
    private String url;
    private String button_txt;

    public RunnableMailer(String from, String to, String subject, String body, String url, String button_txt) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.url = url;
        this.button_txt = button_txt;
    }

    public void run() {
        Mailer.sendMail(from, to, subject, body, url, button_txt);

    }
}

public class Mailer {

    public static void mail(String from, String to, String subject, String body, String url, String button_txt) {

        //Invoke the runnable to send the mail
        //Commented for testing purposes
        //new RunnableMailer(from, to, subject, body, url, button_txt).run();
        Log.info("Send mail to " + to + ", subject: " + subject + ", url:" + url + ", body: " + body);
    }

    /*
    A function to send an email to all the admins in the system
     */
    public static void mailToAdmins(String from, String subject, String body, String url, String button_txt, ServletContext context) {
        try {
            UserDAO userDao;
            DAOFactory daoFactory = (DAOFactory) context.getAttribute("daoFactory");
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

            List<User> admins = userDao.getAdmins();

            //For each admin in the list, send an email
            for (User admin : admins) {
                mail(from, admin.getEmail(), subject, body, url, button_txt);
            }

        } catch (Exception ex) {
            Log.warn("Error sending mail to admins: " + ex.getMessage());
        }
    }

    /**
     * Method called by the runnable to send the mail
     */
    static void sendMail(String from, String to, String subject, String body, String url, String button_txt) {

        final String username = "buyhub_test@girondi.net";
        final String password = "u!l937Ik00[.";
        final String from_address = "no_reply@buyhub.com";

        Properties props = new Properties();

        //MODIFY WITH YOUR CREDENTIALS
        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "srv-hp1.netsons.net");
        //      props.put("mail.smtp.port", "465");

        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        //session.setDebug(true);
        try {

            Message message = new MimeMessage(session);

            try {
                message.setFrom(new InternetAddress(from_address, from + " via BuyHub"));
            } catch (UnsupportedEncodingException ex) {
                Log.warn("Error encoding mail address:" + ex.getMessage());
                throw new MessagingException("Error encoding");
            }

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject + " - BuyHub");

            //message.setText(buildMail(subject, body, url, button_txt));
            message.setContent(buildMail(subject, body, url, button_txt), "text/html; charset=utf-8");
            Transport.send(message);
            Log.info("Sent mail to " + to + " succesfully.");
        } catch (MessagingException e) {
            Log.warn("Error sending mail to " + to + ": " + e.getMessage());

            throw new RuntimeException(e);
        }

    }

    /**
     * Function to build the HTML message
     */
    private static String buildMail(String title, String content, String url, String button_txt) {
        String preview = content;
        String s = "<!doctype html>\n"
                + "<html>\n"
                + "  <head>\n"
                + "    <meta name=\"viewport\" content=\"width=device-width\" />\n"
                + "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
                + "    <title>" + title + "</title>\n"
                + "    <style>\n"
                + "      /* -------------------------------------\n"
                + "          GLOBAL RESETS\n"
                + "      ------------------------------------- */\n"
                + "      img {\n"
                + "        border: none;\n"
                + "        -ms-interpolation-mode: bicubic;\n"
                + "        max-width: 100%; }\n"
                + "\n"
                + "      body {\n"
                + "        background-color: #f6f6f6;\n"
                + "        font-family: sans-serif;\n"
                + "        -webkit-font-smoothing: antialiased;\n"
                + "        font-size: 14px;\n"
                + "        line-height: 1.4;\n"
                + "        margin: 0;\n"
                + "        padding: 0;\n"
                + "        -ms-text-size-adjust: 100%;\n"
                + "        -webkit-text-size-adjust: 100%; }\n"
                + "\n"
                + "      table {\n"
                + "        border-collapse: separate;\n"
                + "        mso-table-lspace: 0pt;\n"
                + "        mso-table-rspace: 0pt;\n"
                + "        width: 100%; }\n"
                + "        table td {\n"
                + "          font-family: sans-serif;\n"
                + "          font-size: 14px;\n"
                + "          vertical-align: top; }\n"
                + "\n"
                + "      /* -------------------------------------\n"
                + "          BODY & CONTAINER\n"
                + "      ------------------------------------- */\n"
                + "\n"
                + "      .body {\n"
                + "        background-color: #f6f6f6;\n"
                + "        width: 100%; }\n"
                + "\n"
                + "      /* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */\n"
                + "      .container {\n"
                + "        display: block;\n"
                + "        Margin: 0 auto !important;\n"
                + "        /* makes it centered */\n"
                + "        max-width: 580px;\n"
                + "        padding: 10px;\n"
                + "        width: 580px; }\n"
                + "\n"
                + "      /* This should also be a block element, so that it will fill 100% of the .container */\n"
                + "      .content {\n"
                + "        box-sizing: border-box;\n"
                + "        display: block;\n"
                + "        Margin: 0 auto;\n"
                + "        max-width: 580px;\n"
                + "        padding: 10px; }\n"
                + "\n"
                + "      /* -------------------------------------\n"
                + "          HEADER, FOOTER, MAIN\n"
                + "      ------------------------------------- */\n"
                + "      .main {\n"
                + "        background: #ffffff;\n"
                + "        border-radius: 3px;\n"
                + "        width: 100%; }\n"
                + "\n"
                + "      .wrapper {\n"
                + "        box-sizing: border-box;\n"
                + "        padding: 20px; }\n"
                + "\n"
                + "      .content-block {\n"
                + "        padding-bottom: 10px;\n"
                + "        padding-top: 10px;\n"
                + "      }\n"
                + "\n"
                + "      .footer {\n"
                + "        clear: both;\n"
                + "        Margin-top: 10px;\n"
                + "        text-align: center;\n"
                + "        width: 100%; }\n"
                + "        .footer td,\n"
                + "        .footer p,\n"
                + "        .footer span,\n"
                + "        .footer a {\n"
                + "          color: #999999;\n"
                + "          font-size: 12px;\n"
                + "          text-align: center; }\n"
                + "\n"
                + "      /* -------------------------------------\n"
                + "          TYPOGRAPHY\n"
                + "      ------------------------------------- */\n"
                + "      h1,\n"
                + "      h2,\n"
                + "      h3,\n"
                + "      h4 {\n"
                + "        color: #000000;\n"
                + "        font-family: sans-serif;\n"
                + "        font-weight: 400;\n"
                + "        line-height: 1.4;\n"
                + "        margin: 0;\n"
                + "        Margin-bottom: 30px; }\n"
                + "\n"
                + "      h1 {\n"
                + "        font-size: 35px;\n"
                + "        font-weight: 300;\n"
                + "        text-align: center;\n"
                + "        text-transform: capitalize; }\n"
                + "\n"
                + "      p,\n"
                + "      ul,\n"
                + "      ol {\n"
                + "        font-family: sans-serif;\n"
                + "        font-size: 14px;\n"
                + "        font-weight: normal;\n"
                + "        margin: 0;\n"
                + "        Margin-bottom: 15px; }\n"
                + "        p li,\n"
                + "        ul li,\n"
                + "        ol li {\n"
                + "          list-style-position: inside;\n"
                + "          margin-left: 5px; }\n"
                + "\n"
                + "      a {\n"
                + "        color: #3498db;\n"
                + "        text-decoration: underline; }\n"
                + "\n"
                + "      /* -------------------------------------\n"
                + "          BUTTONS\n"
                + "      ------------------------------------- */\n"
                + "      .btn {\n"
                + "        box-sizing: border-box;\n"
                + "        width: 100%; }\n"
                + "        .btn > tbody > tr > td {\n"
                + "          padding-bottom: 15px; }\n"
                + "        .btn table {\n"
                + "          width: auto; }\n"
                + "        .btn table td {\n"
                + "          background-color: #ffffff;\n"
                + "          border-radius: 5px;\n"
                + "          text-align: center; }\n"
                + "        .btn a {\n"
                + "          background-color: #ffffff;\n"
                + "          border: solid 1px #3498db;\n"
                + "          border-radius: 5px;\n"
                + "          box-sizing: border-box;\n"
                + "          color: #3498db;\n"
                + "          cursor: pointer;\n"
                + "          display: inline-block;\n"
                + "          font-size: 14px;\n"
                + "          font-weight: bold;\n"
                + "          margin: 0;\n"
                + "          padding: 12px 25px;\n"
                + "          text-decoration: none;\n"
                + "          text-transform: capitalize; }\n"
                + "\n"
                + "      .btn-primary table td {\n"
                + "        background-color: #3498db; }\n"
                + "\n"
                + "      .btn-primary a {\n"
                + "        background-color: #3498db;\n"
                + "        border-color: #3498db;\n"
                + "        color: #ffffff; }\n"
                + "\n"
                + "      /* -------------------------------------\n"
                + "          OTHER STYLES THAT MIGHT BE USEFUL\n"
                + "      ------------------------------------- */\n"
                + "      .last {\n"
                + "        margin-bottom: 0; }\n"
                + "\n"
                + "      .first {\n"
                + "        margin-top: 0; }\n"
                + "\n"
                + "      .align-center {\n"
                + "        text-align: center; }\n"
                + "\n"
                + "      .align-right {\n"
                + "        text-align: right; }\n"
                + "\n"
                + "      .align-left {\n"
                + "        text-align: left; }\n"
                + "\n"
                + "      .clear {\n"
                + "        clear: both; }\n"
                + "\n"
                + "      .mt0 {\n"
                + "        margin-top: 0; }\n"
                + "\n"
                + "      .mb0 {\n"
                + "        margin-bottom: 0; }\n"
                + "\n"
                + "      .preheader {\n"
                + "        color: transparent;\n"
                + "        display: none;\n"
                + "        height: 0;\n"
                + "        max-height: 0;\n"
                + "        max-width: 0;\n"
                + "        opacity: 0;\n"
                + "        overflow: hidden;\n"
                + "        mso-hide: all;\n"
                + "        visibility: hidden;\n"
                + "        width: 0; }\n"
                + "\n"
                + "      .powered-by a {\n"
                + "        text-decoration: none; }\n"
                + "\n"
                + "      hr {\n"
                + "        border: 0;\n"
                + "        border-bottom: 1px solid #f6f6f6;\n"
                + "        Margin: 20px 0; }\n"
                + "\n"
                + "      /* -------------------------------------\n"
                + "          RESPONSIVE AND MOBILE FRIENDLY STYLES\n"
                + "      ------------------------------------- */\n"
                + "      @media only screen and (max-width: 620px) {\n"
                + "        table[class=body] h1 {\n"
                + "          font-size: 28px !important;\n"
                + "          margin-bottom: 10px !important; }\n"
                + "        table[class=body] p,\n"
                + "        table[class=body] ul,\n"
                + "        table[class=body] ol,\n"
                + "        table[class=body] td,\n"
                + "        table[class=body] span,\n"
                + "        table[class=body] a {\n"
                + "          font-size: 16px !important; }\n"
                + "        table[class=body] .wrapper,\n"
                + "        table[class=body] .article {\n"
                + "          padding: 10px !important; }\n"
                + "        table[class=body] .content {\n"
                + "          padding: 0 !important; }\n"
                + "        table[class=body] .container {\n"
                + "          padding: 0 !important;\n"
                + "          width: 100% !important; }\n"
                + "        table[class=body] .main {\n"
                + "          border-left-width: 0 !important;\n"
                + "          border-radius: 0 !important;\n"
                + "          border-right-width: 0 !important; }\n"
                + "        table[class=body] .btn table {\n"
                + "          width: 100% !important; }\n"
                + "        table[class=body] .btn a {\n"
                + "          width: 100% !important; }\n"
                + "        table[class=body] .img-responsive {\n"
                + "          height: auto !important;\n"
                + "          max-width: 100% !important;\n"
                + "          width: auto !important; }}\n"
                + "\n"
                + "      /* -------------------------------------\n"
                + "          PRESERVE THESE STYLES IN THE HEAD\n"
                + "      ------------------------------------- */\n"
                + "      @media all {\n"
                + "        .ExternalClass {\n"
                + "          width: 100%; }\n"
                + "        .ExternalClass,\n"
                + "        .ExternalClass p,\n"
                + "        .ExternalClass span,\n"
                + "        .ExternalClass font,\n"
                + "        .ExternalClass td,\n"
                + "        .ExternalClass div {\n"
                + "          line-height: 100%; }\n"
                + "        .apple-link a {\n"
                + "          color: inherit !important;\n"
                + "          font-family: inherit !important;\n"
                + "          font-size: inherit !important;\n"
                + "          font-weight: inherit !important;\n"
                + "          line-height: inherit !important;\n"
                + "          text-decoration: none !important; }\n"
                + "        .btn-primary table td:hover {\n"
                + "          background-color: #34495e !important; }\n"
                + "        .btn-primary a:hover {\n"
                + "          background-color: #34495e !important;\n"
                + "          border-color: #34495e !important; } }\n"
                + "\n"
                + "    </style>\n"
                + "  </head>\n"
                + "  <body class=\"\">\n"
                + "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"body\">\n"
                + "      <tr>\n"
                + "        <td>&nbsp;</td>\n"
                + "        <td class=\"container\">\n"
                + "          <div class=\"content\">\n"
                + "\n"
                + "            <!-- START CENTERED WHITE CONTAINER -->\n"
                + "            <span class=\"preheader\">" + preview + "</span>\n"
                + "            <table class=\"main\">\n"
                + "\n"
                + "              <!-- START MAIN CONTENT AREA -->\n"
                + "              <tr>\n"
                + "                <td class=\"wrapper\">\n"
                + "                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                + "                    <tr>\n"
                + "                      <td>\n"
                //+ "                        <p>Hi there,</p>\n"
                + "                        <p>" + content + "</p>\n";
        if (url != null && url != "") {
            s += "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"btn btn-primary\">\n"
                    + "                          <tbody>\n"
                    + "                            <tr>\n"
                    + "                              <td align=\"left\">\n"
                    + "                                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                    + "                                  <tbody>\n"
                    + "                                    <tr>\n"
                    + "                                      <td> <a href=\"" + url + "\" target=\"_blank\">";
            if (button_txt != null && button_txt != "") {
                s += button_txt;
            } else {
                s += "See on BuyHub";
            }
            s += "</a> </td>\n"
                    + "                                    </tr>\n"
                    + "                                  </tbody>\n"
                    + "                                </table>\n"
                    + "                              </td>\n"
                    + "                            </tr>\n"
                    + "                          </tbody>\n"
                    + "                        </table>\n";
        }
        s += " <p>Sincerely, BuyHub.</p>\n"
                + "                      </td>\n"
                + "                    </tr>\n"
                + "                  </table>\n"
                + "                </td>\n"
                + "              </tr>\n"
                + "\n"
                + "            <!-- END MAIN CONTENT AREA -->\n"
                + "            </table>\n"
                + "\n"
                + "            <!-- START FOOTER -->\n"
                + "            <div class=\"footer\">\n"
                + "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                + "                <tr>\n"
                + "                  <td class=\"content-block\">\n"
                + "                    <span class=\"apple-link\">BuyHub - P.I. 123412312312</span>\n"
                + "                  </td>\n"
                + "                </tr>\n"
                + "              </table>\n"
                + "            </div>\n"
                + "            <!-- END FOOTER -->\n"
                + "\n"
                + "          <!-- END CENTERED WHITE CONTAINER -->\n"
                + "          </div>\n"
                + "        </td>\n"
                + "        <td>&nbsp;</td>\n"
                + "      </tr>\n"
                + "    </table>\n"
                + "  </body>\n"
                + "</html>";

        return s;

    }

}
