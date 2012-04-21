/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IAS.Model;

import IAS.Class.Database;
import IAS.Class.Queries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author Shailendra Mahapatra
 */
public class JDSModel {

    protected HttpServletRequest request = null;
    protected Connection conn = null;
    protected Database db = null;
    protected HttpSession session = null;

    public JDSModel(HttpServletRequest _request) throws SQLException {

        this.request = _request;
        this.session = _request.getSession(false); //do not create the session if it does not exist
        if (this.session == null) {
            throw (new SQLException("Session does not exist.Database connection not found in the session"));
        }
        this.db = (Database) session.getAttribute("db_connection");
        this.conn = db.getConnection();
    }

    public JDSModel() {
    }

    public void FillBean(HttpServletRequest request, Object _bean) throws IllegalAccessException, java.lang.reflect.InvocationTargetException {
        HashMap paramMap = new HashMap();
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            paramMap.put(name, request.getParameterValues(name));
        }
        BeanUtils.populate(_bean, paramMap);
    }

    public int CompleteInward(int inwardID) throws SQLException {
        //Update inward with completed flag once the transaction is completed
        String sql = Queries.getQuery("update_inward_complete_flag");
        PreparedStatement st = conn.prepareStatement(sql);
        st = conn.prepareStatement(sql);
        st.setInt(1, inwardID);
        if (db.executeUpdatePreparedStatement(st) == 1) {
            session.setAttribute("inwardUnderProcess", null);
            return (1);
        }
        return (0);
    }
}
