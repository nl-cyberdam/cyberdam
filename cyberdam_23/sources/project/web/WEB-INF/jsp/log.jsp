<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep/><cyb:crumb mlkey="log.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="log.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="log.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <form action="" method="POST" id="mainform">
        <%@ include file="textfilter.jsp" %>
            <table class="usertable">
                <thead>
                    <tr class="title">
                        <th><cyb:sortlink lnk="${lnk}" sort="module"><spring:message code="module" /></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="action"><spring:message code="action" /></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="parameter"><spring:message code="parameter" /></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="date"><spring:message code="date" /></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="user"><spring:message code="user" /></cyb:sortlink></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${logentries.pageList}" var="logentry">
                        <tr>
                        <td><c:out value="${logentry.module}"/></td>
                        <td><c:out value="${logentry.action}"/></td>
                        <td><c:out value="${logentry.parameter}"/></td>
                        <td><fmt:formatDate value="${logentry.date}" pattern="dd MMM yyyy HH:mm" /></td>
                        <td><c:out value="${logentry.userString}"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
			<cyb:pagernav pagedListHolder="${logentries}" lnk="${lnk}"/>
		<cyb:pager pagedListHolder="${logentries}" />
        </form>
        <hr />
		<a class="back button" href="<c:url value="systemadministration.htm"/>"><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>