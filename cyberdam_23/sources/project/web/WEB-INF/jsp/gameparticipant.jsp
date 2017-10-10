<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb mlkey="gameparticipant.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gameparticipant.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gameparticipant.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
    <form id="mainform" action="" method="POST">
    <%@ include file="textfilter.jsp" %>
	<table>
	<tr><th><cyb:sortlink lnk="${lnk}" sort="gameSession.name"><spring:message code="session.name" /></cyb:sortlink></th>
	    <th><cyb:sortlink lnk="${lnk}" sort="gameSession.status"><spring:message code="status" /></cyb:sortlink></th>
	    <th><cyb:sortlink lnk="${lnk}" sort="gameSession.runningStarted"><spring:message code="started" /></cyb:sortlink></th>
	    <th><spring:message code="role" /> (<spring:message code="character" />)</th>
	    <th><spring:message code="stepofplay" />, <spring:message code="activities" /> (<spring:message code="uncompleted" />/<spring:message code="total" />)</th>
	    <th><cyb:sortlink lnk="${lnk}" sort="gameSession.owner.username"><spring:message code="gamesession.owner" /></cyb:sortlink></th></tr>
	<c:if test="${empty gameparticipantlist.pageList}">
      	<tr><td colspan="6" ><spring:message code="noitemsfound.participants" /></td></tr>
    </c:if>
	<c:forEach items="${gameparticipantlist.pageList}" var="participant">
	<tr><td><a href="session.htm?participantId=${participant.id}" title="<spring:message code="sessionhome"/>"><c:out value="${participant.gameSession.name}" /></a></td>
	    <td><spring:message code="status.${participant.gameSession.status}" /></td>
	    <td><fmt:formatDate value="${participant.gameSession.runningStarted}" pattern="dd MMM yyyy HH:mm" /></td>
	    <td><c:out value="${participant.roleAndPlayground}" /></td>
	    <td><c:out value="${participant.gameSession.currentStepOfPlay}" />, (<c:out value="${fn:length(participant.activitiesForCurrentStep) - fn:length(participant.completedActivities)}" />/<c:out value="${fn:length(participant.activitiesForCurrentStep)}" />)</td>
	    <td><cyb:displayuser user="${participant.gameSession.owner}"/></td>
	    <td class="icons"><a href="session.htm?participantId=${participant.id}" title="<spring:message code="sessionhome"/>"><img src="themes/default/button_home.gif"/></a></td></tr>
	</c:forEach>
	</table>
	<cyb:pagernav pagedListHolder="${gameparticipantlist}" lnk="${lnk}"/>
	<cyb:pager pagedListHolder="${gameparticipantlist}" />
    </form>
	<br/><hr /><br/>
	 <a class="back button" name="link_back" href="<c:url value="index.htm"/>" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>
