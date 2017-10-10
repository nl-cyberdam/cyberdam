<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_simple.jsp">
  <tiles:putAttribute name="title" ><spring:message code="deleteuser.title"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
    <spring:message code="areyousuredelete" arguments="${user.username}"/>
	<form id="mainform" method="post">
	<input type="hidden" name="id" value="${user.id}" >
    <input type="hidden" id="action"/>
	</form>	
	<table class="noborders"><tr>	
	<td><a href="#" onclick="document.getElementById('action').name='_delete';document.forms.mainform.submit()" class="button"><spring:message code="delete.ok"/></a></td>
	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="delete.cancel"/></a></td>
	</tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>