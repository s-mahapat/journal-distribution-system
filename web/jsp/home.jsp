<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="templates/style.jsp"></jsp:include>
        <title>Home</title>
        <script>
            <%
                String usrRole = null;
                try {
                    if (session != null) {
                        IAS.Bean.User.LoggedInUserBean _userBean = (IAS.Bean.User.LoggedInUserBean) session.getAttribute("userBean");
                        usrRole = _userBean.getUserRole();
                    }
                } catch (NullPointerException e) {
                    usrRole = null;
                }
            %>
        </script>
    </head>
    <body>
        <%@include file="templates/layout.jsp" %>
        <div id="bodyContainer">
            <!--<img id="watermark" src="<%=request.getContextPath() + "/images/watermark.png"%>"/>-->

            <%if (usrRole != null && usrRole.equalsIgnoreCase("monochrome")) {
                    out.println("<img id=\"watermark\"" + " src=\"" + request.getContextPath() + "/images/monochrome_watermark.png\"/>");
                } else {
                    out.println("<img id=\"watermark\"" + " src=\"" + request.getContextPath() + "/images/watermark.png\"/>");
                }
            %>
        </div>
    </body>
</html>