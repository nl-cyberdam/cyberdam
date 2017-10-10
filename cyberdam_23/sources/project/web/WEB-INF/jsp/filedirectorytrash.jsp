<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="filedirectorytrash.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="filedirectorytrash.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
    <div><h2><spring:message code="filedirectorytrash" /></h2>
    <table>
    <tr><th><spring:message code="file.name" /></th><th><spring:message code="stepofplay" /></th><th><spring:message code="datetime" /></th></tr>
    <c:if test="${empty participant.sessionResourceTrash.resources}">
    	<tr><td colspan="3" ><spring:message code="noitemsfound.files" /></td></tr>
    </c:if>
    <c:forEach items="${participant.sessionResourceTrash.resources}" var="sessionresource" >
    <tr><td><c:out value="${sessionresource.name}" /></td><td><c:out value="${sessionresource.stepOfPlay}" /></td><td><fmt:formatDate value="${sessionresource.created}" pattern="dd MMM yyyy HH:mm" /></td>
    <td class="icons"><c:url value="sessionresource" var="openlink"><c:param name="id" value="${sessionresource.id}" /></c:url>
    <c:url value="sessiondeletefile.htm" var="undeletelink"><c:param name="id" value="${sessionresource.id}" /><c:param name="participantId" value="${participant.id}" /><c:param name="restore" value="true" /></c:url>
    <c:url value="sessioneditfile.htm" var="editlink"><c:param name="id" value="${sessionresource.id}" /><c:param name="participantId" value="${participant.id}" /></c:url>
    <a href="${openlink}" title="<spring:message code="open"/>"><img src="themes/default/button_file.gif"/></a>
    <cyb:sessionlink status="${participant.gameSession.status}"  href="${editlink}" tooltip="rename"><img src="themes/default/button_rename.gif"/></cyb:sessionlink>
    <cyb:sessionlink status="${participant.gameSession.status}"  href="${undeletelink}" tooltip="undelete"><img src="themes/default/button_undo.gif"/></cyb:sessionlink>
    </td></tr>
    </c:forEach>
    </table>
    </div>
  </tiles:putAttribute>
</tiles:insertTemplate>
