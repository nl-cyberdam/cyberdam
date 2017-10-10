<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep /><cyb:crumb link="gamemodeldetail.htm?id=${gameModel.id}" mlkey="gamemodel.title"/> - <cyb:crumb mlkey="rolevariables.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="rolevariables.title"/>&nbsp;<c:out value="${role.name}" /></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gamemodelrolevariable.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
	<c:set var="baselnk" ><c:url value="gamemodelrolevariables.htm"><c:param name="id" value="${role.id}" /></c:url></c:set>
	<h2><spring:message code="rolevariables.title" /></h2>
	<table>
		<tr>
			<th><cyb:sortlink lnk="${baselnk}" sort="name"><spring:message code="variable.name" /></cyb:sortlink></th>
			<th><spring:message code="variable.initialized" /></th>
			<th><cyb:sortlink lnk="${baselnk}" sort="initialValue"><spring:message code="variable.initialValue" /></cyb:sortlink></th>
		</tr>
		<c:if test="${roleVariables.nrOfElements == 0}">
			<tr><td colspan="3" ><spring:message code="noitemsfound.variables" /></td></tr>
		</c:if>
		<c:forEach items="${roleVariables.pageList}" var="rolevariable">
			<tr>
				<c:set var="lnk"><c:url value="${baselnk}"><c:param name="objectId" value="${rolevariable.id}" /><c:param name="action" value="edit" /></c:url></c:set>
				<td class="first"><a href="${lnk}"><c:out value="${rolevariable.name}"/></a></td>
				<td><input type="checkbox" disabled <c:if test="${!empty rolevariable.initialValue}">checked</c:if>/></td>
				<td><c:out value="${rolevariable.initialValue}"/></td>
				<td class="icons">
		<a href="${lnk}" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
		<c:set var="lnk"><c:url value="${baselnk}"><c:param name="action" value="delete" /><c:param name="objectId" value="${rolevariable.id}" /></c:url></c:set>
		<a href="${lnk}" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a>
				</td>
			</tr>
		</c:forEach>
	</table>
	<table class="noborders"><tr>
		<c:set var="lnk"><c:url value="${baselnk}"><c:param name="object" value="variables" /><c:param name="action" value="add" /></c:url></c:set>
		<td><a href="${lnk}" class="button"><spring:message code="button.addvariable"/></a></td>
	</tr></table>
	<br />
	<hr />
	<c:url value="gamemodeldetail.htm" var="backurl"><c:param name="id" value="${gameModel.id}"/></c:url>
	<a href="${backurl}#gameplay_section" class="button"><spring:message code="link.back"/></a>
</tiles:putAttribute>
</tiles:insertTemplate>
