<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_playgroundwindow.jsp">
  <tiles:putAttribute name="title" >${playground.name} <spring:message code="playgrounddirectory.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="playgrounddirectory.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
  				<c:set var="lnk" ><c:url value="playgrounddirectory.htm"><c:param name="participantId" value="${param.participantId}" /><c:param name="playgroundId" value="${param.playgroundId}" /></c:url></c:set>
                <c:set var="refreshLink" ><c:url value="playgrounddirectory.htm"><c:param name="participantId" value="${param.participantId}" /><c:param name="playgroundId" value="${param.playgroundId}" /><c:param name="forceRefresh" value="true"/></c:url></c:set><%-- param is an implicit el variable --%>
                <form action="" method="POST" name="mainform">
		        <%@ include file="textfilter.jsp" %>
                <table class="usertable">
                    <thead>
                        <tr class="title">
                            <th><cyb:sortlink lnk="${lnk}" sort="name"><spring:message code="name" /></cyb:sortlink></th>
                            <th><cyb:sortlink lnk="${lnk}" sort="category"><spring:message code="category"/></cyb:sortlink></th>
                            <th><cyb:sortlink lnk="${lnk}" sort="caption"><spring:message code="caption"/></cyb:sortlink></th>
                            <th><cyb:sortlink lnk="${lnk}" sort="address"><spring:message code="address"/></cyb:sortlink></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${playgroundobjects.pageList}" var="playgroundobject">
                            <c:url value="playgroundobject.htm" var="poUrl">
                                <c:param name="playgroundObjectId" value="${playgroundobject.id}"/>
                                <c:param name="participantId" value="${participant.id}"/>
                            </c:url>
                            <tr>
                                <td><a href="${poUrl}"><c:out value="${playgroundobject.name}"/></a></td>
                                <td><spring:message code="category.${playgroundobject.category}"/></td>
                                <td><c:out value="${playgroundobject.caption}"/></td>
                                <td><c:out value="${playgroundobject.address}"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <cyb:pagernav pagedListHolder="${playgroundobjects}" lnk="${lnk}" />
		<cyb:pager pagedListHolder="${playgroundobjects}" />
        </form>
  </tiles:putAttribute>
</tiles:insertTemplate>
