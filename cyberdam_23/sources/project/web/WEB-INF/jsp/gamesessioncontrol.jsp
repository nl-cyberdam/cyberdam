<%@ include file="includes.jsp" %>
<%@page import="nl.cyberdam.domain.GameSession;"%>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gamemaster.htm" mlkey="gamemaster.title"/><cyb:csep/><cyb:crumb mlkey="gamesessioncontrol.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gamesessioncontrol.title"/> <c:out value="${command.name}" /></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gamesessioncontrol.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
  <script>
  /* check all users - if there is only one delete marker it should be hidden (it is not ok to 
     delete the last user
   */
  function check() {
    var users = $$(".usersforrolemarker");
    if(users) {
        users.each(function (e) {
	    	var links = $(e).getElementsByClassName("deletelinkmarker");
	    	// console.log(links);
	    	// console.trace("users: " + users.length + " links: " + links.length)
	    	var single = (links)? links.length < 2: false;
			// console.log("single: " + single);
        	if (single) {
        		Element.hide(links[0]);
        	} else {
        		Element.show(links[0]);
        	}  
    	});
    }
  }
  function addUser(name, id, roleid) {
  	//alert("User toevoegen: " + id);
  	var theForm = document.getElementById('mainform');
  	//alert(Form.getElements(theForm))
  	
  	// new Insertion.Bottom(roleid, '<p><input type="hidden" name="'+roleid+'" value="'+id+'" />user: '+name+'</p>');
  	new Insertion.Bottom(roleid, '<div class="user usermarker" ><input type="hidden" value="'+id+'" name="'+roleid+'"/>'+name+' <a href="#" class="deletelinkmarker" onclick="Element.remove(this.ancestors()[0]); check(); return false;" title="<spring:message code="deleteparticipant" />"><img src="themes/default/button_delete.gif"/></a></div>');
  	// var theform = Element.getHeight(my_div);
  	
  	// close iframe
  	Control.Modal.close();
  	check();
  }
  window.onload=check;
  </script>
        <form:form id="mainform" >
        <table class="edit">
            <tr><td colspan="2">
            <form:errors path="*" cssClass="errorBox" />
            </td></tr>
            
            <tr>
            <td class="first"><spring:message code="session.name"/></td>
            <td><form:input path="name" /></td></tr>

            <tr>
            <td><spring:message code="status"/></td>
            <td><spring:message code="status.${command.status}"/></td></tr>

            <tr>
            <td><spring:message code="currentstatusstarted"/></td>
            <td><fmt:formatDate value="${command.currentStatusStarted}" pattern="dd MMM yyyy HH:mm" /></td></tr>

            <tr>
            <td><spring:message code="runningstarted"/></td>
            <td><fmt:formatDate value="${command.runningStarted}" pattern="dd MMM yyyy HH:mm" /></td></tr>

            <tr>
            <td><spring:message code="sessionstopped"/></td>
            <td><fmt:formatDate value="${command.sessionStopped}" pattern="dd MMM yyyy HH:mm" /></td></tr>

            </table>
			<br/>
            <table>
            <tr><th><spring:message code="role"/></th><th><spring:message code="playgroundcharactername"/></th><th><spring:message code="users"/></th></tr>
            
            <c:forEach items="${command.participants}" var="roleMapping" varStatus="rowCounter">

                    <tr>
                    	<td><c:out value="${roleMapping.roleAndPlayground.role.name}" /></td>
                    	<td><c:out value="${roleMapping.roleAndPlayground.playgroundObject.name}"/>, <c:out value="${roleMapping.roleAndPlayground.playground.name}"/></td>
                    <td id="participants[${rowCounter.index}].users" class="usersforrolemarker" >
                      <input type="hidden" name="_participants[${rowCounter.index}].users" value="1"/>

                      <c:forEach items="${roleMapping.users}" var="user" varStatus="usersRowCounter">
                        <div class="user usermarker" id="p${rowCounter.index}u${usersRowCounter.index}" >
                        	<input type="hidden" value="${user.id}" name="participants[${rowCounter.index}].users"/>
                        	<cyb:displayuser user="${user}"/>
                        	<c:if test="${command.status == 'IN_PREPARATION' or command.status == 'RUNNING'}">
                        		<%-- <c:if test="${command.owner != user}"> --%>
                        			<a href="#" class="deletelinkmarker" onclick="Element.remove('p${rowCounter.index}u${usersRowCounter.index}'); check(); return false;" title="<spring:message code="deleteparticipant" />"><img src="themes/default/button_delete.gif"/></a>
                        		<%-- </c:if> --%>
                        	</c:if>
                        </div>
                      </c:forEach>
                    </td>
                    <td><spring:message code="participant.attributes"/></td>
	         	   <spring:bind path="participants[${rowCounter.index}]">
	         	   		<td>
                    		<form:input path="${status.expression}.value1" />
                    		<form:input path="${status.expression}.value2" />
                    		<form:input path="${status.expression}.value3" /><br/>
                    		<form:input path="${status.expression}.value4" />
                    		<form:input path="${status.expression}.value5" /><br/>
                    	</td>
                   	</spring:bind>
                    <td class="icons">
                      <c:if test="${command.status == 'IN_PREPARATION' or command.status == 'RUNNING' }"><a href="browseusers.htm?users=participants[${rowCounter.index}].users&forceRefresh=true" id="modal_link_browseusers${rowCounter.index}" title="<spring:message code="addparticipant"/>"><img src="themes/default/button_adduser.gif"/></a></c:if>
                      <c:if test="${command.status == 'RUNNING' or command.status == 'FINISHED' or command.status == 'ABORTED'}"><a href="session.htm?participantId=${roleMapping.id}" title="<spring:message code="monitor"/>"><img src="themes/default/button_home.gif"/></a></c:if>
                    </td>
                    </tr>
