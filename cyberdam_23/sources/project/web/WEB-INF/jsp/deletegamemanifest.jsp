<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_simple.jsp">
  <tiles:putAttribute name="title" ><spring:message code="delete.title"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
	<c:choose>
  	<c:when test="${empty error}">
    <spring:message code="areyousuredelete" arguments="${gamemanifest.name}"/>
	<form id="mainform" method="post">
	<input type="hidden" name="id" value="${gamemanifest.id}" >
    <input type="hidden" id="action"/>
	</form>	
	<table class="noborders"><tr>	
	<td><a href="#" onclick="document.getElementById('action').name='_delete';document.forms.mainform.submit()" class="button"><spring:message code="delete.ok"/></a></td>
	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="delete.cancel"/></a></td>
	</tr></table>
  	</c:when>
  	<c:otherwise>
    <div class="errorBox"><c:out value="${error}" /></div>
    <form id="mainform" method="post">
	<input type="hidden" name="id" value="${gamemanifest.id}" >
	</form>	
	<a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.return"/></a>
  	</c:otherwise>
  	</c:choose>
  </tiles:putAttribute>
</tiles:insertTemplate>