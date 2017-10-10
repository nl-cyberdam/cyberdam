<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="useradministrator.htm" mlkey="useradministrator.title"/><cyb:csep /><cyb:crumb link="groupadministration.htm" mlkey="groupadministration.title"/><cyb:csep /><cyb:crumb mlkey="groupsettings.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="groupsettings.title"/> <c:out value="${command.name}" /></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="groupsettings.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
  <script>
  function addUser(name, id, roleid) {
  	//alert("User toevoegen: " + id);
  	var theForm = document.getElementById('adduser_hidden');

  	$('adduser_hidden_userid').setAttribute('value', id);
  	
  	
  	// close iframe and submit hidden form
  	theForm.submit();
  	Control.Modal.close();
  }
  </script>
        <form:form id="addgroup">
          <table class="edit">
            <tr><td colspan="2"><form:errors path="*" cssClass="errorBox" /></td></tr>

            <tr><td class="first"><form:label path="category"><spring:message code="category"/></form:label></td>
            <td><form:input path="category" /></td></tr>
            <tr><td colspan="2"><form:errors path="category" cssClass="error" /></td></tr>

            <tr><td><form:label path="name"><spring:message code="name"/></form:label></td>
            <td><form:input path="name" /></td></tr>
            <tr><td colspan="2"><form:errors path="name" cssClass="error" /></td></tr>

            <tr><td><form:label path="description"><spring:message code="description"/></form:label></td>
            <td><form:input path="description" /></td></tr>
            <tr><td colspan="2"><form:errors path="description" cssClass="error" /></td></tr>

            <tr><td><spring:message code="numberofmembers"/></td>
            <td><c:out value="${fn:length(command.members)}"/></td></tr>
        </table>
	      <input type="submit" style="display:none"/>
	      <input type="hidden" id="action"/>
  	    </form:form>
            <table class="usertable">
                <thead>
                    <tr class="title">
                        <th><spring:message code="firstname"/> <spring:message code="lastname"/></th>
                        <th><spring:message code="loginname"/></th>
                        <th><spring:message code="email"/></th>
                        <th><spring:message code="language"/></th>
                        <th><spring:message code="status"/></th>
                        <th><spring:message code="lastlogin"/></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${command.members}" var="user">
                        <tr>
                        	<td>
                        		<a href="<c:url value="edituser.htm"><c:param name="id" value="${user.id}"/></c:url>"><c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/></a>
                        	</td>
                        	<td><c:out value="${user.username}"/></td>
                        	<td><c:out value="${user.email}"/></td>
                        	<td><c:out value="${user.locale}"/></td>
                        	<td><spring:message code="status.${user.status}"/></td>
                        	<td><c:out value="${user.lastLogin}"/></td>
                        	<td>
                        		<c:url var="lnk" value="deleteuserfromgroup.htm"><c:param name="groupId" value="${command.id}"/><c:param name="userId" value="${user.id}"/></c:url>
                        		<a href="${lnk}" style="margin:0" class="button"><spring:message code="button.removeuser"/></a>
                        	</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
           
			<form id="adduser_hidden" action="addusertogroup.htm">
            <input type="hidden" name="groupId" value="${command.id}">
            <input id="adduser_hidden_userid" type="hidden" name="userId" value="-1">
            </form>
			
	    <table class="noborders"><tr>
			<td><a href="browseusers.htm?forceRefresh=true&sort.property=lastName" id="modal_link_browseusers" class="button"><spring:message code="button.adduser"/></a></td></tr>
			<tr>
			<td><a href="#" onclick="document.forms.addgroup.submit()" class="button"><spring:message code="button.save"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.addgroup.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
<script>
		new Control.Modal('modal_link_browseusers',{
			iframe: true,
			width: 800,
			height: 480
		});
</script>

			<hr/>
			<a href="<c:url value="groupadministration.htm"/>" class="back button" ><spring:message code="link.back.useradmin"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>