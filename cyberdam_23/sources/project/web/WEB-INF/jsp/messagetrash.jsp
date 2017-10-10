<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/> : <cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="messagetrash.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="messagetrash.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
                <div><h2><spring:message code="inboxmessagetrash" /></h2>
                <table>
                <tr><th><spring:message code="message.from" /></th><th><spring:message code="message.to" /></th><th><spring:message code="message.subject" /></th><th><spring:message code="stepofplay" /></th><th><spring:message code="datetime" /></th><th><spring:message code="message.attachments" /></th></tr>
                <c:if test="${empty participant.trash.messages}">
                	<tr><td colspan="6" ><spring:message code="noitemsfound.messages" /></td></tr>
                </c:if>
                <c:forEach items="${participant.trash.messages}" var="message" >
                <tr><td><c:out value="${message.sender.roleAndPlayground.role.name}" /></td><td><c:forEach items="${message.recipients}" var="recipient"><c:out value="${recipient.roleAndPlayground.role.name}" /><br /></c:forEach></td><td><c:out value="${message.subject}" /></td><td><c:out value="${message.stepOfPlay}" /></td><td><fmt:formatDate value="${message.sentDate}" pattern="dd MMM yyyy HH:mm" /></td><td><c:out value="${fn:length(message.attachments)}"/></td>
                <td class="icons"><c:url value="message.htm" var="viewmessagelink"><c:param name="participantId" value="${participant.id}"/><c:param name="id" value="${message.id}" /><c:param name="box" value="trash" /></c:url><a href="${viewmessagelink}" title="<spring:message code="view"/>"><img src="themes/default/button_view.gif"/></a>
                <c:url value="sessiondeletemessage.htm" var="undeletemessagelink"><c:param name="participantId" value="${participant.id}"/><c:param name="id" value="${message.id}" /><c:param name="restore" value="true" /><c:param name="box" value="inbox" /></c:url><cyb:sessionlink status="${participant.gameSession.status}" href="${undeletemessagelink}" tooltip="undelete"><img src="themes/default/button_undo.gif"/></cyb:sessionlink></td></tr>
                </c:forEach>
                </table>
                </div>
                <div><h2><spring:message code="Outboxmessagetrash" /></h2>
                <table>
                <tr><th><spring:message code="message.from" /></th><th><spring:message code="message.to" /></th><th><spring:message code="message.subject" /></th><th><spring:message code="stepofplay" /></th><th><spring:message code="datetime" /></th><th><spring:message code="message.attachments" /></th></tr>
                <c:if test="${empty participant.outtrash.messages}">
                	<tr><td colspan="6" ><spring:message code="noitemsfound.messages" /></td></tr>
                </c:if>
                <c:forEach items="${participant.outtrash.messages}" var="message" >
                <tr><td><c:out value="${message.sender.roleAndPlayground.role.name}" /></td><td><c:forEach items="${message.recipients}" var="recipient"><c:out value="${recipient.roleAndPlayground.role.name}" /><br /></c:forEach></td><td><c:out value="${message.subject}" /></td><td><c:out value="${message.stepOfPlay}" /></td><td><fmt:formatDate value="${message.sentDate}" pattern="dd MMM yyyy HH:mm" /></td><td><c:out value="${fn:length(message.attachments)}"/></td>
                <td class="icons"><c:url value="message.htm" var="viewmessagelink"><c:param name="participantId" value="${participant.id}"/><c:param name="id" value="${message.id}" /><c:param name="box" value="outtrash" /></c:url><a href="${viewmessagelink}" title="<spring:message code="view"/>"><img src="themes/default/button_view.gif"/></a>
                <c:url value="sessiondeletemessage.htm" var="undeletemessagelink"><c:param name="participantId" value="${participant.id}"/><c:param name="id" value="${message.id}" /><c:param name="restore" value="true" /><c:param name="box" value="outbox" /></c:url><cyb:sessionlink status="${participant.gameSession.status}" href="${undeletemessagelink}" tooltip="undelete"><img src="themes/default/button_undo.gif"/></cyb:sessionlink></td></tr>
                </c:forEach>
                </table>
                </div>
  </tiles:putAttribute>
</tiles:insertTemplate>