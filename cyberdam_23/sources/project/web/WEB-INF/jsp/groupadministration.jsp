<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="useradministrator.htm" mlkey="useradministrator.title"/><cyb:csep /><cyb:crumb mlkey="groupadministration.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="groupadministration.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="groupadministration.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <form id="mainform" action="" method="POST">
            <table class="grouptable">
                <thead>
                    <tr class="title">
                        <th><cyb:sortlink lnk="${lnk}" sort="category"><spring:message code="category"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="name"><spring:message code="name"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="description"><spring:message code="description"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="numberOfMembers"><spring:message code="numberofmembers"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="lastModified"><spring:message code="lastmodified"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="lastModifiedBy"><spring:message code="lastmodifiedby"/></cyb:sortlink></th>
                    </tr>
                    <tr class="title">
                        <td><spring:message code="filter"/>:</td><td>
                            <input type="text" name="filter.name" value="<c:out value="${groups.filter.name}"/>"
                                   onchange="document.forms.mainform.submit()" /><a href="?forceRefresh=true"><img src="images/refresh.png" alt=<spring:message code="forcerefresh"/> /></a>
                        </td>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty groups.pageList}">
                	<tr><td colspan="6" ><spring:message code="noitemsfound.groups" /></td></tr>
                	</c:if>
                    <c:forEach items="${groups.pageList}" var="group">
                        <tr><td><a href="<c:url value="groupsettings.htm"><c:param name="id" value="${group.id}"/></c:url>"><c:out value="${group.category}"/></a></td>
                        <td><c:out value="${group.name}"/></td>
                        <td><c:out value="${group.description}"/></td>
                        <td><c:out value="${group.numberOfMembers}"/></td>
                        <td><fmt:formatDate value="${group.lastModified}" pattern="dd MMM yyyy HH:mm" /></td>
                        <td><cyb:displayuser user="${group.lastModifiedBy}"/></td>
                        <td class="icons"><a href="<c:url value="groupsettings.htm"><c:param name="id" value="${group.id}"/></c:url>" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
                        	<a href="<c:url value="deletegroup.htm"><c:param name="id" value="${group.id}"/></c:url>" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a></td></tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <cyb:pagernav pagedListHolder="${groups}" lnk="${lnk}"/>
		<cyb:pager pagedListHolder="${groups}" />
        </form>
        
        <div>
            <a href="<c:url value="groupsettings.htm"/>" class="button"><spring:message code="addgroup"/></a>
        </div>
        <hr />
        <a href="<c:url value="useradministrator.htm"/>" class="button" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>