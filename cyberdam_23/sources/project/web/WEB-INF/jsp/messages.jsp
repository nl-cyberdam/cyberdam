<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="messages.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="messages.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
                <div>
                <h2><spring:message code="message.receivedmessages" /></h2>
                <table>
                <tr><th><spring:message code="message.from" /></th><th><spring:message code="message.to" /></th><th><spring:message code="message.subject" /></th><th><spring:message code="stepofplay" /></th><th><spring:message code="datetime" /></th><th><spring:message code="message.attachments" /></th></tr>
                <c:if test="${inboxmessagesListHolder.nrOfElements == 0}">
                	<tr><td colspan="6" ><spring:message code="noitemsfound.messages" /></td></tr>
                </c:if>
                <c:forEach items="${inboxmessagesListHolder.pageList}" var="message" >
                <tr><td><c:out value="${message.sender.roleAndPlayground.role.name}" /></td><td><c:forEach items="${message.recipients}" var="recipient"><c:out value="${recipient.roleAndPlayground.role.name}" /><br /></c:forEach>                 
                </td><td><c:out value="${message.subject}" /></td><td><c:out value="${message.stepOfPlay}" /></td><td><fmt:formatDate value="${message.sentDate}" pattern="dd MMM yyyy HH:mm" /></td><td><c:out value="${fn:length(message.attachments)}"/></td>
                <td class="icons"><c:url value="message.htm" var="viewmessagelink"><c:param name="participantId" value="${participant.id}"/><c:param name="id" value="${message.id}" /><c:param name="box" value="inbox" /></c:url><a href="${viewmessagelink}" title="<spring:message code="view"/>"><img src="themes/default/button_view.gif"/></a>
                <c:url value="sessiondeletemessage.htm" var="deletemessagelink"><c:param name="participantId" value="${participant.id}"/><c:param name="id" value="${message.id}" /><c:param name="box" value="inbox" /></c:url><cyb:sessionlink status="${participant.gameSession.status}" href="${deletemessagelink}" tooltip="delete"><img src="themes/default/button_delete.gif"/></cyb:sessionlink></td></tr>
                </c:forEach>
                </table>                
                
                <h2><spring:message code="SentMessages" /></h2>
                <cyb:sessionlink status="${participant.gameSession.status}" href="sendmessage.htm?participantId=${participant.id}" styleClass="button"><spring:message code="sendmessage" /></cyb:sessionlink>                                
                <table>
                <tr><th><spring:message code="message.from" /></th><th><spring:message code="message.to" /></th><th><spring:message code="message.subject" /></th><th><spring:message code="stepofplay" /></th><th><spring:message code="datetime" /></th><th><spring:message code="message.attachments" /></th></tr>
                <c:if test="${outboxmessagesListHolder.nrOfElements == 0}">
                	<tr><td colspan="6" ><spring:message code="noitemsfound.messages" /></td></tr>
                </c:if>
                <c:forEach items="${outboxmessagesListHolder.pageList}" var="message" >
                <tr><td><c:out value="${message.sender.roleAndPlayground.role.name}" /></td><td><c:forEach items="${message.recipients}" var="recipient"><c:out value="${recipient.roleAndPlayground.role.name}" /><br /></c:forEach></td><td><c:out value="${message.subject}" /></td><td><c:out value="${message.stepOfPlay}" /></td><td><fmt:formatDate value="${message.sentDate}" pattern="dd MMM yyyy HH:mm" /></td><td><c:out value="${fn:length(message.attachments)}"/></td>
                <td class="icons"><c:url value="message.htm" var="viewmessagelink"><c:param name="participantId" value="${participant.id}"/><c:param name="id" value="${message.id}" /><c:param name="box" value="outbox" /></c:url><a href="${viewmessagelink}" title="<spring:message code="view"/>"><img src="themes/default/button_view.gif"/></a>
                <c:url value="sessiondeletemessage.htm" var="deletemessagelink"><c:param name="participantId" value="${participant.id}"/><c:param name="id" value="${message.id}" /><c:param name="box" value="outbox" /></c:url><cyb:sessionlink status="${participant.gameSession.status}" href="${deletemessagelink}" tooltip="delete"><img src="themes/default/button_delete.gif"/></cyb:sessionlink></td></tr>
                </c:forEach>
                </table>
                
                <c:url value="messagetrash.htm" var="trashlink"><c:param name="participantId" value="${participant.id}" /></c:url>
    			<a href="${trashlink}" class="button"><spring:message code="messagetrash" /></a>
                </div>
  </tiles:putAttribute>
</tiles:insertTemplate>