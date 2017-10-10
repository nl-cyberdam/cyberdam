<%@ include file="includes.jsp" %>
    <div id="statusText">
    	${statusText}
    </div>
    <hr />
    <menu-el:useMenuDisplayer name="ListMenu" permissions="rolesAdapter"
	repository="menurepository">
		<c:forEach var="menuitem" items="${menurepository.topMenus}">
			<menu-el:displayMenu name="${menuitem.name}" />
		</c:forEach>
	</menu-el:useMenuDisplayer>     
    <div class="listofcontacts" >
    <h3><spring:message code="listofcontacts"/></h3>
	<ul>
	<c:forEach items="${participant.gameSession.participants}" var="sessionparticipant" >
	<c:url value="playgroundobject.htm" var="playgroundobjectlink" ><c:param name="playgroundObjectId" value="${sessionparticipant.roleAndPlayground.playgroundObject.id}"/><c:param name="participantId" value="${participant.id}"/></c:url>
	<li><c:out value="${sessionparticipant.roleAndPlayground.role.name}" /> 
        <c:if test="${!empty sessionparticipant.roleAndPlayground.playgroundObject.name}">(<a href="${playgroundobjectlink}" onclick="link_popup(this,'width=800,height=800,resizable=yes,scrollbars=yes'); return false;"><c:out value="${sessionparticipant.roleAndPlayground.playgroundObject.name}" /></a><spring:message code="separator.playground"/><c:out value="${sessionparticipant.roleAndPlayground.playgroundObject.playground.name}" />)</c:if>
    </li>
	</c:forEach>
	</ul>
	</div>
    <p>
    <a href="gameparticipant.htm"><spring:message code="back.gameparticipant"/></a>
    </p>