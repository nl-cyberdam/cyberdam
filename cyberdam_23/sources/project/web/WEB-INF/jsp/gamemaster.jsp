<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb mlkey="gamemaster.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gamemaster.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gamemaster.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <form id="mainform" action="" method="POST">
        <%@ include file="textfilter.jsp" %>
            <table class="usertable">
                <thead>
                    <tr class="title">
                        <th><cyb:sortlink lnk="${lnk}" sort="name"><spring:message code="session.name"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="status"><spring:message code="status"/></cyb:sortlink></th>
                        <th><cyb:sortlink lnk="${lnk}" sort="currentStatusStarted"><spring:message code="currentStatusStarted"/></cyb:sortlink></th>
                        <th><spring:message code="numberofroles"/></th>
                        <th><spring:message code="numberofparticipants"/></th>
                        <th><spring:message code="currentStepOfPlay"/></th>
                        <th><spring:message code="activities"/> (<spring:message code="uncompleted"/>/<spring:message code="total"/>)</th>
                        <th><cyb:sortlink lnk="${lnk}" sort="owner"><spring:message code="gamesession.owner"/></cyb:sortlink></th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${gamesessions.nrOfElements == 0}">
                	<tr><td colspan="8" ><spring:message code="noitemsfound.gamesessions" /></td></tr>
                	</c:if>
                    <c:forEach items="${gamesessions.pageList}" var="gamesession">
                        <tr>
                            <td>
                            	<c:if test="${gamesession.editable}"><a href="<c:url value="gamesessioncontrol.htm"><c:param name="id" value="${gamesession.id}" /></c:url>"><c:out value="${gamesession.name}"/></a></c:if>
                            	<c:if test="${not gamesession.editable}"><c:out value="${gamesession.name}"/></c:if>
                           	</td>
                            <td><spring:message code="status.${gamesession.status}"/></td>
                            <td><fmt:formatDate value="${gamesession.currentStatusStarted}" pattern="dd MMM yyyy HH:mm" /></td>
                            <td><c:out value="${fn:length(gamesession.manifest.gameModel.roles)}"/></td>
                            <td><c:out value="${gamesession.totalUsers}"/></td>
                            <td><c:out value="${gamesession.currentStepOfPlay}"/></td>
                            <td><c:out value="${gamesession.nrOfActivitiesForCurrentStep - gamesession.nrOfCompletedActivities}" />/<c:out value="${gamesession.nrOfActivitiesForCurrentStep}" /></td>
                            <td><cyb:displayuser user="${gamesession.owner}" /></td>
                            <td class="icons">
                            <c:if test="${gamesession.editable}"><a href="<c:url value="gamesessioncontrol.htm"><c:param name="id" value="${gamesession.id}" /></c:url>" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a></c:if>
                            <c:if test="${gamesession.editable}"><a href="<c:url value="deletegamesession.htm"><c:param name="id" value="${gamesession.id}" /></c:url>" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a></c:if>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <cyb:pagernav pagedListHolder="${gamesessions}" lnk="${lnk}"/>
		<cyb:pager pagedListHolder="${gamesessions}" />
        </form><br/>
        <a href="<c:url value="creategamesession.htm"></c:url>" class="button" ><spring:message code="link.new.session"/></a>
        <br/><hr /><br/>
        <div>
            <h2><spring:message code="uploadmultipleusers"/></h2>
            <form id="uploadform" action="uploadusers.htm" method="post" enctype="multipart/form-data">
            <input type="hidden" name="brief" value="1" />
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
      	<a class="back button" href="<c:url value="index.htm"/>" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>
