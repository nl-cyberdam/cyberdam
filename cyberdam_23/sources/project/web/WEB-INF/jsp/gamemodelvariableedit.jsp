<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
<c:if test="${empty role}">
	<c:set var="title" value="${gameModel.name}"/>
	<c:set var="introduction" value="gamemodelvariableedit.introduction"/>	
</c:if>
<c:if test="${!empty role}">
	<c:set var="title" value="${role.name}"/>
	<c:set var="introduction" value="gamemodelrolevariableedit.introduction"/>	
</c:if>
<c:if test="${empty role}">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep /><cyb:crumb link="gamemodeldetail.htm?id=${gameModel.id}" mlkey="gamemodel.title"/> - <cyb:crumb mlkey="variable.title"/></tiles:putAttribute>
</c:if>
<c:if test="${!empty role}">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep /><cyb:crumb link="gamemodeldetail.htm?id=${gameModel.id}" mlkey="gamemodel.title"/><cyb:csep /><cyb:crumb link="gamemodelrolevariables.htm?id=${role.id}" mlkey="rolevariables.title"/> - <cyb:crumb mlkey="variableedit.title"/></tiles:putAttribute>
</c:if>
  <tiles:putAttribute name="title" ><spring:message code="variableedit.title"/>&nbsp;<spring:message code="${title}"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="${introduction}"/></tiles:putAttribute>
  <tiles:putAttribute name="content">

        <form:form id="mainform">
        <form:errors path="*" cssClass="errorBox" />
        <table class="edit">
            <tr><td class="first"><spring:message code="variable.name"/></td>
            <td><form:input path="name" /></td></tr>

            <tr><td><spring:message code="initialized"/></td>
            <td><input id="checkbox" type="checkbox" onclick="javascript:toggledisplay(this.checked)"></td></tr>

            <tr id="optional"><td><spring:message code="initialValue"/></td>
            <td><form:input id="value" path="initialValue" /></td></tr>
        </table>
	      <input type="submit" style="display:none"/>
	      <input type="hidden" id="action"/>
  	    </form:form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
<script type="text/javascript">
if (document.getElementById('value').value == "")
{
	document.getElementById('checkbox').checked = false;
	document.getElementById('optional').style.display = 'none';
}
else
{
	document.getElementById('checkbox').checked = true;
}
function toggledisplay(checked)
{
	document.getElementById('optional').style.display = (checked == true) ? '' :'none'
	if (checked == false)
	{
		document.getElementById('value').value = '';
	}
		
}
</script>
  </tiles:putAttribute>
</tiles:insertTemplate>
