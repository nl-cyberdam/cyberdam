<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
	<c:set var="lnk" ><c:url value=""><c:param name="id" value="${gameModel.id}" /></c:url></c:set>
	<tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep /><cyb:crumb link="${lnk}" mlkey="gamemodel.title"/> - <cyb:crumb mlkey="gamemodelresource.view"/></tiles:putAttribute>
	<tiles:putAttribute name="title" ><spring:message code="gamemodelresource.view"/>&nbsp;${resource.name}</tiles:putAttribute>
	<tiles:putAttribute name="introduction"><spring:message code="gamemodelresourceview.introduction"/></tiles:putAttribute>
	<tiles:putAttribute name="content">
		<iframe style="width:100%;height:500px" src="<c:url value="/resource"><c:param name="resourceId" value="${resource.id}"/></c:url>"></iframe>
		<a href="${lnk}#resources_section" class="button"><spring:message code="link.back"/></a>
	</tiles:putAttribute>
</tiles:insertTemplate>
