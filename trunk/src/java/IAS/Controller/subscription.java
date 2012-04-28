/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IAS.Controller;

import IAS.Class.JDSLogger;
import IAS.Class.util;
import IAS.Model.Subscriber.subscriberModel;
import IAS.Model.Subscription.SubscriptionModel;
import java.io.IOException;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.log4j.Logger;

public class subscription extends JDSController {

    private static final Logger logger = JDSLogger.getJDSLogger("IAS.Controller.subscription");

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String oper = request.getParameter("oper") != null ? request.getParameter("oper") : "noop";
        String action = request.getParameter("action") != null ? request.getParameter("action") : "noop";
        String url = null;

        try {
            SubscriptionModel _subscriptionModel = new SubscriptionModel(request);
            subscriberModel _subscriberModel = new IAS.Model.Subscriber.subscriberModel(request);
            if (oper.equalsIgnoreCase("view")) {

                url = "/jsp/subscription/viewsubscription.jsp";

            } else if (oper.equalsIgnoreCase("add")) {

                //save the subscription details sent from the UI
                String xml = _subscriptionModel.addSubscription();
                request.setAttribute("xml", xml);
                url = "/xmlserver";

            } else if (oper.equalsIgnoreCase("adddetail")) {

                String xml = null;
                int subscriptionID = Integer.parseInt(request.getParameter("subid"));
                int journalGroupID = Integer.parseInt(request.getParameter("journalGroupID"));
                int startYear = Integer.parseInt(request.getParameter("startYear"));
                int endYear = Integer.parseInt(request.getParameter("endYear"));
                int copies = Integer.parseInt(request.getParameter("copies"));
                float total = Float.parseFloat(request.getParameter("total"));
                int journalPriceGroupID = Integer.parseInt(request.getParameter("journalPriceGroupID"));

                //save the subscription details sent from the UI
                int[] res = _subscriptionModel.addNewSubscriptionDetail(
                        subscriptionID, journalGroupID, startYear, endYear,
                        copies, total, journalPriceGroupID);
                if(res.length == 1){
                    xml = util.convertStringToXML(String.valueOf(res[0]), "success");
                }
                request.setAttribute("xml", xml);
                url = "/xmlserver";

            } else if (action != null && action.equalsIgnoreCase("edit")) {

                //fill in the subscriber bean
                ResultSet rs = _subscriptionModel.getSubscriptionByID(Integer.parseInt(request.getParameter("id")));
                IAS.Bean.Subscription.SubscriptionFormBean _SubscriptionFormBean = null;
                while (rs.next()) {
                    BeanProcessor bProc = new BeanProcessor();
                    Class type = Class.forName("IAS.Bean.Subscription.SubscriptionFormBean");
                    _SubscriptionFormBean = (IAS.Bean.Subscription.SubscriptionFormBean) bProc.toBean(rs, type);

                }
                if (_subscriberModel.GetSubscriber() != null) {
                    request.setAttribute("SubscriptionFormBean", _SubscriptionFormBean);
                    url = "/jsp/subscription/editsubscription.jsp";
                }

            } else if(action.equalsIgnoreCase("subscriptioninfo")){
                // gets the subscription info given a subscription id
                ResultSet rs = _subscriptionModel.getSubscriptionByID(Integer.parseInt(request.getParameter("id")));
                String xml = util.convertResultSetToXML(rs);
                request.setAttribute("xml", xml);
                url = "/xmlserver";

            } else if (oper.equalsIgnoreCase("edit")){

                // we reach here if the existing subscription is being edited
                String xml = null;
                int startYear = Integer.parseInt(request.getParameter("startYear"));
                int endYear = Integer.parseInt(request.getParameter("endYear"));
                int copies = Integer.parseInt(request.getParameter("copies"));
                int subTypeID = Integer.parseInt(request.getParameter("subtypeid"));
                boolean active = Boolean.parseBoolean(request.getParameter("active"));
                int id = Integer.parseInt(request.getParameter("id"));
                if(_subscriptionModel.updateSubscriptionDetail(id, startYear, endYear, active, copies, subTypeID) != 1){
                    xml = util.convertStringToXML("Error updating subscription details", "error");
                }else{
                    xml = util.convertStringToXML("1","success");
                }
                request.setAttribute("xml", xml);
                url = "/xmlserver";


            } else if (oper.equalsIgnoreCase("del")) {

                _subscriptionModel.deleteSubscription();

            } else if (oper.equalsIgnoreCase("getsubscription")) {

                //get the subscription details for the subscriber and send it back to the UI as xml
                String xml = _subscriptionModel.getSubscription();
                request.setAttribute("xml", xml);
                url = "/xmlserver";

            } else if (oper.equalsIgnoreCase("subid")) {

                //get the subscription details for the subscriber and send it back to the UI as xml
                ResultSet rs = _subscriptionModel.getSubscriptionDetailBySubscriptionID(Integer.parseInt(request.getParameter("id")));
                String xml = util.convertResultSetToXML(rs);
                request.setAttribute("xml", xml);
                url = "/xmlserver";

            } else if (oper.equalsIgnoreCase("detail")) {

                String xml = _subscriptionModel.getSubscriptionDetails();
                request.setAttribute("xml", xml);
                url = "/xmlserver";

            } else if (oper.equalsIgnoreCase("getPrice")) {
                ResultSet rs = _subscriptionModel.getJournalPrice(
                        Integer.parseInt(request.getParameter("startyear")),
                        Integer.parseInt(request.getParameter("years")),
                        Integer.parseInt(request.getParameter("journalgroupid")),
                        Integer.parseInt(request.getParameter("subtypeid")));
                String xml = util.convertResultSetToXML(rs);
                request.setAttribute("xml", xml);
                url = "/xmlserver";
            } else if (oper.equalsIgnoreCase("getJournalGroupContents")) {
                int groupID = Integer.parseInt(request.getParameter("groupid"));
                ResultSet rs = _subscriptionModel.getJournalGroupContents(groupID);
                String xml = util.convertResultSetToXML(rs);
                request.setAttribute("xml", xml);
                url = "/xmlserver";

            } else if (oper.equalsIgnoreCase("getSubscriptionDetalsForInward")) {
                String inwardNumber = request.getParameter("inwardNumber");
                ResultSet rs = _subscriptionModel.getSubscriptionDetailsForInward(inwardNumber);
                String xml = null;
                if (rs != null) {
                    xml = util.convertResultSetToXML(rs);
                } else {
                    logger.error("Failed to get subscription details for id: " + request.getParameter("inwardNumber"));
                    xml = util.convertStringToXML("Failed to get subscription details", "error");
                }
                request.setAttribute("xml", xml);
                url = "/xmlserver";

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new javax.servlet.ServletException(e);

        } finally {
            RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
            if (rd != null && url != null) {
                rd.forward(request, response);
                //response.sendRedirect(request.getContextPath() + url);
            }
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}