<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <c:choose>
  <c:when test="${empty param.participantId}">
	<c:url value="gamesessioncontrol.htm" var="gamesessioncontrol"><c:param name="id" value="${param.id}"/></c:url>
	<c:url value="sessionreport.htm" var="sessionreport"><c:param name="id" value="${param.id}"/></c:url>
	<tiles:putAttribute name="breadcrumb" >
	<cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/>
	<cyb:crumb link="gamemaster.htm" mlkey="gamemaster.title"/><cyb:csep/>
	<cyb:crumb link="${gamesessioncontrol}" mlkey="gamesessioncontrol.title"/><cyb:csep/>
	<cyb:crumb link="${sessionreport}" mlkey="sessionreport.title"/><cyb:csep/>
	<cyb:crumb mlkey="sessionactivityview.title"></cyb:crumb>
	</tiles:putAttribute>
  </c:when>
  <c:otherwise>
	  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
	  <c:url value="sessionrolereport.htm" var="sessionreport"><c:param name="id" value="${param.id}"/><c:param name="participantId" value="${param.participantId}"/></c:url>
  </c:otherwise>
  </c:choose>
	<tiles:putAttribute name="title" ><spring:message code="sessionactivityview.title"/> <c:out value="${sessionName}" /></tiles:putAttribute>
	<tiles:putAttribute name="introduction"><spring:message code="sessionactivityview.introduction"/></tiles:putAttribute>
	<tiles:putAttribute name="content">
		<c:choose>
		<c:when test="${!empty message}">
		<table class="edit">
			<tr>
				<td class="first"><spring:message code="from" /></td>
				<td><c:out value="${message.sender.roleAndPlayground}" /></td>
			</tr>
			<tr>
				<td><spring:message code="to" /></td>
				<td><c:forEach items="${message.recipients}" var="recipient">
						<c:out value="${recipient.roleAndPlayground}" /><br />
					</c:forEach>
				</td>
			</tr>
			<tr>
				<td><spring:message code="subject" /></td>
				<td><c:out value="${message.subject}" /></td>
			</tr>
			<tr>
				<td><spring:message code="message.attachments" /></td>
				<td><c:forEach items="${message.attachments}" var="attachment">
					  <c:url value="/sessionresource" var="resourceurl" ><c:param name="id" value="${attachment.id}" /></c:url>
					  <a href="${resourceurl}"><c:out value="${attachment.name}" /></a><br/>
					</c:forEach>
				</td>
			</tr>
		</table>
		<br />
		<h3><spring:message code="body" /></h3>
		<div class="messagebody"><c:out value="${message.body}" escapeXml="false"/></div>
		</c:when>
		<c:otherwise>
		<c:url value="/resource" var="resourceurl"><c:param name="sessionResourceId" value="${sessionResourceId}"/></c:url>
		<iframe style="width:100%;height:500px" src="${resourceurl}"></iframe>
		</c:otherwise>
		</c:choose>
		<a href="${sessionreport}" class="button"><spring:message code="link.back"/></a>
	</tiles:putAttribute>
</tiles:insertTemplate>
