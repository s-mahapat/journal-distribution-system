
<jsp:useBean class="IAS.Bean.masterdata.cityFormBean" id="cityFormBean" scope="request"></jsp:useBean>
<%-----------------------------------------------------------------------------------------------------%>
<%-- City Basic data Field Set --%>
<%-----------------------------------------------------------------------------------------------------%>

<fieldset class="subMainFieldSet">
    <legend>City</legend>
    <div class="IASFormFieldDiv">
        <div class="IASFormLeftDiv">
            <div class="IASFormFieldDiv">
                <span class="IASFormDivSpanLabel">
                    <label>City Id:</label>
                </span>
                <span class="IASFormDivSpanInputBox">
                    <input class="IASTextBoxMandatory" TABINDEX="1" type="text" name="cityId" id="cityId" value="<jsp:getProperty name="cityFormBean" property="cityId"/>"/>
                </span>
            </div>
            <div class="IASFormFieldDiv">
                <span class="IASFormDivSpanLabel">
                    <label>City:</label>
                </span>
                <span class="IASFormDivSpanInputBox">
                    <input class="IASTextBoxMandatory" TABINDEX="1" type="text" name="city" id="city" value="<jsp:getProperty name="cityFormBean" property="city"/>"/>
                </span>
            </div>
        </div>
     </div>
</fieldset>
<%-----------------------------------------------------------------------------------------------------%>
<%-- City Basic Action Field Set --%>
<%-----------------------------------------------------------------------------------------------------%>

<fieldset class="subMainFieldSet">
    <div class="IASFormFieldDiv">
        <input type="hidden" name="action" id="action"/>
        <div id="saveBtnDiv">
            <input onclick="setActionValue('save')"  class="IASButton" TABINDEX="2" type="submit" value="save" id="btnSave" name="btnSubmitAction"/>
        </div>
        <div id="editBtnDiv">
            <input onclick="setActionValue('edit')" class="IASButton" TABINDEX="2" type="submit" value="edit" id="btnEdit" name="btnSubmitAction"/>
        </div>
    </div>
</fieldset>

