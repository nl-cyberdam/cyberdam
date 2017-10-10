<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep/><cyb:crumb mlkey="swfsdirectory.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="swfsdirectory.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="swfsdirectory.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
		<form action="" method="POST">
            <table class="usertable">
                <thead>
                    <tr class="title">
                        <th><cyb:sortlink lnk="${lnk}" sort="name"><spring:message code="file.name" /></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="date"><spring:message code="date" /></cyb:sortlink></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${swfs.pageList}" var="swf">
                        <tr>
                        <td><c:out value="${swf.name}"/></td>
                        <td><fmt:formatDate value="${swf.date}" pattern="dd MMM yyyy HH:mm" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
			<cyb:pagernav pagedListHolder="${swfs}" lnk="${lnk}"/>
		<cyb:pager pagedListHolder="${swfs}" />
        </form>
        <c:url value="swfupload.htm" var="newswflink"></c:url>
		<a href="${newswflink}" class="button" ><spring:message code="link.addnewswf"/></a>
		<hr />
		<a class="back button" href="<c:url value="systemadministration.htm"/>"><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>