<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb mlkey="gameauthor.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gameauthor.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gameauthor.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <form id="mainform" action="" method="POST">
        <%@ include file="textfilter.jsp" %>
            <table class="usertable">
                <thead>
                    <tr>
                        <th><cyb:sortlink lnk="${lnk}" sort="name"><spring:message code="game.name"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="status"><spring:message code="status"/></cyb:sortlink></th>
                        <th><spring:message code="numberofroles"/></th>
                        <th><spring:message code="caption"/></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="lastModified"><spring:message code="version"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="owner"><spring:message code="game.owner"/></cyb:sortlink></th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${gamemodels.nrOfElements == 0}">
                	<tr><td colspan="7" ><spring:message code="noitemsfound.gamemodels" /></td></tr>
                	</c:if>
                    <c:forEach items="${gamemodels.pageList}" var="gamemodel">
                        <tr>
                            <td><c:choose >
                            	<c:when test="${gamemodel.editable}">
                            		<c:url value="gamemodeldetail.htm" var="gamemodelurl" ><c:param name="id" value="${gamemodel.id}"/></c:url><a href="${gamemodelurl}"><c:out value="${gamemodel.name}"/></a>
								</c:when>
								<c:otherwise>
									<c:out value="${gamemodel.name}"/>
								</c:otherwise>                            	
                            </c:choose></td>                            
                            <td><spring:message code="status.${gamemodel.status}"/></td>
                            <td><c:out value="${fn:length(gamemodel.roles)}"/></td>
                            <td><c:out value="${gamemodel.caption}" /></td>
                            <td><fmt:formatDate value="${gamemodel.lastModified}" pattern="dd MMM yyyy HH:mm"/></td>
                            <td><cyb:displayuser user="${gamemodel.owner}"/></td>
                            <td class="icons">
                            <c:if test="${gamemodel.editable}">
                            	<a href="${gamemodelurl}" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
                            	<c:url value="deletegamemodel.htm" var="gamemodeldeleteurl" ><c:param name="id" value="${gamemodel.id}"/></c:url>
                            	<a href="${gamemodeldeleteurl}" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a>
                            </c:if>
							<c:url value="copygamemodel.htm" var="gamemodelcopyurl" ><c:param name="id" value="${gamemodel.id}"/></c:url>
							<a href="${gamemodelcopyurl}" title="<spring:message code="copy"/>"><img src="themes/default/button_copy.gif"/></a>
							<c:url value="gamemodelexport.htm" var="gamemodelexporturl" ><c:param name="id" value="${gamemodel.id}"/></c:url>
							<a href="${gamemodelexporturl}" title="<spring:message code="gamemodelexport.link"/>"><img src="themes/default/button_export.gif"/></a>
							</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
			<cyb:pagernav pagedListHolder="${gamemodels}" lnk="${lnk}"/>
		<cyb:pager pagedListHolder="${gamemodels}" />
        </form>
        <br/>
        <table class="noborders"><tr>
        <td><a href="<c:url value="gamemodeldetail.htm" ></c:url>" class="button" ><spring:message code="link.new.game"/></a></td>
        <td><a href="<c:url value="gamemodelimport.htm"/>" class="button" >
        	<spring:message code="gamemodelimport.link"/></a></td>
        </tr></table>
        <br/>
        <hr />
        <a href="<c:url value="index.htm"/>" class="button">
        <spring:message code="link.back"/></a>
</tiles:putAttribute>
</tiles:insertTemplate>