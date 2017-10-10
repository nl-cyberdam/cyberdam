<%@page import="nl.cyberdam.domain.Participant"%>
<%@page import="nl.cyberdam.domain.Activity"%>
<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="activities.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="activities.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
		<div>
		<h2><spring:message code="Activities" /></h2>
		<table>
			<tr>
				<th><spring:message code="activity.type" /></th>
				<th><spring:message code="activity.name" /></th>
				<th><spring:message code="activity.attachments" /></th>
				<th><spring:message code="activity.completed" /></th>
			</tr>
			<c:if test="${empty enabledActivities}">
				<tr>
					<td colspan="4"><spring:message code="noitemsfound.activities" /></td>
				</tr>
			</c:if>
			<c:forEach items="${enabledActivities}"
				var="activity">
				<%
					// XXX implement cleaner solution later
								Activity activity = (Activity) pageContext.getAttribute("activity");
								Participant participant = (Participant) pageContext
										.findAttribute("participant");

					// check if the activity has been completed
					boolean completed = participant.checkActivityCompleted(activity);
					boolean enabled = participant.checkActivityEnabled(activity);
					boolean selectable = enabled || !completed;
					pageContext.setAttribute("activitySelectable", new Boolean(selectable));
					pageContext.setAttribute("activityCompleted", new Boolean(completed));
				%>
				<c:if test="${!empty activity.id}">
				<tr>
					<td><spring:message code="activitytype.${activity.type}" /></td>
					<td><c:out value="${activity.name}" /></td>
					
                <td>
				<c:forEach items="${activity.sortedAttachments}" var="attachment" varStatus="varStatus" >
				<c:url value="/resource" var="resourceurl" >
				<c:param name="resourceId" value="${attachment.id}" />
				</c:url>
				<a href="${resourceurl}" target="_blank">
				<c:out value="${attachment.name}" />
				</a></c:forEach>
				</td>
					<td><spring:message code="completed.${activityCompleted}" />
					</td>
					<td><c:if test="${activitySelectable}">
						<c:if test="${activity.type eq 'message'}">
							<cyb:sessionlink status="${participant.gameSession.status}" 
								href="sessionmessageactivity.htm?participantId=${participant.id}&activityId=${activity.id}"><spring:message code="perform.activity" /></cyb:sessionlink>
						</c:if>
						<c:if test="${activity.type eq 'progress'}">
							<cyb:sessionlink status="${participant.gameSession.status}" 
								href="sessionprogressactivity.htm?participantId=${participant.id}&activityId=${activity.id}"><spring:message code="perform.activity" /></cyb:sessionlink>
						</c:if>
						<c:if test="${activity.type eq 'fileupload'}">
							<cyb:sessionlink status="${participant.gameSession.status}" 
								href="sessionfileuploadactivity.htm?participantId=${participant.id}&activityId=${activity.id}"><spring:message code="perform.activity" /></cyb:sessionlink>
						</c:if>
						<c:if test="${activity.type eq 'form'}">
							<cyb:sessionlink status="${participant.gameSession.status}" 
								href="sessionformactivity.htm?participantId=${participant.id}&activityId=${activity.id}"><spring:message code="perform.activity" /></cyb:sessionlink>
						</c:if>
						<c:if test="${activity.type eq 'event'}">
							<cyb:sessionlink
								status="${participant.gameSession.status}"
								href="sessioneventactivity.htm?participantId=${participant.id}&activityId=${activity.id}">
								<spring:message code="perform.activity" />
							</cyb:sessionlink>
						</c:if>
					</c:if></td>
				</tr>
				</c:if>
			</c:forEach>
		</table>
		<br/>
		<c:url value="participantreport.htm" var="sessionreport"><c:param name="participantId" value="${participant.id}"/></c:url>
		<a href="${sessionreport}" class="button"><spring:message code="link.activityreport"/></a>
		</div>
	</tiles:putAttribute>
</tiles:insertTemplate>
