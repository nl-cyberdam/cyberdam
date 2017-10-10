<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep/><cyb:crumb mlkey="systemparameters.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="systemparameters.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="systemparameters.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
  <form:form id="mainform">
  <table class="edit">
  <tr><td colspan="2"><form:errors path="*" cssClass="errorBox" /></td></tr>
  <!-- 
  <tr><td colspan="2"><spring:message code="croninterval.systemparam.comment" /></td></tr>
  <tr><td><spring:message code="croninterval" /></td><td><form:input path="croninterval" /></td></tr>
  -->
  <tr><td colspan="2"><spring:message code="email.systemparam.comment" /></td></tr>
  <tr><td class="first"><spring:message code="email" /></td><td><form:input path="email" /></td></tr>
  <tr><td colspan="2"><spring:message code="defaultLanguageCode.systemparam.comment" /></td></tr>
  <tr><td><spring:message code="defaultLanguageCode" /></td><td><form:input path="defaultLanguageCode" /></td></tr>
  <tr><td colspan="2"><spring:message code="defaultRows.systemparam.comment" /></td></tr>
  <tr><td><spring:message code="defaultRows" /></td><td><form:select items="${defaultRowOptions}" path="defaultRows" cssClass="pagesize" /></td></tr>
  <tr><td colspan="2"><spring:message code="vleActivities.systemparam.comment" /></td></tr>
  <tr><td><spring:message code="vleActivities" /></td><td><form:input path="vleActivities" /></td></tr>
  <tr><td colspan="2"><spring:message code="vleFiles.systemparam.comment" /></td></tr>
  <tr><td><spring:message code="vleFiles" /></td><td><form:input path="vleFiles" /></td></tr>
  <tr><td colspan="2"><spring:message code="vleMail.systemparam.comment" /></td></tr>
  <tr><td><spring:message code="vleMail" /></td><td><form:input path="vleMail" /></td></tr>
  <tr><td colspan="2"><spring:message code="vleMessage.systemparam.comment" /></td></tr>
  <tr><td><spring:message code="vleMessage" /></td><td><form:checkbox path="vleMessage" /></td></tr>
  <tr><td colspan="2"><spring:message code="vleStep.systemparam.comment" /></td></tr>
  <tr><td><spring:message code="vleStep" /></td><td><form:checkbox path="vleStep" /></td></tr>
  <tr><td colspan="2"><spring:message code="uploadSizeMaxBytes.systemparam.comment" /></td></tr>
  <tr><td><spring:message code="uploadSizeMaxBytes" /></td><td><form:input path="uploadSizeMaxBytes" /></td></tr>
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