<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="playgroundlistpage.htm" mlkey="playgroundlistpage.title"/><cyb:csep /><cyb:crumb mlkey="playgrounddetail.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="playgrounddetail.title"/> <c:out value="${playground.name}" /></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="playgrounddetail.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
  <c:set var="lnk" ><c:url value="playgrounddetail.htm"><c:param name="id" value="${playground.id}" /></c:url></c:set><%-- param is an implicit el variable --%>
        <form id="mainform" action="#playgroundobjects_section" method="POST">
  		<ul>
			<li><a href="#metadata_section"><spring:message code="playgrounddetail.metadata"/></a></li>
			<li><a href="#playgroundobjects_section"><spring:message code="playgrounddetail.playgroundobjects"/></a></li>
		</ul>
		<br/>
		<h3 id="metadata_section"><spring:message code="playgroundmetadata.title"/></h3>
	        <table class="detail">
        <tr><td class="first"><spring:message code="playground.name"/></td><td><c:out value="${playground.name}" /></td></tr>
        <tr><td><spring:message code="caption" /></td><td><c:out value="${playground.caption}" /></td></tr>
        <tr><td><spring:message code="uri"/></td><td><c:out value="${playground.uriId}" /></td></tr>
        <tr><td><spring:message code="description"/></td><td><c:out value="${playground.description}" escapeXml="false"/></td></tr>
        <tr><td><spring:message code="version" /></td><td><c:out value="${playground.version}" /></td></tr>
        <tr><td><spring:message code="lastmutatedby" /></td><td><cyb:displayuser user="${playground.lastModifier}"/></td></tr>
        <tr><td><spring:message code="lastmutatedon" /></td><td><fmt:formatDate value="${playground.lastModified}" pattern="dd MMM yyyy HH:mm" /></td></tr>
        <tr><td><spring:message code="playground.link" /></td><td><c:out value="${playground.link}"/></td></tr>
        <tr><td><spring:message code="status"/></td><td><spring:message code="status.${playground.status}"/></td></tr>
        </table>
		<br/>
	    <table class="noborders"><tr>
		<td><a href="playgroundeditorpage.htm?id=${playground.id}" class="button" ><spring:message code="edit"/></a></td>
		<td><a href="playgroundexport.htm?id=${playground.id}" class="button" ><spring:message code="playgroundexport.link"/></a></td>
		</tr></table>
		<br/>
		<h3 id="playgroundobjects_section"><spring:message code="playgroundobjects.title"/></h3>
        <c:set var="refreshLink" value="?forceRefresh=true&id=${playground.id}#playgroundobjects_section"/>
        <%@ include file="textfilter.jsp" %>
            <table class="usertable">
                <thead>
                    <tr class="title">
                        <th><cyb:sortlink lnk="${lnk}" sort="uri"><spring:message code="uri"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="name"><spring:message code="name"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="category"><spring:message code="category"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="onMap"><spring:message code="onmap"/></cyb:sortlink> / <cyb:sortlink lnk="${lnk}" sort="inDirectory"><spring:message code="indirectory"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="name"><spring:message code="status"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="lastModified"><spring:message code="lastmodified"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="lastModifier"><spring:message code="by"/></cyb:sortlink></th>
                    </tr>
                </thead>
                <tbody>
                	<c:if test="${playgroundObjects.nrOfElements == 0}">
                	<tr><td colspan="7" ><spring:message code="noitemsfound.playgroundobjects" /></td></tr>
                	</c:if>
                    <c:forEach items="${playgroundObjects.pageList}" var="playgroundObject">
                        <tr>
                        	<td><authz:authorize ifAnyGranted="ROLE_PLAYGROUNDAUTHOR">
    	                            <a href="playgroundobjecteditor.htm?id=${playgroundObject.id}"><c:out value="${playgroundObject.uri}"/></a>
            	                </authz:authorize>
                	            <authz:authorize ifNotGranted="ROLE_PLAYGROUNDAUTHOR">
                    	            <c:if test="${playgroundObject.editable}">
	    	                            <a href="playgroundobjecteditor.htm?id=${playgroundObject.id}"><c:out value="${playgroundObject.uri}"/></a>
	    	                        </c:if>
                    	            <c:if test="${not playgroundObject.editable}">
	    	                            <c:out value="${playgroundObject.uri}"/>
	    	                        </c:if>
    	                        </authz:authorize>
                                <c:if test="${playgroundObject.id == errId}">
	                                <div class="errorBox" />
	                                <c:forEach items="${err}" var="error">
	                                    <spring:message code="${error}"/><br/>
	                                </c:forEach>
	                                </div>
                                </c:if>
                            </td>
                            <td><c:out value="${playgroundObject.name}"/></td>
                            <td><spring:message code="category.${playgroundObject.category}"/></td>
                            <td><c:choose><c:when test="${playgroundObject.onMap}"><spring:message code="yes"/></c:when><c:otherwise><spring:message code="no"/></c:otherwise></c:choose>/<c:choose><c:when test="${playgroundObject.inDirectory}"><spring:message code="yes"/></c:when><c:otherwise><spring:message code="no"/></c:otherwise></c:choose></td>
                            <td><spring:message code="status.${playgroundObject.status}"/></td>
                            <td><fmt:formatDate value="${playgroundObject.lastModified}" pattern="dd MMM yyyy HH:mm" /></td>
                            <td><cyb:displayuser user="${playgroundObject.lastModifier}"/></td>
                            <td>
                            <authz:authorize ifAnyGranted="ROLE_PLAYGROUNDAUTHOR">
                                <a href="playgroundobjecteditor.htm?id=${playgroundObject.id}" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
                                <a href="deleteplaygroundobject.htm?id=${playgroundObject.id}" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a>
                            </authz:authorize>
                            <authz:authorize ifNotGranted="ROLE_PLAYGROUNDAUTHOR">
                                <c:if test="${playgroundObject.editable}">
                                    <a href="playgroundobjecteditor.htm?id=${playgroundObject.id}" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
                                    <a href="deleteplaygroundobject.htm?id=${playgroundObject.id}" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a>
                                </c:if>
                            </authz:authorize>
                            <a href="copyplaygroundobject.htm?id=${playgroundObject.id}" title="<spring:message code="copy"/>"><img src="themes/default/button_copy.gif"/></a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <cyb:pagernav pagedListHolder="${playgroundObjects}" lnk="${lnk}"/>
		<cyb:pager pagedListHolder="${playgroundObjects}" />
        </form>
        	<br/>
            <a href="<c:url value="playgroundobjecteditor.htm?playgroundId=${playground.id}"></c:url>" class="button" ><spring:message code="newplaygroundobject"/></a>
            <br/>
            <hr />
        <a href="<c:url value="playgroundlistpage.htm?forceRefresh=true"/>" class="button" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>
