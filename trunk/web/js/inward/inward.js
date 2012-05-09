/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var selectedInward = 0;
var selectedSubscriberId = 0;
var selectedInwardRowIndex = -1;
var selectedInwardPurpose = "";

function validateSearchSubscriber(){

    var isValidSearch = false;
    if(!isEmptyValue($("#from").val())){
        isValidSearch = true;
    }
    else if(!isEmptyValue($("#country").val()) && !isEmptyValue($("#state").val())){
        isValidSearch = true;
    }
    else if(!isEmptyValue($("#country").val()) && !isEmptyValue($("#city").val())){
        isValidSearch = true;
    }

    if(!isValidSearch){
        alert("Please fill in Country along with City/State to search for subscriber");
        return;
    }else{
        var selectedSubscriberFromDialog = searchSubscriber(document.getElementById("country").value
            ,document.getElementById("state").value
            ,document.getElementById("city").value
            ,document.getElementById("from").value);

        if(!isEmptyValue(selectedSubscriberFromDialog) && selectedSubscriberFromDialog != 0){
            document.getElementById("subscriberId").value = selectedSubscriberFromDialog;
        }
    }
}

function searchSubscriber(country, state, city, subscriberName){
    var subscriber = new Object();
    windowParams = "dialogHeight:600px; dialogWidth:1000px; center:yes; resizeable:no; status:no; menubar:no;\n\
                    scrollbars:yes; toolbar: no;";
    subscriber.country = country;
    subscriber.city = city;
    subscriber.name = subscriberName;
    var selectedSubscriberFromDialog = openModalPopUp("jsp/subscriber/subscriberlist.jsp"
        , subscriber
        , windowParams);
    return selectedSubscriberFromDialog;
}

function clearSubscriber(){
    $("#subscriberId").val("");
}


function setInwardSubscriber(inwardId,subscriberId, purpose){
    selectedInward = inwardId;
    selectedSubscriberId = subscriberId || null;
    selectedInwardPurpose = purpose;
}

function validateNewInward(){

    var isInwardValid = true;

    // called from common.js
    //isInwardValid = checkMandatoryFields();

    // check the inward purpose field
    var InwardPurpose = document.getElementById("inwardPurpose").value.toLowerCase();

    if(checkMandatoryFields() == false){
        isInwardValid = false;
    }
    else if(InwardPurpose == 0){
        alert("Please select an Inward Purpose");
        isInwardValid = false;
    }
    else if(InwardPurpose == 'others' && isEmpty(document.getElementById("remarks"))){

        alert("Since you have created a miscellaneous inward, please fill in the remarks section");
        isInwardValid = false;
    }

    // if the payment mode is selected then ensure that the payment date and currency is also filled in
    else if(!isEmpty(document.getElementById("paymentMode")) && (isEmpty(document.getElementById("currency")) || isEmpty(document.getElementById("paymentDate")))){
        alert("Please select a currency and payment date");
        isInwardValid = false;
    }

    //if the payment mode is cheque/dd/mo ensure that the instrument number is filled in
    else if((!isEmpty(document.getElementById("paymentMode")) && document.getElementById("paymentMode").value != "Cash") && (isEmpty(document.getElementById("chqddNumber")))){
        alert("Please enter the " + document.getElementById("paymentMode").value + " number");
        isInwardValid = false;
    }

    // since selecting the subscriber is not mandatory, check if he is ok to proceed without
    // selecting subscriber. The inward should be alreay valid before we make this check
    if(isInwardValid && isEmptyValue($("#subscriberId").val())){
        if(confirm("Do you want to save the inward without selecting a subscriber?")){
            isInwardValid = true;
        }else{
            isInwardValid = false;
        }

    }

    return isInwardValid;
}

function subscriberlink(cellvalue, options, rowObject){
    rowid = options.rowId;
    var tagnames = ["subscriberId", "from", "city", "inwardNumber"];
    var tagvalues = new Array();

    for(i=0; i<tagnames.length; i++){
        var obj = rowObject.getElementsByTagName(tagnames[i])[0];
        if(obj.hasChildNodes()){
            tagvalues[i] = obj.childNodes[0].nodeValue;
        }
    }
    var subscriberID = tagvalues[0];
    var subscriberName = tagvalues[1];
    var city = tagvalues[2];
    var inwardid = tagvalues[3];
    if(isEmptyValue(subscriberID)){
        var link = "<a href=\"#\" onclick=" + "\"" + "selectSubscriber('" + city + "','" + subscriberName + "','" + inwardid + "')" + "\"" + ">Select Subscriber</a>";
        return link;
    }
    return "";
}

function selectInwardFormatter(cellvalue, options, rowObject){
    rowid = options.rowId;
    //console.log(rowObject);
    var tagnames = ["inwardPurposeID", "inwardNumber", "subscriberId"];
    var tagvalues = new Array();

    for(i=0; i<tagnames.length; i++){
        var obj = rowObject.getElementsByTagName(tagnames[i])[0];
        if(obj.hasChildNodes()){
            tagvalues[i] = obj.childNodes[0].nodeValue;
        }
    }
    var purposeId = tagvalues[0];
    var inwardId = tagvalues[1];
    var subscriberId = tagvalues[2];
    action = "<input type='radio' name='selectedInwardRadio'" + " value=" + "\"" + rowid + "\"" + " onclick=" + "\"" + "setInwardSubscriber('" + inwardId + "','" + subscriberId + "','" + purposeId + "')" + "\"" + "/>";
    return action;
}

function isInwardSelected(){
    var _JDSConstants = new JDSConstants();
    //var ids = jQuery("#inwardTable").jqGrid('getDataIDs');

    if(parseInt(selectedInward) == 0){
        alert("Please select an Inward");
        return false;

    }else if(selectedSubscriberId == null && selectedInwardPurpose == _JDSConstants.INWARD_PURPOSE_RENEW_SUBSCRIPTION){
        // if its not a new subscription then we need a subscriber id, search for the subscriber id
        city = jQuery("#inwardTable").jqGrid('getCell',selectedInward,'City').toString();
        subscriberName = jQuery("#inwardTable").jqGrid('getCell',selectedInward,'From').toString();
        selectSubscriber(city, subscriberName, selectedInward);
        return false;
    }else if(selectedSubscriberId == null && selectedInwardPurpose != _JDSConstants.INWARD_PURPOSE_NEW_SUBSCRIPTION){
        if(confirm("Do you want to continue without selecting subscriber ?") == false){
            return false;
        }
    }
    if(selectedSubscriberId=="undefined"){
        selectedSubscriberId = "";
    }
    $("#inwardNumber").val(selectedInward);
    $("#subscriberNumber").val(selectedSubscriberId);
    $("#purpose").val(selectedInwardPurpose);

//    document.processInwardForm.action = "inward?action=processinward&" +
//    "inwardNumber=" + selectedInward + "&" +
//    "subscriberNumber=" + selectedSubscriberId + "&" +
//    "purpose=" + selectedInwardPurpose
    return true;
}

function selectSubscriber(city, subscriberName, rowid){
    selectedSubscriberId = searchSubscriber("", "", city, subscriberName);
    jQuery("#inwardTable").jqGrid('setRowData', rowid, {
        'SubscriberId': selectedSubscriberId
    });
}