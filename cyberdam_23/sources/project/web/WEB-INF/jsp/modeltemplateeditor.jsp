<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" >
  	<cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/>
  	<cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep/>
  	<cyb:crumb link="systemparameters.htm" mlkey="systemparameters.title"/><cyb:csep/>
  	<cyb:crumb mlkey="modeltemplate.title"/>
  </tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="modeltemplateeditor.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="modeltemplate.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
  <form:form id="mainform">
 	<textarea	id="modelMetaDataExportTemplate" 
 				name="modelMetaDataExportTemplate"
 				rows="28"
 				style="width:62em;"><c:out value="${command.modelMetaDataExportTemplate}" escapeXml="true"/></textarea>
        <input type="hidden" id="action"/>
 	<br/>
  </form:form>
	<table class="noborders"><tr>
	<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	</tr><tr>
	<td><a href="<c:url value="systemparameters.htm"/>" class="button" ><spring:message code="link.back"/></a></td>
	</tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>