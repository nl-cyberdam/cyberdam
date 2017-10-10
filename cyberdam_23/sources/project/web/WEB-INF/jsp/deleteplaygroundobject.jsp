<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_simple.jsp">
  <tiles:putAttribute name="title" ><spring:message code="delete.title"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
	<c:choose>
  	<c:when test="${empty gameManifests}">
    <spring:message code="areyousuredelete" arguments="${playgroundobject.name}"/>
	<form id="mainform" method="post">
	<input type="hidden" name="id" value="${playgroundobject.id}" >
    <input type="hidden" id="action"/>
	</form>	
	<table class="noborders"><tr>	
	<td><a href="#" onclick="document.getElementById('action').name='_delete';document.forms.mainform.submit()" class="button"><spring:message code="delete.ok"/></a></td>
	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="delete.cancel"/></a></td>
	</tr></table>
  	</c:when>
  	<c:otherwise>
    <div class="errorBox"><spring:message code="playgroundobject.in.use" /></div>
    <div class="manifestlist">
    <ul>
    <c:forEach items="${gameManifests}" var="manifest" >
    <li><c:out value="${manifest.name}" /></li>
    </c:forEach>
    </ul>
    </div>
    <form id="mainform" method="post">
		<input type="hidden" name="id" value="${playgroundobject.id}" >
	</form>	
	<a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.return"/></a>
  	</c:otherwise>
  	</c:choose>
  </tiles:putAttribute>
</tiles:insertTemplate>