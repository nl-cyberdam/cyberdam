<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb mlkey="mysettings.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="mysettings.title"/> <c:out value="${command.user.firstName}" /> <c:out value="${command.user.lastName}" /></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="mysettings.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <form:form id="mainform">
<h2><spring:message code="basics" /></h2>
          <table class="edit">
            <tr><td colspan="2"><form:errors path="*" cssClass="errorBox" /></td></tr>

            <tr><td class="first"><form:label path="user.firstName"><spring:message code="firstname"/></form:label></td>
            <td><c:out value="${command.user.firstName}" /></td></tr>
            
            <tr><td><form:label path="user.lastName"><spring:message code="lastname"/></form:label></td>
            <td><c:out value="${command.user.lastName}" /></td></tr>
            
            <tr><td><form:label path="user.username"><spring:message code="username"/></form:label></td>
            <td><c:out value="${command.user.username}" /></td></tr>

            <tr><td><form:label path="user.password"><spring:message code="password"/></form:label></td>
            <td><form:password path="user.password"/></td></tr>

            <tr><td><form:label path="user.status"><spring:message code="status"/></form:label></td>
            <td><spring:message code="status.${command.user.status}" /></td></tr>
			</table>


<h2><spring:message code="privileges" /></h2> 
			<table class="edit">
			<tr><td class="first"><form:label path="user.gameAuthorities.gameSessionMaster"><spring:message code="gamesessionmaster"/></form:label></td>
            <td>&nbsp;<form:checkbox path="user.gameAuthorities.gameSessionMaster" disabled="true" /></td></tr>

            <tr><td><form:label path="user.gameAuthorities.gameManifestComposer"><spring:message code="gameManifestComposer"/></form:label></td>
            <td>&nbsp;<form:checkbox path="user.gameAuthorities.gameManifestComposer" disabled="true" /></td></tr>

            <tr><td><form:label path="user.gameAuthorities.gameAuthor"><spring:message code="gameAuthor"/></form:label></td>
            <td>&nbsp;<form:checkbox path="user.gameAuthorities.gameAuthor" disabled="true" /></td></tr>

            <tr><td><form:label path="user.gameAuthorities.playgroundAuthor"><spring:message code="playgroundAuthor"/></form:label></td>
            <td>&nbsp;<form:checkbox path="user.gameAuthorities.playgroundAuthor" disabled="true" /></td></tr>

            <tr><td><form:label path="user.gameAuthorities.lcmsAdministrator"><spring:message code="lcmsAdministrator"/></form:label></td>
            <td>&nbsp;<form:checkbox path="user.gameAuthorities.lcmsAdministrator" disabled="true" /></td></tr>

            <tr><td><form:label path="user.gameAuthorities.lmsAdministrator"><spring:message code="lmsAdministrator"/></form:label></td>
            <td>&nbsp;<form:checkbox path="user.gameAuthorities.lmsAdministrator" disabled="true" /></td></tr>

            <tr><td><form:label path="user.gameAuthorities.vleAdministrator"><spring:message code="vleAdministrator"/></form:label></td>
            <td>&nbsp;<form:checkbox path="user.gameAuthorities.vleAdministrator" disabled="true" /></td></tr>

            <tr><td><form:label path="user.gameAuthorities.userAdministrator"><spring:message code="userAdministrator"/></form:label></td>
            <td>&nbsp;<form:checkbox path="user.gameAuthorities.userAdministrator" disabled="true" /></td></tr>

            <tr><td><form:label path="user.gameAuthorities.systemAdministrator"><spring:message code="systemAdministrator"/></form:label></td>
            <td>&nbsp;<form:checkbox path="user.gameAuthorities.systemAdministrator" disabled="true" /></td></tr>
			</table>

			<table class="edit">
			<tr><td class="first"><form:label path="user.groups"><spring:message code="groups"/></form:label></td>
            <td><spring:message code="allgroups"/></td></tr>
			</table>

<h2><spring:message code="preferences" /></h2>
			<table class="edit">
            <tr><td class="first"><form:label path="user.locale"><spring:message code="language"/></form:label></td>
            <td><form:select path="user.locale" items="${languages}" itemLabel="name" itemValue="locale">
            </form:select></td></tr>
            <tr><td colspan="2"><form:errors path="user.locale" cssClass="error" /></td></tr>
            
            <tr><td><form:label path="user.email"><spring:message code="email"/></form:label></td>
            <td><form:input path="user.email" /></td></tr>
            <tr><td colspan="2"><form:errors path="user.email" cssClass="error" /></td></tr>
            
			<tr><td class="first"><form:label path="user.defaultNotifyNewStepOfPlay"><spring:message code="defaultNotifyNewStepOfPlay"/></form:label></td>
            <td><form:checkbox path="user.defaultNotifyNewStepOfPlay" /></td></tr>

			<tr><td class="first"><form:label path="user.notifyNewMessages"><spring:message code="defaultNotifyNewMessages"/></form:label></td>
            <td><form:checkbox path="user.notifyNewMessages" /></td></tr>
			</table>
			
<h2><spring:message code="statistics" /></h2>
			<table class="edit">
            <tr><td class="first"><form:label path="user.lastLogin"><spring:message code="lastlogin" /></form:label></td><td><fmt:formatDate value="${command.user.lastLogin}" pattern="dd MMM yyyy HH:mm" /></td></tr>

           <tr><td colspan="2">
	  </table>
	  <input type="submit" style="display:none"/>
	  <input type="hidden" id="action"/>
  	</form:form>
	<table class="noborders"><tr><td>
  		<a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a>
  		</td><td>
  		<a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a>
  		</td></tr>
	</table>
  </tiles:putAttribute>
</tiles:insertTemplate>
