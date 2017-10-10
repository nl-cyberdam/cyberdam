<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">

<tiles:putAttribute name="breadcrumb" >
    <cyb:crumb link="index.htm" mlkey="homepage.title"/>
    <cyb:csep/>
    <cyb:crumb link="gamemaster.htm" mlkey="gamemaster.title"/>
    <cyb:csep/>
	<c:url value="gamesessioncontrol.htm" var="gamesessioncontrol">
	    <c:param name="id" value="${param.id}"/>
	</c:url>
    <cyb:crumb link="${gamesessioncontrol}" mlkey="gamesessioncontrol.title"/>
    <cyb:csep/>
    <cyb:crumb mlkey="sessionreport.title"/>
</tiles:putAttribute>

<tiles:putAttribute name="title" > <spring:message code="sessionreport.title"/> <c:out value="${sessionName}" /> </tiles:putAttribute>

<tiles:putAttribute name="introduction">
    <spring:message code="sessionreport.introduction"/>
</tiles:putAttribute>

<tiles:putAttribute name="content">

    <c:url var="lnk" value="sessionreport.htm">
        <c:param name="id" value="${param.id}"/>
    </c:url>

        <form action="" method="POST">
            <table class="usertable">
                <thead>
                    <tr class="title">
                        <th><cyb:sortlink lnk="${lnk}" sort="activity.name"><spring:message code="activity.name"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="userName"><spring:message code="game.participant"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="role.name"><spring:message code="role"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="date"><spring:message code="date"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="stepOfPlay"><spring:message code="stepOfPlay"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="type"><spring:message code="type"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="fileName"><spring:message code="fileName"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="receiver"><spring:message code="message.to"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="subject"><spring:message code="subject"/></cyb:sortlink></th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${sessionreportlist.nrOfElements == 0}">
                    <tr><td colspan="9"><spring:message code="noitemsfound.sessionreport" /></td></tr>
                    </c:if>
                    <c:forEach items="${sessionreportlist.pageList}" var="sessionreport">
						<tr>
							<td>
							<c:choose>
							<c:when test="${empty sessionreport.activity}">
								<c:set var="activityName"><spring:message code="user.initiated"/></c:set>
							</c:when>
							<c:otherwise>
								<c:set var="activityName" value="${sessionreport.activity.name}" />
							</c:otherwise>
							</c:choose>
							<c:choose>
							<c:when test="${sessionreport.resourceId != null}">
								<c:url value="sessionactivityviewer.htm" var="sessionactivityviewer">
								<c:param name="id" value="${param.id}"/>
								<c:param name="sessionResourceId" value="${sessionreport.resourceId}"/></c:url>
								<a href="${sessionactivityviewer}"><c:out value="${activityName}"/></a>
							</c:when>
							<c:when test="${sessionreport.messageId != null}">
								<c:url value="sessionactivityviewer.htm" var="sessionactivityviewer">
								<c:param name="id" value="${param.id}"/>
								<c:param name="messageId" value="${sessionreport.messageId}"/></c:url>
								<a href="${sessionactivityviewer}"><c:out value="${activityName}"/></a>
							</c:when>
							<c:otherwise><c:out value="${activityName}"/></c:otherwise>
							</c:choose>
							</td>
                            <td><c:out value="${sessionreport.userName}"/></td>
                            <td><c:out value="${sessionreport.role.name}"/></td>
                            <td><c:out value="${sessionreport.date}"/></td>
                            <td><c:out value="${sessionreport.stepOfPlay}"/></td>
                            <td><spring:message code="activitytype.${sessionreport.type}"/></td>
                            <td><c:out value="${sessionreport.fileName}"/></td>
                            <td><c:out value="${sessionreport.receiver}"/></td>
                            <td><c:out value="${sessionreport.subject}"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <cyb:pagernav pagedListHolder="${sessionreportlist}" lnk="${lnk}"/>
        <cyb:pager pagedListHolder="${sessionreportlist}" />
        </form>
    <c:url value="printablesessionreport.htm" var="printablesessionreport">
        <c:param name="id" value="${param.id}"/>
    </c:url>
    <a href="${printablesessionreport}" class="button"><spring:message code="link.print.sessionreport"/></a>
    <br/>
    <a href="${gamesessioncontrol}" class="button"><spring:message code="link.back"/></a>
</tiles:putAttribute>

</tiles:insertTemplate>
