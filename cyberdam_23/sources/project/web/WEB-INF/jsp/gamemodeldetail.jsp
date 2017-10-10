<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep /><cyb:crumb mlkey="gamemodel.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gamemodel.title"/> <c:out value="${gameModel.name}" /></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gamemodeldetail.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
  <c:set var="baselnk" ><c:url value="gamemodeldetail.htm"><c:param name="id" value="${gameModel.id}" /></c:url></c:set>
	<script type="text/javascript" src="<c:url value="/js/json2.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/grid.js"/>"></script>
	<script type="text/javascript">
		grid.gridData = JSON.parse('${gridData}');
		grid.noGridString = '<spring:message code="noitemsfound.activitygrid"/>';
		grid.shiftLeftString = '<spring:message code="shift.left"/>';
		grid.shiftRightString = '<spring:message code="shift.right"/>';
		grid.editStepString = '<spring:message code="button.editstepdetails"/>';
		grid.editStepUrl = '<c:url value="${baselnk}"><c:param name="object" value="stepofplay" /><c:param name="action" value="edit" /></c:url>';
		grid.nrRoles = ${fn:length(gameModel.roles)};
	</script>
		<ul>
			<li><a href="#metadata_section"><spring:message code="gamemodelstep.metadata"/></a></li>
			<li><a href="#gameplay_section"><spring:message code="gamemodelstep.gameplay"/></a></li>
			<li><a href="#activities_section"><spring:message code="gamemodelstep.activities"/></a></li>
			<li><a href="#resources_section"><spring:message code="gamemodelstep.resources"/></a></li>
			<li><a href="#variables_section"><spring:message code="gamemodelstep.variables"/></a></li>
		</ul>
		<br/>
		<h3 id="metadata_section"><spring:message code="gamemodelmetadata.title"/></h3>
        <table class="detail">
        <tr><td class="first"><spring:message code="name"/></td><td><c:out value="${gameModel.name}" /></td></tr>
        <tr><td><spring:message code="caption" /></td><td><c:out value="${gameModel.caption}" /></td></tr>
        <tr><td><spring:message code="description"/></td><td><c:out value="${gameModel.description}" escapeXml="false"/></td></tr>
        <tr><td><spring:message code="model.statusTemplate"/></td><td><c:out value="${gameModel.statusTemplate}" escapeXml="false"/></td></tr>
        <tr><td><spring:message code="model.headerTemplate"/></td><td><c:out value="${gameModel.headerTemplate}" escapeXml="false"/></td></tr>
        <tr><td><spring:message code="numberofroles"/></td><td><c:out value="${fn:length(gameModel.roles)}"/></td></tr>
        <tr><td><spring:message code="status"/></td><td><spring:message code="status.${gameModel.status}"/></td></tr>
        <tr><td><spring:message code="model.classicLayout"/></td><td><c:if test="${gameModel.sessionClassicLayout}"><spring:message code="yes"/></c:if><c:if test="${!gameModel.sessionClassicLayout}"><spring:message code="no"/></c:if></td></tr>
        </table>
		<br/>
	    <table class="noborders"><tr>
		<td><a href="gamemodelmetadata.htm" class="button" ><spring:message code="edit"/></a></td>
		</tr></table>
		<br/>
		<h3 id="gameplay_section"><spring:message code="gamemodelgameplay.title"/></h3>
		<div id="grid">
			<table>
				<tr>
					<th <c:if test="${fn:length(gameModel.roles) == 0 || fn:length(gameModel.stepsOfPlay) == 0}">class="buttondimension"</c:if>>&nbsp;</th>
				</tr>
			<c:forEach items="${gameModel.roles}" var="role">
				<tr>
					<td class="bold"><c:out value="${role.name}" /></td>
					<td class="button"><a href="<c:url value="gamemodelrolevariables.htm"><c:param name="id" value="${role.id}" /></c:url>" class="button"><spring:message code="button.rolevariables"/></a></td>
				</tr>
			</c:forEach>
			<tr class="button"><td>&nbsp;</td></tr>
			</table>
		</div>
		<br/>
	    <table class="noborders"><tr>
		<td><a href="gamemodelgameplay.htm" class="button" ><spring:message code="edit"/></a></td>
		</tr></table>
		<br/>
        <h3 id="activities_section"><spring:message code="activities" /></h3>
		<c:if test="${!empty activitiesiiuerror}"><div class="errorBox">${activitiesiiuerror}</div></c:if>
		<c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="activities" /></c:url></c:set>
        <table>
        <tr><th><spring:message code="activity.type" /></th>
            <th><spring:message code="activity.name" /></th>
        </tr>
        <c:if test="${activities.nrOfElements == 0}">
           	<tr><td colspan="2" ><spring:message code="noitemsfound.activities" /></td></tr>
        </c:if>
        <c:forEach items="${activities.pageList}" var="activity">
        <tr><c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="activities" /><c:param name="objectId" value="${activity.id}" /><c:param name="action" value="edit" /></c:url></c:set>
        	<td class="first"><a href="${lnk}"><spring:message code="activitytype.${activity.type}" /></a></td>
            <td><c:out value="${activity.name}" /></td>
            <td class="icons">
            	<a href="${lnk}" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
                <c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="activities" /><c:param name="objectId" value="${activity.id}" /><c:param name="action" value="delete" /></c:url></c:set>
                <a href="${lnk}" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a>
                <c:set var="lnk"><c:url value="${baselnk}"><c:param name="objectId" value="${activity.id}" /><c:param name="action" value="copy" /></c:url></c:set>
                <a href="${lnk}" title="<spring:message code="copy"/>"><img src="themes/default/button_copy.gif"/></a>
            </td>
        </tr>
        </c:forEach>
        </table>
        <br/>
	    <table class="noborders"><tr>
			<c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="activities" /><c:param name="action" value="add" /></c:url></c:set>
	    	<td><a href="<c:url value="${lnk}"><c:param name="type" value="fileupload" /></c:url>" class="button"><spring:message code="add.file.upload.activity"/></a></td>
	    	<td><a href="<c:url value="${lnk}"><c:param name="type" value="message" /></c:url>" class="button"><spring:message code="add.message.activity"/></a></td>
	    	<td><a href="<c:url value="${lnk}"><c:param name="type" value="progress" /></c:url>" class="button"><spring:message code="add.progress.activity"/></a></td>
	    	<td><a href="<c:url value="${lnk}"><c:param name="type" value="form" /></c:url>" class="button"><spring:message code="add.variable.activity"/></a></td>
	    	<td><a href="<c:url value="${lnk}"><c:param name="type" value="event" /></c:url>" class="button"><spring:message code="add.event.activity"/></a></td>
	   	</tr></table>
		<br/>
        <h3 id="resources_section"><spring:message code="resources" /></h3>
		<c:if test="${!empty resourcesiiuerror}"><div class="errorBox">${resourcesiiuerror}</div></c:if>
		<jsp:useBean id="now" class="java.util.Date" scope="request"/><!--  this is to disable browser caching -->
		<c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="resources" /><c:param name="rnd" value="${now.time}" /></c:url></c:set>
        <table>
        <tr><th><cyb:sortlink lnk="${lnk}#resources_section" sort="name"><spring:message code="resource.name" /></cyb:sortlink></th>
            <th><cyb:sortlink lnk="${lnk}#resources_section" sort="fileName"><spring:message code="filename" /></cyb:sortlink></th>
            <th><spring:message code="filesize" /></th>
        </tr>
        <c:if test="${resources.nrOfElements == 0}">
           	<tr><td colspan="3" ><spring:message code="noitemsfound.resources" /></td></tr>
        </c:if>
        <c:forEach items="${resources.pageList}" var="resource">
        <tr><td class="first"><a href="gamemodelresourceedit.htm?id=${resource.id}"><c:out value="${resource.name}" /></a></td>
            <td><c:out value="${resource.fileName}" /></td>
            <td><c:out value="${resource.fileSize}" /> <spring:message code="bytes" /></td>
            <td class="icons">
            	<a href="gamemodelresourceedit.htm?id=${resource.id}" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
                <c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="resources" /><c:param name="objectId" value="${resource.id}" /><c:param name="action" value="delete" /></c:url></c:set>
                <a href="${lnk}" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a>
                <c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="resources" /><c:param name="objectId" value="${resource.id}" /><c:param name="action" value="view" /></c:url></c:set>
				<a href="${lnk}" title="<spring:message code="view"/>"><img src="themes/default/button_view.gif"/></a>
            </td>
        </tr>
        </c:forEach>
        </table>
	    <table class="noborders"><tr>
	    	<td><a href="resourceupload.htm?id=${gameModel.id}&amp;redirectTo=gamemodeldetail.htm" class="button"><spring:message code="resource.new"/></a></td>
	   	</tr></table>
		<br/>
		<h3 id="variables_section"><spring:message code="gamemodelstep.variables" /></h3>
		<c:if test="${!empty variablesiiuerror}"><div class="errorBox">${variablesiiuerror}</div></c:if>
			<c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="variables" /><c:param name="rnd" value="${now.time}" /></c:url></c:set>
			<table>
			<tr>
				<th><cyb:sortlink lnk="${lnk}#variables_section" sort="name"><spring:message code="variable.name" /></cyb:sortlink></th>
				<th><spring:message code="variable.initialized" /></th>
				<th><cyb:sortlink lnk="${lnk}#variables_section" sort="initialValue"><spring:message code="variable.initialValue" /></cyb:sortlink></th>
			</tr>
			<c:if test="${variables.nrOfElements == 0}">
				<tr><td colspan="3" ><spring:message code="noitemsfound.variables" /></td></tr>
			</c:if>
			<c:forEach items="${variables.pageList}" var="variable">
				<tr>
					<c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="variables" /><c:param name="objectId" value="${variable.id}" /><c:param name="action" value="edit" /></c:url></c:set>
					<td class="first"><a href="${lnk}"><c:out value="${variable.name}"/></a></td>
					<td><input type="checkbox" disabled="disabled" <c:if test="${!empty variable.initialValue}">checked</c:if>/></td>
					<td><c:out value="${variable.initialValue}"/></td>
					<td class="icons">
			<a href="${lnk}" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
            <c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="variables" /><c:param name="objectId" value="${variable.id}" /><c:param name="action" value="delete" /></c:url></c:set>
			<a href="${lnk}" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a>
					</td>
				</tr>
            </c:forEach>
            </table>
	    <table class="noborders"><tr>
            <c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="variables" /><c:param name="action" value="add" /></c:url></c:set>
	    	<td><a href="${lnk}" class="button"><spring:message code="button.addvariable"/></a></td>
		</tr></table>
		<br/>
		<hr />
        <a href="<c:url value="gameauthor.htm?forceRefresh=true"/>" class="button" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>
