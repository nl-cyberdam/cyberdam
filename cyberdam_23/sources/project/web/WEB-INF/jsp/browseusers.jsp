<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_popup.jsp">
  <tiles:putAttribute name="content">
  	<c:set var="lnk" ><c:url value=""><c:param name="users" value="${param.users}" /></c:url></c:set>
          <form id="mainform" action="" method="POST">
	        <%@ include file="textfilter.jsp" %>
            <table class="usertable">
                <thead>
                    <tr class="title">
                        <th><cyb:sortlink lnk="${lnk}" sort="firstName"><spring:message code="firstname"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="lastName"><spring:message code="lastname"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="email"><spring:message code="email"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="groupsAsCSV"><spring:message code="groups"/></cyb:sortlink></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${userslist.pageList}" var="user">
                        <tr><td><c:out value="${user.firstName}"/></td>
                            <td><c:out value="${user.lastName}"/></td>
                            <td><c:out value="${user.email}"/></td>
                            <td><c:out value="${user.groupsAsCSV}"/></td>
                            <td><a href="" onClick="parent.addUser('${user.firstName} ${user.lastName}', ${user.id}, '${param.users}')" class="button" style="margin:0"><spring:message code="choose"/></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <cyb:pagernav pagedListHolder="${userslist}" lnk="${lnk}" />
        <cyb:pager pagedListHolder="${userslist}" />
        </form>
  </tiles:putAttribute>
</tiles:insertTemplate>
