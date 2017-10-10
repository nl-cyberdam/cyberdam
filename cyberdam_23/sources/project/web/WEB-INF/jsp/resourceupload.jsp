<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <c:if test="${empty param.target}">
    <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep /><cyb:crumb link="gamemodeldetail.htm?id=${param.id}" mlkey="gamemodel.title"/><cyb:csep/><cyb:crumb mlkey="resourceupload.title"/></tiles:putAttribute>
  </c:if>
  <c:if test="${!empty param.target}">
    <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="playgroundlistpage.htm" mlkey="playgroundlistpage.title"/><cyb:csep/><cyb:crumb link="playgrounddetail.htm?id=${playgroundId}" mlkey="playgrounddetail.title"/><cyb:csep/><cyb:crumb link="playgroundobjecteditor.htm?id=${param.id}" mlkey="playgroundobjecteditor.title"/><cyb:csep/><cyb:crumb mlkey="resourceupload.title"/></tiles:putAttribute>
  </c:if>
  <tiles:putAttribute name="title" ><spring:message code="resourceupload.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="resourceupload.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
  		<c:if test="${! empty errors}" >
  		<span id="*.errors" class="errorBox">
  		<c:forEach items="${errors}" var="error" >
  		<spring:message code="${error.code}" /><br />
  		</c:forEach>
  		</span>
  		</c:if>
        <form id="mainform" method="post" action="resourceupload.htm" enctype="multipart/form-data">
        	<input type="hidden" name="id" value="${param.id}" />
        	<input type="hidden" name="target" value="${param.target}" />
            <input type="hidden" name="redirectTo" value="${param.redirectTo}" />
            <input type="file" name="file"/>
            <input type="hidden" id="action"/>
        </form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.upload"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>