<script>
		new Control.Modal('modal_link_browseusers${rowCounter.index}',{
			iframe: true,
			width: 800,
			height: 480
		});
</script>
            </c:forEach>
            </table>
            <authz:authorize ifAnyGranted="ROLE_GAMESESSIONMASTER,ROLE_SYSTEMADMINISTRATOR">
	            <br/>
	            <c:url value="sessionreport.htm" var="sessionreport">
	                <c:param name="id" value="${param.id}"/>
	            </c:url>
	            <table class="noborders"><tr>
	            <td><a href="${sessionreport}" class="button"><spring:message code="link.sessionreport"/></a></td>
	            <td><a href="<c:url value="copygamesession.htm"><c:param name="id" value="${param.id}" /></c:url>" class="button"><spring:message code="copy"/></a></td>
	            </tr></table>
            </authz:authorize>
            <br/>
           	<c:if test="${command.status == 'IN_PREPARATION' or command.status == 'READY_TO_START' or command.status == 'RUNNING'}">
            <table class="noborders">
            	<tr><td><spring:message code="control" /></td>
            	<c:if test="${command.status == 'IN_PREPARATION'}">
            	<td><a href="#" onclick="document.getElementById('state').value='makeready';document.forms.mainform.submit()" class="button nomargin"><spring:message code="button.makeready"/></a></td>
            	<td><a href="#" onclick="document.getElementById('state').value='canceling';document.forms.mainform.submit()" class="button nomargin"><spring:message code="button.canceling"/></a></td>
                </c:if>
                <c:if test="${command.status == 'READY_TO_START'}">
            	<td><a href="#" onclick="document.getElementById('state').value='preparing';document.forms.mainform.submit()" class="button nomargin"><spring:message code="button.preparing"/></a></td>
            	<td><a href="#" onclick="document.getElementById('state').value='starting';document.forms.mainform.submit()" class="button nomargin"><spring:message code="button.starting"/></a></td>
                </c:if>
                <c:if test="${command.status == 'RUNNING'}">
            	<td><a href="#" onclick="document.getElementById('state').value='aborting';document.forms.mainform.submit()" class="button nomargin"><spring:message code="button.aborting"/></a></td>
                </c:if>
                </tr>
        	</table>
        	<br />
			</c:if>
	      <input type="submit" style="display:none"/>
	      <input type="hidden" id="action"/>
	      <input type="hidden" id="state" name="state"/>
  	    </form:form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
</tiles:putAttribute>
</tiles:insertTemplate>
