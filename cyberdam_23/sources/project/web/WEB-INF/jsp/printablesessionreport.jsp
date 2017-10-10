<%@ include file="includes.jsp"%>
<%@page import="nl.cyberdam.domain.GameSession;"%>
<tiles:insertTemplate template="layout_basic.jsp">

	<tiles:putAttribute name="breadcrumb">
		<cyb:crumb link="index.htm" mlkey="homepage.title" />
		<cyb:csep />
		<cyb:crumb link="gamemaster.htm" mlkey="gamemaster.title" />
		<cyb:csep />
		<c:url value="gamesessioncontrol.htm" var="gamesessioncontrol">
			<c:param name="id" value="${param.id}" />
		</c:url>
		<cyb:crumb link="${gamesessioncontrol}"
			mlkey="gamesessioncontrol.title" />
		<cyb:csep />
		<c:url value="sessionreport.htm" var="sessionreportlink">
			<c:param name="id" value="${param.id}" />
		</c:url>
		<cyb:crumb link="${sessionreportlink}" mlkey="sessionreport.title" />
		<cyb:csep />
		<cyb:crumb mlkey="printablesessionreport.title" />
	</tiles:putAttribute>

	<tiles:putAttribute name="title">
		<spring:message code="printablesessionreport.title" />
		<c:out value="${gameSession.name}" />
	</tiles:putAttribute>

	<tiles:putAttribute name="introduction">
		<spring:message code="printablesessionreport.introduction" />
		<br />
	</tiles:putAttribute>

	<tiles:putAttribute name="content">
        <%-- printable style --%>
		<style type="text/css">
			h2 { padding-top: 0.6em; }
			@media print {
			    body { font: normal 9pt/ 1.2em Arial, Helvetica, sans-serif; }
			    h2 { font-size: 12pt; }
			    td { width: 6cm; }
			    div.top-nav, div.bottom-nav, a.button { display: none; }
			}
		</style>
        <%-- after onload: print dialog --%>
        <script type="text/javascript">
	        Event.observe (window, 'load',
	            function() {
	               window.print();
	            }
	        );
        </script>


        <%-- Session.Properties --%>
        <div>
		<h2><spring:message code="Session.Properties" /></h2>
		<table class="edit">
			<tr>
				<td class="first"><spring:message code="printablesessionreport.user" /></td>
				<td><c:out value="${user.firstName}" /> <c:out
					value="${user.lastName}" /> (<c:out value="${user.username}" />)</td>
			</tr>
			<tr>
				<td><spring:message code="printablesessionreport.date" /></td>
				<td><c:out value="${date}" /></td>
			</tr>
			<tr>
				<td><spring:message code="printablesessionreport.home" /></td>
				<td><c:out value="${home}" /></td>
			</tr>
			<tr>
				<td><spring:message code="printablesessionreport.version" /></td>
				<td><c:out value="${version}" /></td>
			</tr>
		</table>
        </div>
        <%-- /Session.Properties --%>

        <%-- Playground.Info --%>
		<div>
		<h2><spring:message code="Playground.Info" /></h2>
		<table class="edit">
			<c:forEach items="${playgrounds}" var="playground">
				<tr>
					<td class="first"><spring:message code="playground.name" /></td>
					<td>${playground.name}</td>
				</tr>
			</c:forEach>
		</table>
		</div>
        <%-- /Playground.Info --%>


        <%-- Game.Info --%>
		<div>
		<h2><spring:message code="Game.Info" /></h2>
		<table class="edit">
			<tr>
				<td class="first"><spring:message code="gamemodel.name" /></td>
				<td><c:out value="${gameSession.manifest.gameModel.name}" /></td>
			</tr>
			<tr>
				<td><spring:message code="caption" /></td>
				<td><c:out escapeXml="false"
					value="${gameSession.manifest.gameModel.caption}" /></td>
			</tr>
			<tr>
				<td><spring:message code="createdby" /></td>
				<td><cyb:displayuser
					user="${gameSession.manifest.gameModel.creator}" /></td>
			</tr>
			<tr>
				<td><spring:message code="createdon" /></td>
				<td><cyb:date value="${gameSession.manifest.gameModel.created}" /></td>
			</tr>
			<tr>
				<td><spring:message code="lastmodifiedby" /></td>
				<td><cyb:displayuser
					user="${gameSession.manifest.gameModel.lastModifier}" /></td>
			</tr>
			<tr>
				<td><spring:message code="lastmodified" /></td>
				<td><cyb:date
					value="${gameSession.manifest.gameModel.lastModified}" /></td>
			</tr>
		</table>
		</div>
        <%-- /Game.Info --%>


        <%-- Manifest.Info --%>
		<div>
		<h2><spring:message code="Manifest.Info" /></h2>
		<table class="edit">
			<tr>
				<td class="first"><spring:message code="manifest.name" /></td>
				<td><c:out value="${gameSession.manifest.name}" /></td>
			</tr>
			<tr>
				<td><spring:message code="created.by" /></td>
				<td><cyb:displayuser user="${gameSession.manifest.creator}" /></td>
			</tr>
			<tr>
				<td><spring:message code="created.on" /></td>
				<td><cyb:date value="${gameSession.manifest.created}" /></td>
			</tr>
			<tr>
				<td><spring:message code="lastmodified.by" /></td>
				<td><cyb:displayuser
					user="${gameSession.manifest.lastModifier}" /></td>
			</tr>
			<tr>
				<td><spring:message code="lastmodified" /></td>
				<td><cyb:date value="${gameSession.manifest.lastModified}" /></td>
			</tr>
		</table>
		</div>
        <%-- /Manifest.Info --%>


        <%-- Session.Info --%>
		<div>
		<h2><spring:message code="Session.Info" /></h2>
		<table class="edit">
			<tr>
				<td class="first"><spring:message code="session.name" /></td>
				<td><c:out value="${gameSession.name}" /></td>
			</tr>
			<tr>
				<td><spring:message code="session.id" /></td>
				<td><c:out value="${gameSession.id}" /></td>
			</tr>
			<tr>
				<td><spring:message code="gamemaster" /></td>
				<td><cyb:displayuser user="${gameSession.owner}" /></td>
			</tr>
			<tr>
				<td><spring:message code="sessionstatus" /></td>
				<td><spring:message code="status.${gameSession.status}" /></td>
			</tr>
			<tr>
				<td><spring:message code="sessionstarted" /></td>
				<td><cyb:date value="${gameSession.runningStarted}" /></td>
			</tr>
			<tr>
				<td><spring:message code="sessionstopped" /></td>
				<td><cyb:date value="${gameSession.sessionStopped}" /></td>
			</tr>
		</table>
		</div>
        <%-- /Session.Info --%>

    
        <%-- Roles --%>
        <div>
		<h2><spring:message code="Roles" /></h2>
		<table class="edit">
			<tr>
				<th><spring:message code="role" /></th>
				<th><spring:message code="playgroundcharactername" /></th>
				<th><spring:message code="users" /></th>
			</tr>
			<c:forEach items="${participants}" var="roleMapping"
				varStatus="rowCounter">
				<tr>
					<td class="first"><c:out value="${roleMapping.roleAndPlayground.role.name}" /></td>
					<td><c:out
						value="${roleMapping.roleAndPlayground.playgroundObject.name}" />,
					<c:out value="${roleMapping.roleAndPlayground.playground.name}" /></td>
					<td id="participants[${rowCounter.index}].users"><c:forEach
						items="${roleMapping.users}" var="user"
						varStatus="usersRowCounter">
						<div class="user usermarker"
							id="p${rowCounter.index}u${usersRowCounter.index}"><cyb:displayuser
							user="${user}" /></div>
					</c:forEach></td>
				</tr>
			</c:forEach>
		</table>
        </div>
        <%-- /Roles --%>


        <%-- Session.Report --%>
        <div>
		<h2><spring:message code="Session.Report" /></h2>
		<table class="edit">
			<tr>
				<th><spring:message code="activity.name" /></th>
				<th><spring:message code="game.participant" /></th>
				<th><spring:message code="role" /></th>
				<th><spring:message code="date" /></th>
				<th><spring:message code="stepOfPlay" /></th>
				<th><spring:message code="type" /></th>
				<th><spring:message code="fileName" /></th>
				<th><spring:message code="subject" /></th>
			</tr>
			<c:if test="${fn:length(sessionReportList) == 0}">
				<tr><td colspan="8"><spring:message code="noitemsfound.sessionreport" /></td></tr>
			</c:if>
			<c:forEach items="${sessionReportList}" var="sessionreport">
				<c:choose>
				<c:when test="${empty sessionreport.activity}">
					<c:set var="activityName"><spring:message code="user.initiated"/></c:set>
				</c:when>
				<c:otherwise>
					<c:set var="activityName" value="${sessionreport.activity.name}" />
				</c:otherwise>
				</c:choose>
				<tr>
					<td><c:out value="${activityName}" /></td>
					<td><c:out value="${sessionreport.userName}" /></td>
					<td><c:out value="${sessionreport.role.name}" /></td>
					<td><c:out value="${sessionreport.date}" /></td>
					<td><c:out value="${sessionreport.stepOfPlay}" /></td>
					<td><c:out value="${sessionreport.type}" /></td>
					<td><c:out value="${sessionreport.fileName}" /></td>
					<td><c:out value="${sessionreport.subject}" /></td>
				</tr>
			</c:forEach>
		</table>
        </div>
        <%-- /Session.Report --%>

		<br />
		<br />

		<a href="${sessionreportlink}" class="button"><spring:message
			code="link.back" /></a>
	</tiles:putAttribute>

</tiles:insertTemplate>
