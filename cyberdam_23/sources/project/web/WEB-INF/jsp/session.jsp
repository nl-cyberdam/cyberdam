<%@page import="nl.cyberdam.domain.Participant"%>
<%@page import="nl.cyberdam.domain.Activity"%>
<%@ include file="includes.jsp"%>
<tiles:insertTemplate template="layout_menu.jsp">
	<tiles:putAttribute name="breadcrumb">
		<cyb:crumb link="index.htm" mlkey="homepage.title" />
		<cyb:csep />
		<cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title" />
		<cyb:csep />
		<cyb:crumb mlkey="session.title" />
	</tiles:putAttribute>
	<tiles:putAttribute name="title">
		<spring:message code="session.title" />
		<c:out value="${participant.gameSession.name}" />
	</tiles:putAttribute>
	<tiles:putAttribute name="introduction">
		<spring:message code="session.introduction" />
	</tiles:putAttribute>
	<tiles:putAttribute name="menu">
		<%@ include file="session_leftnav.jsp"%>
	</tiles:putAttribute>
	<tiles:putAttribute name="content">
		${headerText}
		<c:if test="${activityDisabled}">
			<div class="error"><spring:message code="activity.disabled" /></div>
		</c:if>
		<c:if test="${isClassicLayout}">
			<div>
			<h2><spring:message code="message.receivedmessages" /></h2>
			<cyb:sessionlink status="${participant.gameSession.status}"
				href="sendmessage.htm?participantId=${participant.id}"
				styleClass="button">
				<spring:message code="sendmessage" />
			</cyb:sessionlink>
			<table>
				<tr>
					<th><spring:message code="message.from" /></th>
					<th><spring:message code="message.to" /></th>
					<th><spring:message code="message.subject" /></th>
					<th><spring:message code="stepofplay" /></th>
					<th><spring:message code="datetime" /></th>
					<th><spring:message code="message.attachments" /></th>
				</tr>
				<c:if test="${inboxmessagesListHolder.nrOfElements == 0}">
					<tr>
						<td colspan="6"><spring:message code="noitemsfound.messages" /></td>
					</tr>
				</c:if>
				<c:forEach items="${inboxmessagesListHolder.pageList}" var="message"
					end="${systemMaxMail}">
					<tr>
						<td><c:out
							value="${message.sender.roleAndPlayground.role.name}" /></td>
						<td><c:forEach items="${message.recipients}" var="recipient">
							<c:out value="${recipient.roleAndPlayground.role.name}" />
							<br />
						</c:forEach></td>
						<td><c:out value="${message.subject}" /></td>
						<td><c:out value="${message.stepOfPlay}" /></td>
						<td><fmt:formatDate value="${message.sentDate}"
							pattern="dd MMM yyyy HH:mm" /></td>
						<td><c:out value="${fn:length(message.attachments)}" /></td>
						<td><c:url value="message.htm" var="viewmessagelink">
							<c:param name="participantId" value="${participant.id}" />
							<c:param name="id" value="${message.id}" />
						</c:url><a href="${viewmessagelink}" title="<spring:message code="view"/>"><img
							src="themes/default/button_view.gif" /></a> <c:url
							value="sessiondeletemessage.htm" var="deletemessagelink">
							<c:param name="participantId" value="${participant.id}" />
							<c:param name="id" value="${message.id}" />
							<c:param name="box" value="inbox" />
						</c:url><cyb:sessionlink status="${participant.gameSession.status}"
							href="${deletemessagelink}" tooltip="delete">
							<img src="themes/default/button_delete.gif" />
						</cyb:sessionlink></td>
					</tr>
				</c:forEach>
			</table>
			</div>
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
						<td colspan="4"><spring:message
							code="noitemsfound.activities" /></td>
					</tr>
				</c:if>
				<c:forEach items="${enabledActivities}" var="activity"
					end="${systemMaxActivities}">
					<%
						// XXX implement cleaner solution later
										Activity activity = (Activity) pageContext
												.getAttribute("activity");
										Participant participant = (Participant) pageContext
												.findAttribute("participant");

										// check if the activity has been completed
										boolean completed = participant.checkActivityCompleted(activity);
										boolean enabled = participant.checkActivityEnabled (activity);
										boolean selectable = enabled || !completed;
										pageContext.setAttribute("activitySelectable",
												new Boolean (selectable));
										pageContext.setAttribute("activityCompleted",
												new Boolean (completed));
					%>
					<c:if test="${!empty activity.id}">
						<tr>
							<td><spring:message code="activitytype.${activity.type}" /></td>
							<td><c:out value="${activity.name}" /></td>

							<td><c:forEach items="${activity.sortedAttachments}"
								var="attachment" varStatus="varStatus">
								<c:url value="/resource" var="resourceurl">
									<c:param name="resourceId" value="${attachment.id}" />
								</c:url>
								<%--<a href="${resourceurl}">--%>
								<c:out value="${attachment.name}" />
								<%--</a>--%>
							</c:forEach></td>

							<td><spring:message code="completed.${activityCompleted}" />
							</td>
							<td><c:if test="${activitySelectable}">
								<c:if test="${activity.type eq 'message'}">
									<cyb:sessionlink status="${participant.gameSession.status}"
										href="sessionmessageactivity.htm?participantId=${participant.id}&activityId=${activity.id}">
										<spring:message code="perform.activity" />
									</cyb:sessionlink>
								</c:if>
								<c:if test="${activity.type eq 'progress'}">
									<cyb:sessionlink status="${participant.gameSession.status}"
										href="sessionprogressactivity.htm?participantId=${participant.id}&activityId=${activity.id}">
										<spring:message code="perform.activity" />
									</cyb:sessionlink>
								</c:if>
								<c:if test="${activity.type eq 'fileupload'}">
									<cyb:sessionlink status="${participant.gameSession.status}"
										href="sessionfileuploadactivity.htm?participantId=${participant.id}&activityId=${activity.id}">
										<spring:message code="perform.activity" />
									</cyb:sessionlink>
								</c:if>
								<c:if test="${activity.type eq 'form'}">
									<cyb:sessionlink status="${participant.gameSession.status}"
										href="sessionformactivity.htm?participantId=${participant.id}&activityId=${activity.id}">
										<spring:message code="perform.activity" />
									</cyb:sessionlink>
								</c:if>
								<c:if test="${activity.type eq 'event'}">
									<cyb:sessionlink status="${participant.gameSession.status}"
										href="sessioneventactivity.htm?participantId=${participant.id}&activityId=${activity.id}">
										<spring:message code="perform.activity" />
									</cyb:sessionlink>
								</c:if>
							</c:if></td>
						</tr>
					</c:if>
				</c:forEach>
			</table>
			</div>
			<div>
			<h2><spring:message code="Files" /></h2>
			<cyb:sessionlink status="${participant.gameSession.status}"
				href="sessionfileuploadactivity.htm?participantId=${participant.id}"
				styleClass="button">
				<spring:message code="link.fileupload" />
			</cyb:sessionlink>
			<table>
				<tr>
					<th><spring:message code="file.name" /></th>
					<th><spring:message code="stepofplay" /></th>
					<th><spring:message code="datetime" /></th>
				</tr>
				<c:if test="${empty participant.sessionResourceBox.resources}">
					<tr>
						<td colspan="3"><spring:message code="noitemsfound.files" /></td>
					</tr>
				</c:if>
				<c:forEach items="${participant.sessionResourceBox.sortedResources}"
					var="sessionresource" end="${systemMaxFiles}">
					<tr>
						<td><c:out value="${sessionresource.name}" /></td>
						<td><c:out value="${sessionresource.stepOfPlay}" /></td>
						<td><fmt:formatDate value="${sessionresource.created}"
							pattern="dd MMM yyyy HH:mm" /></td>
						<td><c:url value="sessionresource" var="openlink">
							<c:param name="id" value="${sessionresource.id}" />
						</c:url> <c:url value="sessiondeletefile.htm" var="deletelink">
							<c:param name="id" value="${sessionresource.id}" />
							<c:param name="participantId" value="${participant.id}" />
						</c:url> <a href="${openlink}" title="<spring:message code="open"/>"><img
							src="themes/default/button_file.gif" /></a> <cyb:sessionlink
							status="${participant.gameSession.status}" href="${deletelink}"
							tooltip="delete">
							<img src="themes/default/button_delete.gif" />
						</cyb:sessionlink></td>
					</tr>
				</c:forEach>
			</table>
			</div>
		</c:if>
	</tiles:putAttribute>
</tiles:insertTemplate>