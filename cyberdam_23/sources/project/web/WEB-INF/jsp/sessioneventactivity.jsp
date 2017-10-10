<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="sessionactivity.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="sessionactivity.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
  <h1 class="activityname" ><c:out value="${activity.name}"/></h1>
  <h2 class="activityinstructions" ><spring:message code="instructions"/></h2>
  <div class="activityinstructions" ><c:out value="${substitutedInstructions}" escapeXml="false"/></div>
  <h2 class="activityattachments" ><spring:message code="activity.attachments"/></h2>
  <c:forEach items="${activity.sortedAttachments}" var="attachment" varStatus="varStatus" >
	  <c:url value="/resource" var="resourceurl" >
	  <c:param name="resourceId" value="${attachment.id}" />
	  </c:url>
	  <a href="${resourceurl}" target="_blank"> <c:out value="${attachment.name}" /></a>
	  <br/>
  </c:forEach>
  <c:if test="${empty activity.attachments}" ><spring:message code="activity.no_attachments"/><br /></c:if>
  <br />
  <form id="mainform">
  <input type="hidden" name="activityId" value="${activity.id}" >
  <input type="hidden" name="participantId" value="${participant.id}" >
  <input type="hidden" id="action"/>
  </form>
    <table class="noborders"><tr>
  	 <td><a href="#" onclick="document.getElementById('action').name='_ok';document.forms.mainform.submit()" class="button"><spring:message code="button.return"/></a></td>
  	 <td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
    </tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>
