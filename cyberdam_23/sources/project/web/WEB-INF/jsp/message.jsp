<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="message.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="message.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
  <table class="edit">
  <tr><td class="first"><spring:message code="from" /></td><td><c:out value="${message.sender.roleAndPlayground}" /></td></tr>
  <tr><td><spring:message code="to" /></td><td><c:forEach items="${message.recipients}" var="recipient"><c:out value="${recipient.roleAndPlayground}" /><br /></c:forEach></td></tr>
  <tr><td><spring:message code="subject" /></td><td><c:out value="${message.subject}" /></td></tr>
  <tr><td><spring:message code="message.attachments" /></td><td><c:forEach items="${message.attachments}" var="attachment" varStatus="varStatus" >
  <c:url value="/sessionresource" var="resourceurl" ><c:param name="id" value="${attachment.id}" /></c:url><a href="${resourceurl}">
  <c:out value="${attachment.name}" /></a><br/></c:forEach></td></tr>
  </table><br />
  <h3><spring:message code="body" /></h3>
  <div class="messagebody">
  <c:out value="${message.body}" escapeXml="false"/>
  </div>
  <c:if test="${incoming}" >
  <form name="sendmessage" action="sendmessage.htm">
  <c:if test="${participant.gameSession.status eq 'RUNNING'}">
        <input type="hidden" name="participantId" value="${participant.id}" >
        <input type="hidden" name="messageId" value="${message.id}" >
        <input type="hidden" id="action1"/>
  </c:if>
  </form>
    <c:choose>
    <c:when test="${participant.gameSession.status eq 'RUNNING'}">
  <a href="#" onclick="document.getElementById('action1').name='_reply';document.forms.sendmessage.submit()" class="button"><spring:message code="button.reply"/></a>
    </c:when>
    <c:otherwise>
  <a class="button disabled"><spring:message code="button.reply"/></a>
    </c:otherwise>
  </c:choose>
  </c:if>
  <form name="session" action="session.htm">
  <input type="hidden" name="participantId" value="${participant.id}" >
  <input type="hidden" id="action2"/>
  </form><br/>
  <a href="#" onclick="document.getElementById('action2').name='_return';document.forms.session.submit()" class="button"><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>
