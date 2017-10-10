<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_popup.jsp">
  <tiles:putAttribute name="content">
	<div class="errorBox"><spring:message code="manifest.warning.gamemodel"/></div><br/>
		<form id="mainform" action="" method="POST">
		<%@ include file="textfilter.jsp" %>
            <table class="usertable">
                <thead>
                    <tr class="title">
                        <th><cyb:sortlink lnk="" sort="name"><spring:message code="gamemodel.name"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="" sort="status"><spring:message code="status"/></cyb:sortlink></th>
                    </tr>
                </thead>
                <tbody>
                	<c:if test="${gamemodelslist.nrOfElements == 0}">
                	<tr><td colspan="2" ><spring:message code="noitemsfound.gamemodels" /></td></tr>
                	</c:if>
                    <c:forEach items="${gamemodelslist.pageList}" var="gamemodel">
                        <tr><td><c:out value="${gamemodel.name}"/></td>
                            <td><spring:message var="status_i18n" code="status.${gamemodel.status}"/><c:out value="${status_i18n}"/></td>
                            <td class="button"><a href="" onClick="parent.selectGameModel(${gamemodel.id}, '${status_i18n}')" class="button" style="margin:0"><spring:message code="choose" /></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <cyb:pagernav pagedListHolder="${gamemodelslist}" lnk="" />
        <cyb:pager pagedListHolder="${gamemodelslist}" />
        </form>
  </tiles:putAttribute>
</tiles:insertTemplate>
