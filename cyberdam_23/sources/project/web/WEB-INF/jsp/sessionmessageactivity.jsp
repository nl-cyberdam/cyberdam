<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="sessionmessageactivity.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="sessionmessageactivity.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
  <h1 class="activityname" ><c:out value="${messageActivity.name}"/></h1>
  <h2 class="activityinstructions" ><spring:message code="instructions"/></h2>
  <div class="activityinstructions" ><c:out value="${substitutedInstructions}" escapeXml="false"/></div>
  <h2 class="activityattachments" ><spring:message code="activity.attachments"/></h2>
  <c:forEach items="${messageActivity.sortedAttachments}" var="attachment" varStatus="varStatus" >
	  <c:url value="/resource" var="resourceurl" >
	  <c:param name="resourceId" value="${attachment.id}" />
	  </c:url>
	  <a href="${resourceurl}" target="_blank"> <c:out value="${attachment.name}" /></a>
	  <br/>
  </c:forEach>
  <c:if test="${empty messageActivity.attachments}" ><spring:message code="activity.no_attachments"/></c:if>
  <br/><br/>
    <form:form id="mainform">
        <div class="errors"><form:errors path="*" cssClass="errorBox" /></div>
        <table class="edit">
        <tr><td class="first_session"><spring:message code="message.from" /></td><td><c:out value="${from.roleAndPlayground}" /></td></tr>
        <tr><td><spring:message code="message.to" /></td><td><c:forEach items="${recipients}" var="recipient" varStatus="varStatus" ><c:out value="${recipient.roleAndPlayground}" /> <c:if test="${!varStatus.last}">, </c:if> </c:forEach></td></tr>
        <tr><td><spring:message code="message.subject" /></td><td><input name="subject" type="text"></input></td></tr>
        <tr><td><spring:message code="message.attachments" /></td><td><form:select path="attachments" items="${participant.sessionResourceBox.resources}" itemLabel="name" itemValue="id" ></form:select></td></tr>
        <tr><td></td><td><form:textarea path="body" id="body_textarea"/></td></tr>
        <cyb:richtext textareaid="body_textarea" />
       </table>
	      <input type="hidden" name="activityId" value="${messageActivity.id}" >
	      <input type="hidden" name="participantId" value="${participant.id}" >
	      <input type="submit" style="display:none" name="_sendmessage"/>
	      <input type="hidden" id="action"/>
  	    </form:form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.getElementById('action').name='_sendmessage';document.forms.mainform.submit()" class="button"><spring:message code="button.send"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>
