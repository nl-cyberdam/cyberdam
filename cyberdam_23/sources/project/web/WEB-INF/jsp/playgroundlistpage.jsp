<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb mlkey="playgroundlistpage.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="playgroundlistpage.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="playgroundlistpage.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <form id="mainform" action="" method="POST">
        <c:if test="${param.no_swf_found!=null}">
        	<div class="errorBox"><spring:message code="no_swf_found" /></div>
        </c:if>
        <%@ include file="textfilter.jsp" %>
            <table class="usertable">
                <thead>
                    <tr class="title">
                        <th><cyb:sortlink lnk="${lnk}" sort="name"><spring:message code="playground.name"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="caption"><spring:message code="caption"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="uriId"><spring:message code="uri"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="lastModified"><spring:message code="lastmodified"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="lastModifier"><spring:message code="by"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="status"><spring:message code="status"/></cyb:sortlink></th>
                    </tr>
                </thead>
                <tbody>
                	<c:if test="${playgrounds.nrOfElements == 0}">
                	<tr><td colspan="7" ><spring:message code="playgroundlistpage.noitemsfound" /></td></tr>
                	</c:if>
                    <c:forEach items="${playgrounds.pageList}" var="playground">
                        <tr>
                            <td>        
                            	<authz:authorize ifAnyGranted="ROLE_PLAYGROUNDAUTHOR">
                                    <a href="playgrounddetail.htm?id=${playground.id}"><c:out value="${playground.name}"/></a>
                            	</authz:authorize>
                            	<authz:authorize ifNotGranted="ROLE_PLAYGROUNDAUTHOR">
                                	<c:if test="${playground.editable}">
                                    <a href="playgrounddetail.htm?id=${playground.id}"><c:out value="${playground.name}"/></a>
                                    </c:if>
                                    <c:if test="${not playground.editable}">
                                    <c:out value="${playground.name}"/>
                                    </c:if>
                                </authz:authorize>
                            </td>
                            <td><c:out value="${playground.caption}"/></td>
                        	<td><c:out value="${playground.uriId}"/></td>
                        	<td><fmt:formatDate value="${playground.lastModified}" pattern="dd MMM yyyy HH:mm" /></td>
                            <td><cyb:displayuser user="${playground.lastModifier}"/></td>
                            <td><spring:message code="status.${playground.status}"/></td>
                            <td class="icons">
                            <authz:authorize ifAnyGranted="ROLE_PLAYGROUNDAUTHOR">
                                <a href="playgrounddetail.htm?id=${playground.id}" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
                            </authz:authorize>
                            <authz:authorize ifNotGranted="ROLE_PLAYGROUNDAUTHOR">
                                <c:if test="${playground.editable}">
                                    <a href="playgrounddetail.htm?id=${playground.id}" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
                                    <a href="deleteplayground.htm?id=${playground.id}" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a>
                                </c:if>
                            </authz:authorize>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <cyb:pagernav pagedListHolder="${playgrounds}" lnk="${lnk}"/>
		<cyb:pager pagedListHolder="${playgrounds}" />
        </form>
        	<authz:authorize ifAnyGranted="ROLE_SYSTEMADMINISTRATOR,ROLE_LCMSADMININSTRATOR">
        	<br/>
        	<table class="noborders"><tr>
            	<td><a href="<c:url value="playgrounddetail.htm"></c:url>" class="button" ><spring:message code="playground.new"/></a></td>
            	<td><a href="<c:url value="playgroundimport.htm"></c:url>" class="button" ><spring:message code="playground.import"/></a></td>
           	</tr></table>
        	<br/>
            </authz:authorize>
        <hr />
        <a href="<c:url value="index.htm"/>" class="button" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>