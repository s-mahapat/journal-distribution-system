
package IAS.Controller.masterdata;

import IAS.Model.masterdata.annualSubscriptionRateModel;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import IAS.Class.JDSLogger;
import IAS.Class.msgsend;
import IAS.Class.util;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

/**
 *
 * @author aloko
 */
public class annualSubscriptionRate extends HttpServlet {
    private annualSubscriptionRateModel _annualSubscriptionRateModel = null;
    private static final Logger logger = JDSLogger.getJDSLogger("IAS.Controller.masterData");

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String url = null;

        try {
            _annualSubscriptionRateModel = new IAS.Model.masterdata.annualSubscriptionRateModel(request);

            if(action.equalsIgnoreCase("add")){

                String xml = _annualSubscriptionRateModel.add();
                request.setAttribute("xml", xml);
                url = "/xmlserver";

            }else if(action.equalsIgnoreCase("search")){

                String xml = _annualSubscriptionRateModel.search();
                request.setAttribute("xml", xml);
                url = "/xmlserver";

            }else if(action.equalsIgnoreCase("addAndSearch")){

                String xml = _annualSubscriptionRateModel.addAndSearch();
                request.setAttribute("xml", xml);
                url = "/xmlserver";

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            ServletContext context = getServletContext();
            String emailPropertiesFile = context.getRealPath("/WEB-INF/classes/jds_email.properties");
            msgsend smtpMailSender = new msgsend();
            smtpMailSender.sendMailWithAuthentication(
                    emailPropertiesFile,
                    "jds.adm.all@gmail.com", "", "",
                    "Exception generated in JDS code",
                    util.getExceptionStackTraceAsString(e),
                    "JDS", "");

            throw new javax.servlet.ServletException(e);

        } finally {
            if(url == null){
                url = "/jsp/errors/404.jsp";
                logger.error("Redirect url was not found, forwarding to 404");
            }
            else
            {
                logger.debug("Called->" + url);
            }
            RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
            if (rd != null && url != null) {
                rd.forward(request, response);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}