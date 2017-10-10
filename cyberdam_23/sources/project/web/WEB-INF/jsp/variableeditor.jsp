<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep /><cyb:crumb link="variable.htm" mlkey="variable.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="variableedit.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="variableedit.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">

        <form:form id="mainform">
        <form:errors path="*" cssClass="errorBox" />
        <table class="edit">
            <tr><td class="first"><form:label path="name"><spring:message code="name"/></form:label></td>
            <td><form:input path="name" /></td></tr>

            <tr><td><spring:message code="variable.initialized"/></td>
            <td><input id="checkbox" type="checkbox" onclick="javascript:toggledisplay(this.checked)"></td></tr>

            <tr id="optional"><td><form:label path="initialValue"><spring:message code="variable.initialValue"/></form:label></td>
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