<jwr:style src="/commonstyle.css" /><jwr:script src="/commonscript.js"/><%-- 
<c:set var="css"><spring:theme code="css"/></c:set>
<c:if test="${not empty css}"><link rel="stylesheet" href="<c:url value="/${css}"/>" type="text/css" /></c:if>
<c:set var="menucss"><spring:theme code="menucss"/></c:set>
<c:if test="${not empty menucss}"><link rel="stylesheet" href="<c:url value="/${menucss}"/>" type="text/css" /></c:if> --%>
<%--
<script src="<c:url value="/js/prototype/prototype.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/control.modal.2.2.3.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/lib.js"/>" type="text/javascript"></script>
<script src="<c:url value="/js/popup.js"/>" type="text/javascript"></script>
<script type="text/javascript" src="<c:url value="/js/swfobject.js"/>"></script>
--%>
<script type="text/javascript" src="<c:url value="/fckeditor/fckeditor.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/resetfckeditor.js"/>"></script>
<%--  
<script type="text/javascript" src="<c:url value="/js/menuExpandable.js"/>"></script>
--%>
<script type="text/javascript">
function confirmdelete(objectname, confirmId) {  
 var confirm = document.getElementById(confirmId);
 // var confirm = document.getElementById("confirm");
 var reallyDelete = window.confirm("Are you sure you want to delete '"+objectname+"'?"); 
 confirm.value = reallyDelete;
 return reallyDelete;
}

function confirmdeleteandredirect(message,objectname) {
  return window.confirm(message+" '"+objectname+"'?");
}


function doGetWithFormFields(formname, link)
{
	// append the serialized text fields to the href attribute
	// alert("Serialized: " + Form.serializeElements( $('mainform').getInputs('text')));
	// link.href += '&' + Form.serializeElements( $(formname).getInputs('text'));
	
	// special case: if there is a fckeditor we need to synchronize it
	// this is needed on the metadata page
	if (typeof FCKeditorAPI != "undefined") {
		var oFckEditor = FCKeditorAPI.GetInstance('description_area')
		if (oFckEditor) {
	    	oFckEditor.UpdateLinkedField();
	    }
    }
	if ($(formname) != null) {
		link.href += '&' + $(formname).serialize()
	}
	return true
}

function onWindowLoad()
{
	// initialize both the menu code and the sortable code (they both try to register window.onload
	// listeners - that does not really work
	initializeMenus();
	sorttable.init
}

Event.observe(window, 'load',
    	function() { 
        	onWindowLoad();
    	});
</script>