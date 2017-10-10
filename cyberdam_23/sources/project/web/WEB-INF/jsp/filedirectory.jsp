<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="filedirectory.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="filedirectory.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
    <c:set var="lnk" ><c:url value="filedirectory.htm"><c:param name="participantId" value="${param.participantId}" /></c:url></c:set>
    <div><h2><spring:message code="Files" /></h2>
    <cyb:sessionlink status="${participant.gameSession.status}"  href="sessionfileuploadactivity.htm?participantId=${participant.id}" styleClass="button" ><spring:message code="link.fileupload" /></cyb:sessionlink>
    <form action="" method="POST" >
    <table>
    <tr><th><cyb:sortlink lnk="${lnk}" sort="name"><spring:message code="file.name" /></cyb:sortlink></th>
       <th><cyb:sortlink lnk="${lnk}" sort="stepOfPlay"><spring:message code="stepofplay" /></cyb:sortlink></th>
       <th><cyb:sortlink lnk="${lnk}" sort="created"><spring:message code="datetime" /></cyb:sortlink></th></tr>
    <c:if test="${fileslistholder.nrOfElements == 0}">
    	<tr><td colspan="3" ><spring:message code="noitemsfound.files" /></td></tr>
    </c:if>
    <c:forEach items="${fileslistholder.pageList}" var="sessionresource" >
    <tr><td><c:out value="${sessionresource.name}" /></td><td><c:out value="${sessionresource.stepOfPlay}" /></td><td><fmt:formatDate value="${sessionresource.created}" pattern="dd MMM yyyy HH:mm" /></td>
    <td class="icons"><c:url value="sessionresource" var="openlink"><c:param name="id" value="${sessionresource.id}" /></c:url>
    <c:url value="sessiondeletefile.htm" var="deletelink"><c:param name="id" value="${sessionresource.id}" /><c:param name="participantId" value="${participant.id}" /></c:url>
    <c:url value="sessioneditfile.htm" var="editlink"><c:param name="id" value="${sessionresource.id}" /><c:param name="participantId" value="${participant.id}" /></c:url>
    <a href="${openlink}" title="<spring:message code="open"/>"><img src="themes/default/button_file.gif"/></a>
    <cyb:sessionlink status="${participant.gameSession.status}" href="${deletelink}" tooltip="delete"><img src="themes/default/button_delete.gif"/></cyb:sessionlink>
    <cyb:sessionlink status="${participant.gameSession.status}" href="${editlink}" tooltip="rename"><img src="themes/default/button_rename.gif"/></cyb:sessionlink>
    </td></tr>
    </c:forEach>
    </table>
    <cyb:pagernav pagedListHolder="${fileslistholder}" lnk="${lnk}"/>
	<cyb:pager pagedListHolder="${fileslistholder}" />
    </form>
    <c:url value="filedirectorytrash.htm" var="trashlink"><c:param name="participantId" value="${participant.id}" /></c:url>
    <a class="button" href="${trashlink}"><spring:message code="filedirectorytrash" /></a>
    </div>
  </tiles:putAttribute>
</tiles:insertTemplate>
