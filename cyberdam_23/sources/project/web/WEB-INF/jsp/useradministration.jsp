<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="useradministrator.htm" mlkey="useradministrator.title"/><cyb:csep /><cyb:crumb mlkey="useradministration.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="useradministration.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="useradministration.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <form id="mainform" action="" method="POST">
	        <%@ include file="textfilter.jsp" %>
            <table class="usertable">
                <thead>
                    <tr class="title">
                        <th><cyb:sortlink lnk="${lnk}" sort="firstName"><spring:message code="firstname"/></cyb:sortlink> <cyb:sortlink lnk="${lnk}" sort="lastName"><spring:message code="lastname"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="username"><spring:message code="username"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="email"><spring:message code="email"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="locale.language"><spring:message code="language"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="status"><spring:message code="status"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="groupsAsCSV"  query="users=${param.users}"><spring:message code="groups"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="lastLogin"><spring:message code="lastlogin"/></cyb:sortlink></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${users.pageList}" var="user">
                    <authz:authorize ifNotGranted="ROLE_SYSTEMADMINISTRATOR">
                    	<c:if test="${!user.userAdministrator && !user.systemAdministrator}">
	                        <tr>
		                        <td><a href="<c:url value="edituser.htm"><c:param name="id" value="${user.id}"/></c:url>"><c:out value="${user.firstName} ${user.lastName}"/></a></td>
		                        <td><c:out value="${user.username}"/></td>
		                        <td><c:out value="${user.email}"/></td>
		                        <td><c:out value="${user.locale}"/></td>
		                        <td><spring:message code="status.${user.status}"/></td>
		                        <td><c:out value="${user.groupsAsCSV}"/></td>
		                        <td><fmt:formatDate value="${user.lastLogin}" pattern="dd MMM yyyy HH:mm" /></td>
		                        <td class="icons"><a href="<c:url value="edituser.htm"><c:param name="id" value="${user.id}"/></c:url>" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
		                        	<a href="<c:url value="deleteuser.htm"><c:param name="id" value="${user.id}"/></c:url>" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a></td>
	                        </tr>
                        </c:if>
                    </authz:authorize>
                    <authz:authorize ifAnyGranted="ROLE_SYSTEMADMINISTRATOR">
                    	<tr>
		                        <td><a href="<c:url value="edituser.htm"><c:param name="id" value="${user.id}"/></c:url>"><c:out value="${user.firstName} ${user.lastName}"/></a></td>
		                        <td><c:out value="${user.username}"/></td>
		                        <td><c:out value="${user.email}"/></td>
		                        <td><c:out value="${user.locale}"/></td>
		                        <td><spring:message code="status.${user.status}"/></td>
		                        <td><c:out value="${user.groupsAsCSV}"/></td>
		                        <td><fmt:formatDate value="${user.lastLogin}" pattern="dd MMM yyyy HH:mm" /></td>
		                        <td class="icons"><a href="<c:url value="edituser.htm"><c:param name="id" value="${user.id}"/></c:url>" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
		                        	<a href="<c:url value="deleteuser.htm"><c:param name="id" value="${user.id}"/></c:url>" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a></td>
	                        </tr>
                    </authz:authorize>
                    	
                    </c:forEach>
                </tbody>
            </table>
            
            <cyb:pagernav pagedListHolder="${users}" lnk="${lnk}" />
        <cyb:pager pagedListHolder="${users}" />
        </form>
        
        <div>
            <a href="<c:url value="edituser.htm"/>" class="button"><spring:message code="adduser"/></a>
        </div>
        <hr />
        <div>
            <h2><spring:message code="uploadmultipleusers"/></h2>
            <form id="uploadform" action="uploadusers.htm" method="post" enctype="multipart/form-data">
        <table>
		<tr>
			<td>
				<spring:message code="file" />:
			</td>
			<td>
				<input type="file" name="file">
			</td>
		</tr>
            </table>
	      <input type="submit" style="display:none"/>
        </form>         
        </div>
	    <a href="#" onclick="document.forms.uploadform.submit()" class="button"><spring:message code="button.upload"/></a>
        <hr/>
        <a class="back button" href="<c:url value="useradministrator.htm"/>"><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>