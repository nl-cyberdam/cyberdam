<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep /><cyb:crumb link="languagepacks.htm" mlkey="languagepacks.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="languagepackeditor.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="languagepackeditor.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <form:form id="mainform">
        <form:errors path="*" cssClass="errorBox" />
        <table class="edit">
            <tr>
                <td class="first"><form:label path="locale"><spring:message code="locale"/></form:label></td>
                <td><form:input path="locale"/></td>
            </tr>
            <tr>
                <td><form:label path="name"><spring:message code="name"/></form:label></td>
                <td><form:input path="name"/></td>
            </tr>
        </table>
	      <input type="submit" style="display:none"/>
	      <input type="hidden" id="action"/>
  	    </form:form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>