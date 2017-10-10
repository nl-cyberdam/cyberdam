<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="sessionprogressactivity.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="sessionprogressactivity.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
  <h1 class="activityname" ><c:out value="${progressActivity.name}"/></h1>
  <h2 class="activityinstructions" ><spring:message code="instructions"/></h2>
  <div class="activityinstructions" ><c:out value="${substitutedInstructions}" escapeXml="false"/></div>
  <h2 class="activityattachments" ><spring:message code="activity.attachments"/></h2>
  <c:forEach items="${progressActivity.sortedAttachments}" var="attachment" varStatus="varStatus" >
	  <c:url value="/resource" var="resourceurl" >
	  <c:param name="resourceId" value="${attachment.id}" />
	  </c:url>
	  <a href="${resourceurl}" target="_blank"> <c:out value="${attachment.name}" /></a>
	  <br/>
  </c:forEach>
  <c:if test="${empty progressActivity.attachments}" ><spring:message code="activity.no_attachments"/><br /></c:if>
  <br />
  <h2><spring:message code="progressactivity.options" /></h2>
  <form id="mainform">
  <c:forEach items="${progressActivity.filteredNextStepOfPlayOptions}" var="nextstepoption" varStatus="varStatus">
    <input type="radio" name="nextstep" value="${nextstepoption.step.id}" id="option${nextstepoption.step.id}" <c:if test="${varStatus.index == 0}" >checked="checked"</c:if>>
    <label style="line-height:20px;display:inline" for="option${nextstepoption.step.id}">&nbsp;${nextstepoption.description}</label><br/>
  </c:forEach>
  <input type="hidden" name="activityId" value="${progressActivity.id}" >
  <input type="hidden" name="participantId" value="${participant.id}" >
  <input type="hidden" id="action"/>
  </form>
    <table class="noborders"><tr>
  <c:if test="${!empty progressActivity.filteredNextStepOfPlayOptions}">
  	 <td><a href="#" onclick="document.getElementById('action').name='_nextstep';document.forms.mainform.submit()" class="button"><spring:message code="goto.next.step"/></a></td>
  </c:if>
  	 <td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
    </tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>