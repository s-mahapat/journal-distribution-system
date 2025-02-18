<%@page language="java" import="java.util.*" %>
<jsp:useBean class="IAS.Bean.Subscriber.subscriberFormBean" id="subscriberFormBean" scope="request"></jsp:useBean>
<script>
    $(document).ready(function(){
        var deactivationReason = "${subscriberFormBean.deactivationReason}";
        $("#deactivationReason").val(deactivationReason);
    });
</script>
    <fieldset class="subMainFieldSet">

        <div class="IASFormLeftDiv">

            <div class="IASFormFieldDiv">
                <span class="IASFormDivSpanLabel">
                    <label>City:</label>
                </span>

                <span class="IASFormDivSpanInputBox">
                    <select class="IASComboBoxMandatory" TABINDEX="3" name="city" id="city">
                        <option value="0" selected >Select</option>
                    <%
                        if (subscriberFormBean.getCity() != null && !subscriberFormBean.getCity().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getCity() + "\"" + " selected >" + subscriberFormBean.getCity() + "</option>");
                        }
                    %>
                </select>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>District:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <select class="IASComboBox" TABINDEX="4" name="district" id="district">
                    <option value="0" selected >Select</option>
                    <%
                        if (subscriberFormBean.getDistrict() != null && !subscriberFormBean.getDistrict().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getDistrict() + "\"" + " selected >" + subscriberFormBean.getDistrict() + "</option>");
                        }
                    %>

                </select>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>State:</label>
            </span>
            <span class="IASFormDivSpanInputBox">
                <select class="IASComboBoxMandatory" TABINDEX="5" name="state" id="state">
                    <option value="0" selected >Select</option>
                    <%
                        if (subscriberFormBean.getState() != null && !subscriberFormBean.getState().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getState() + "\"" + " selected >" + subscriberFormBean.getState() + "</option>");
                        }
                    %>
                </select>
                <%--<input class="IASTextBoxMandatory" TABINDEX="3" name="state" id="state" value="<jsp:getProperty name="inwardFormBean" property="state"/>"--%>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Country:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <select class="IASComboBoxMandatory" TABINDEX="6" name="country" id="country">
                    <option value="0" selected >Select</option>
                    <%
                        if (subscriberFormBean.getCountry() != null && !subscriberFormBean.getCountry().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getCountry() + "\"" + " selected >" + subscriberFormBean.getCountry() + "</option>");
                        }
                    %>
                </select>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Pin Code:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASTextBox" TABINDEX="7" type="text" name="pincode" id="pincode" value="${subscriberFormBean.pincodeAsText}"/>
            </span>
        </div>


        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label id="lblsubtype">Subscriber Type:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <select class="IASComboBoxSmallMandatory" TABINDEX="8" name="subtype" id="subtype"
                        onchange="subtypeAppend()">
                    <option value="0" selected >Select</option>
                    <%
                        if (subscriberFormBean.getSubtype() != null && !subscriberFormBean.getSubtype().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getSubtype() + "\"" + " selected >" + subscriberFormBean.getSubtype() + "</option>");
                        }
                    %>
                </select>
            </span>
        </div>
        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label id="lbldescription">Description:</label>
            </span>
            <span class="IASFormDivSpanInputBox">
                <select class="IASComboBoxMandatory" TABINDEX="9" name="subtypedesc" id="subtypedesc"
                        onchange="subtypeCodeAppend()">
                    <option value="0">Select</option>
                    <%
                        if (subscriberFormBean.getSubtypedesc() != null && !subscriberFormBean.getSubtypedesc().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getSubtypedesc() + "\"" + " selected >" + subscriberFormBean.getSubtypedesc() + "</option>");
                        }
                    %>
                </select>
                <label class="IASLabelOnly" id="subtypecode"></label>
            </span>
        </div>
    </div>

    <div class="IASFormRightDiv">

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label id="lblsubscriberCreationDate">Creation Date:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASDateTextBox" readonly type="text" name="subscriberCreationDate" id="subscriberCreationDate" value="${subscriberFormBean.subscriberCreationDate}"/>
            </span>
        </div>


        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Department:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASTextBoxWide" TABINDEX="10" type="text" name="department" id="department" value="${subscriberFormBean.department}"/>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Institution:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASTextBoxWide" TABINDEX="11" type="text" name="institution" id="institution" value="${subscriberFormBean.institution}"/>
            </span>
        </div>


        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Email:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASTextBoxWide" TABINDEX="12" type="email" name="email" id="email" onblur="validateEmail(this.id)" value="${subscriberFormBean.email}"/>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label id="lblphone">Phone:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASTextBoxWide" TABINDEX="13" type="text" name="phone" id="phone" value="${subscriberFormBean.phone}"/>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label id="lblfax">Fax:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASTextBoxWide" TABINDEX="14" type="text" name="fax" id="fax" value="${subscriberFormBean.fax}"/>
            </span>
        </div>


        <!--<div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Agent:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <select class="IASComboBoxWide" TABINDEX="13" name="agent" id="agent">
                    <option value="">Select</option>
        <%
            if (subscriberFormBean.getAgentName() != null && !subscriberFormBean.getAgentName().isEmpty()) {
                out.println("<option value=" + "\"" + subscriberFormBean.getAgentName() + "\"" + " selected >" + subscriberFormBean.getAgentName() + "</option>");
            }
        %>
    </select>
</span>
</div>-->

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label id="lbldeactivate">Deactivate</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input type="checkbox" class="IASCheckBox" disabled TABINDEX="14" name="deactive" id="deactive" onclick="deActivateSubscriber()"
                       <%
                           String isEnabled = "enabled";
                           if (subscriberFormBean.isDeactive() == true) {
                               out.println(" checked ");
                               isEnabled = "disabled";
                           }
                       %>
                       />
                <select class="IASComboBoxWide" id="deactivationReason" name="deactivationReason">
                    <option value="">Select</option>
                    <option value="Non payment of dues">Non payment of dues</option>
                    <option value="Deceased">Deceased</option>
                    <option value="Others">Others</option>
                </select>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label id="lbldeactivationDate">Deactivation Date:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASDateTextBox" readonly type="text" name="deactivationDate" id="deactivationDate" value="${subscriberFormBean.deactivationDate}"/>
            </span>
        </div>
    </div>
    <div class="actionBtnDiv" id="divsubscriberdtlsbtn">
        <button onclick="setActionValue('edit')" class="IASButton" TABINDEX="15" type="submit" value="Edit" id="btnEditSubscriber" name="btnSubmitAction"/>Edit</button>
    </div>

</fieldset>