<%--
    Document   : Create Agent
    Author     : Deepali
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../templates/style.jsp"></jsp:include>
        <link rel="stylesheet" type="text/css" href="css/masterdata/agent.css"/>
        <title>Create Agent</title>
        <script type="text/javascript" src="js/masterdata/addAgent.js"></script>
        <script type="text/javascript" src="js/masterdata/validateAgent.js"></script>

        <script>
            $(document).ready(function(){
                makeCreateReadOnly();
            });
        </script>
    </head>
    <body>

        <%@include file="../templates/layout.jsp" %>
        <div id="bodyContainer">
            <form method="post" action="<%=request.getContextPath() + "/agent"%>" name="agentForm" onsubmit="return validateAgent()">
                <div class="MainDiv">
                    <fieldset class="MainFieldset">
                        <legend>Create Agent</legend>
                        <%@include file="agent.jsp"%>
                        </fieldset>
                </div>
            </form>
        </div>
    </body>
</html>