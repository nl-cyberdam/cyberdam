<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="sendmessage.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="sendmessage.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
    <form:form id="mainform">
        <div class="errors"><form:errors path="*" cssClass="errorBox" /></div>
        <table class="edit">
        <tr><td class="first_session"><spring:message code="message.from" /></td><td><c:out value="${from.roleAndPlayground}" /></td></tr>
        <tr><td><spring:message code="message.to" /></td><td><form:select path="recipients" multiple="true" cssStyle="width: 20em;">
                <form:options items="${recipientOptions}" itemLabel="roleAndPlayground" itemValue="id" />
            </form:select></td></tr>
        <tr><td><spring:message code="message.subject" /></td><td><form:input path="subject" size="60"/></td></tr>
        <tr><td><spring:message code="message.attachments" /></td><td><form:select path="attachments" multiple="true" cssStyle="width: 20em;">
                <form:options items="${attachmentOptions}" itemLabel="name" itemValue="id" />
            </form:select></td></tr>
        <tr><td></td><td><form:textarea path="body" id="body_textarea"/></td></tr>
        <cyb:richtext textareaid="body_textarea" />
        </table>
	      <input type="submit" style="display:none"/>
	      <input type="hidden" id="action"/>
  	    </form:form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.send"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>
