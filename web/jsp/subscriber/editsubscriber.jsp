<%--
    Document   : Edit Subscriber
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../templates/style.jsp"></jsp:include>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() + "/css/subscriber.css"%>"/>
        <title>Edit Subscriber</title>
        <script type="text/javascript" src="<%=request.getContextPath() + "/js/subscriber/subscriber.js"%>"></script>
        <script type="text/javascript" src="<%=request.getContextPath() + "/js/subscriber/createsubscriber.js"%>"></script>
        <script type="text/javascript" src="<%=request.getContextPath() + "/js/subscriber/editsubscriber.js"%>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/city.js"></script>
        <script>
            $(document).ready(function() {
                loadCities();
                //jdsAppend("CMasterData?md=city","city","city");
                jdsAppend("CMasterData?md=country","country","country");
                jdsAppend("CMasterData?md=state","state","state");
                jdsAppend("CMasterData?md=district","district","district");
                jdsAppend("CMasterData?md=agent","agentName","agent");
                makeEditSubscriberReadOnly();
                subtypeDescriptionAppend();
                subtypeCodeAppend();
            });
        </script>
        <script>
            $(document).ready(function() {
                //disable the add subscription button
                $("#btnAddSubscription").attr("disabled","disabled");
                jdsAppend("CMasterData?md=sub_type","subtype","subtype");
            });
        </script>
    </head>
    <body>

        <%@include file="../templates/layout.jsp" %>
        <div id="bodyContainer">
            <form method="post" action="<%=request.getContextPath() + "/subscriber"%>" name="subscriberForm" onsubmit = "return validateNewSubscriber()">
                <div class="MainDiv">
                    <fieldset class="MainFieldset">
                        <legend>Edit Subscriber</legend>
                        <%@include file="subscriber.jsp"%>
                    </fieldset>
                </div>
            </form>
        </div>
    </body>
</html>