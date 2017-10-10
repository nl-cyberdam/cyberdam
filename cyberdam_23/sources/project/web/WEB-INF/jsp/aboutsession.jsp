<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="aboutsession.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="aboutsession.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
  			<c:set var="gameSession" value="${participant.gameSession}"/>
  			<c:set var="gameManifest" value="${gameSession.manifest}"/>
  			<c:set var="gameModel" value="${gameManifest.gameModel}"/>
            <div>
            <h2><spring:message code="Playground.Info" /></h2>
            <table class="edit">
            <c:forEach items="${playgroundNames}" var="playgroundName" >
            	<tr><td class="first"><spring:message code="playground.name" /></td><td>${playgroundName}</td></tr>
            </c:forEach>
            </table>
            </div>
            <div>
            <h2><spring:message code="Game.Info" /></h2>
            <table class="edit">
            <tr><td class="first"><spring:message code="gamemodel.name" /></td><td><c:out value="${gameModel.name}" /></td></tr>
            <tr><td><spring:message code="caption" /></td><td><c:out value="${gameModel.caption}" /></td></tr>
            <tr><td><spring:message code="createdby" /></td><td><cyb:displayuser user="${gameModel.creator}" /></td></tr>
            <tr><td><spring:message code="createdon" /></td><td><cyb:date value="${gameModel.created}" /></td></tr>
            <tr><td><spring:message code="lastmodifiedby" /></td><td><cyb:displayuser user="${gameModel.lastModifier}" /></td></tr>
            <tr><td><spring:message code="lastmodified" /></td><td><cyb:date value="${gameModel.lastModified}" /></td></tr>
            </table>
            </div>
            <div>
            <h2><spring:message code="Manifest.Info" /></h2>
            <table class="edit">
            <tr><td class="first"><spring:message code="manifest.name" /></td><td><c:out value="${gameManifest.name}" /></td></tr>
            <tr><td><spring:message code="created.by" /></td><td><cyb:displayuser user="${gameManifest.creator}" /></td></tr>
            <tr><td><spring:message code="created.on" /></td><td><cyb:date value="${gameManifest.created}"  /></td></tr>
            <tr><td><spring:message code="lastmodified.by" /></td><td><cyb:displayuser user="${gameManifest.lastModifier}" /></td></tr>
            <tr><td><spring:message code="lastmodified" /></td><td><cyb:date value="${gameManifest.lastModified}" /></td></tr>
            </table>
            </div>
            <div>
            <h2><spring:message code="Session.Info" /></h2>
            <table class="edit">
            <tr><td class="first"><spring:message code="session.name" /></td><td><c:out value="${gameSession.name}" /></td></tr>
            <tr><td><spring:message code="gamemaster" /></td><td><cyb:displayuser user="${gameSession.owner}" /></td></tr>
            <tr><td><spring:message code="sessionstatus" /></td><td><c:out value="${gameSession.status}" /></td></tr>
            <tr><td><spring:message code="sessionstarted" /></td><td><cyb:date value="${gameSession.runningStarted}" /></td></tr>
            <tr><td><spring:message code="sessionstopped" /></td><td><cyb:date value="${gameSession.sessionStopped}" /></td></tr>
            </table>
            </div>
  </tiles:putAttribute>
</tiles:insertTemplate>
