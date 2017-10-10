<%@ include file="includes.jsp"%>
<tiles:insertTemplate template="layout_popup.jsp">
	<tiles:putAttribute name="content">
		<form id="mainform" action="" method="POST"><%@ include file="textfilter.jsp"%>
		<table class="usertable">
			<thead>
				<tr class="title">
					<th><cyb:sortlink lnk="" sort="name">
						<spring:message code="gamemanifest.name" />
					</cyb:sortlink></th>
					<th><cyb:sortlink lnk="" sort="status">
						<spring:message code="status" />
					</cyb:sortlink></th>
					<th><cyb:sortlink lnk="" sort="owner.name">
						<spring:message code="owner" />
					</cyb:sortlink></th>
					<th><cyb:sortlink lnk="" sort="gameModel.name">
						<spring:message code="gamemodel.name" />
					</cyb:sortlink></th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${gamemanifestslist.nrOfElements == 0}">
					<tr>
						<td colspan="4"><spring:message code="noitemsfound.gamemanifest" /></td>
					</tr>
				</c:if>
				<c:forEach items="${gamemanifestslist.pageList}" var="gamemanifest">
					<tr>
						<td><c:out value="${gamemanifest.name}" /></td>
						<td><spring:message var="status_i18n" code="status.${gamemanifest.status}" /><c:out value="${status_i18n}" /></td>
						<td><c:out value="${gamemanifest.owner.username}" /></td>
						<td><c:out value="${gamemanifest.gameModel.name}" /></td>
						<td class="button"><a href="" onClick="parent.selectGameManifest(${gamemanifest.id})" class="button" style="margin: 0"><spring:message code="choose" /></a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<cyb:pagernav pagedListHolder="${gameanifestslist}" lnk="" /> <cyb:pager pagedListHolder="${gamemanifestslist}" /></form>
	</tiles:putAttribute>
</tiles:insertTemplate>
