<%@page import="nl.cyberdam.domain.Participant"%>
<%@page import="nl.cyberdam.domain.Activity"%>
<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="participantreport.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="participantreport.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
    <c:url var="lnk" value="participantreport.htm">
        <c:param name="participantId" value="${param.participantId}"/>
    </c:url>

        <form action="" method="POST">
            <table class="usertable">
                <thead>
                    <tr class="title">
						<th><cyb:sortlink lnk="${lnk}" sort="date"><spring:message code="date"/></cyb:sortlink></th>
						<th><cyb:sortlink lnk="${lnk}" sort="stepOfPlay"><spring:message code="stepOfPlay"/></cyb:sortlink></th>
						<th><cyb:sortlink lnk="${lnk}" sort="activity.name"><spring:message code="activity.name"/></cyb:sortlink></th>
						<th><cyb:sortlink lnk="${lnk}" sort="type"><spring:message code="type"/></cyb:sortlink></th>
						<th><spring:message code="activity.attachments" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${participantreportlist.nrOfElements == 0}">
                    <tr><td colspan="5"><spring:message code="noitemsfound.sessionreport" /></td></tr>
                    </c:if>
                    <c:forEach items="${participantreportlist.pageList}" var="sessionreport">
						<tr>
							<td><c:out value="${sessionreport.date}"/></td>
							<td><c:out value="${sessionreport.stepOfPlay}"/></td>
							<td><c:out value="${sessionreport.activity.name}"/></td>
							<td><spring:message code="activitytype.${sessionreport.type}"/></td>
							<td>
							<c:forEach items="${sessionreport.activity.sortedAttachments}" var="attachment">
								<c:url value="/resource" var="resourceurl" >
								<c:param name="resourceId" value="${attachment.id}" />
								</c:url>
								<a href="${resourceurl}" target="_blank"> <c:out value="${attachment.name}" /></a>
		                    </c:forEach>
	            			</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <cyb:pagernav pagedListHolder="${participantreportlist}" lnk="${lnk}"/>
        <cyb:pager pagedListHolder="${participantreportlist}" />
        </form>
    <a href="activities.htm?participantId=${participant.id}" class="button"><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>
