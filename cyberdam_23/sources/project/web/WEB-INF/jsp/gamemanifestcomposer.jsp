<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb mlkey="gamemanifestcomposer.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gamemanifestcomposer.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gamemanifestcomposer.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <form id="mainform" action="" method="POST">
        <%@ include file="textfilter.jsp" %>
			<table class="usertable">
                <thead>
                    <tr class="title">
                        <th><cyb:sortlink lnk="${lnk}" sort="name"><spring:message code="manifest.name"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="gameModel.name"><spring:message code="gamename"/></cyb:sortlink></th>
                        <th><spring:message code="numberofroles"/></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="status"><spring:message code="status"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="lastModified"><spring:message code="lastmodified"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="owner"><spring:message code="manifest.owner"/></cyb:sortlink></th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${gamemanifests.nrOfElements == 0}">
                		<tr><td colspan="7" ><spring:message code="noitemsfound.gamemanifests" /></td></tr>
                	</c:if>
                    <c:forEach items="${gamemanifests.pageList}" var="gamemanifest">
                        <tr>
                            <td>
                 				<c:if test="${gamemanifest.editable}"><a href="<c:url value="gamemanifest.htm"><c:param name="id" value="${gamemanifest.id}"/></c:url>"><c:out value="${gamemanifest.name}"/></a></c:if>
                            	<c:if test="${not gamemanifest.editable}"><c:out value="${gamemanifest.name}"/></c:if>
                           	</td>
                            <td><c:out value="${gamemanifest.gameModel.name}"/></td>
                            <td><c:out value="${fn:length(gamemanifest.gameModel.roles)}"/></td>
                            <td><spring:message code="status.${gamemanifest.status}"/></td>
                            <td><fmt:formatDate value="${gamemanifest.lastModified}" pattern="dd MMM yyyy HH:mm" /></td>
                            <td><cyb:displayuser user="${gamemanifest.owner}" /></td>
                            <td class="icons">
                            <c:if test="${gamemanifest.editable}"><a href="<c:url value="gamemanifest.htm"><c:param name="id" value="${gamemanifest.id}"/></c:url>" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a></c:if>
                            <c:if test="${gamemanifest.editable}"><a href="<c:url value="deletegamemanifest.htm"><c:param name="id" value="${gamemanifest.id}"/></c:url>" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a></c:if>
                            <a href="<c:url value="copygamemanifest.htm"><c:param name="id" value="${gamemanifest.id}"/></c:url>" title="<spring:message code="copy"/>"><img src="themes/default/button_copy.gif"/></a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
			<cyb:pagernav pagedListHolder="${gamemanifests}" lnk="${lnk}"/>
		<cyb:pager pagedListHolder="${gamemanifests}" />
        </form>
		<br />
        <a href="<c:url value="gamemanifest.htm"/>" class="button" ><spring:message code="link.new.manifest"/></a>
		<br />
        <hr />
        <a href="<c:url value="index.htm"/>" class="button" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>