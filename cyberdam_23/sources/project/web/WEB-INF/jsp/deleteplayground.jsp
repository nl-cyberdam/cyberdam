<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_simple.jsp">
  <tiles:putAttribute name="title" ><spring:message code="delete.title"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
	<c:choose>
    <c:when test="${!empty error}">
        <div class="errorBox"><spring:message code="roletoplaygroundmapping.inuse" /></div>
        <form id="errorform" method="post">
        </form>
		<a href="#" onclick="document.forms.errorform.submit()" class="button"><spring:message code="button.return"/></a>
    </c:when>
  	<c:when test="${empty gameManifests}">
    <spring:message code="areyousuredelete" arguments="${playground.name}"/>
	<form id="mainform" method="post">
	<input type="hidden" name="id" value="${playground.id}" >
	<c:forEach var="item" items="${resultStrings}" >
			 <li><c:out value="${item}" /></li>
	</c:forEach>
    <input type="hidden" id="action"/>
	</form>	
	<table class="noborders"><tr>	
	<td><a href="#" onclick="document.getElementById('action').name='_delete';document.forms.mainform.submit()" class="button"><spring:message code="delete.ok"/></a></td>
	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="delete.cancel"/></a></td>
	</tr></table>
  	</c:when>
  	<c:otherwise>
    <div class="errorBox"><spring:message code="playground.inuse" /></div>
    <div class="manifestlist">
    <ul>
    <c:forEach items="${gameManifests}" var="gameManifests" >
    <li><c:out value="${gameManifests.name}" /></li>
    </c:forEach>
    </ul>
    </div>
    <form id="mainform" method="post">
		<input type="hidden" name="id" value="${playground.id}" >
	</form>	
	<a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.return"/></a>
  	</c:otherwise>
  	</c:choose>
  </tiles:putAttribute>
</tiles:insertTemplate>