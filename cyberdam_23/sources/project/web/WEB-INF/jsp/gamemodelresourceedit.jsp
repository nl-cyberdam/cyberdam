<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep /><cyb:crumb link="gamemodeldetail.htm?id=${gameModel.id}" mlkey="gamemodel.title"/> - <cyb:crumb mlkey="gamemodelstep.resource"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gamemodelresourceedit.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gamemodelresourceedit.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <form:form id="mainform">
        <table class="edit">
            <tr><td colspan="2"><form:errors path="*" cssClass="errorBox" /></td></tr>
            <tr><td class="first"><spring:message code="name" /></td><td><form:input path="name" /></td></tr>
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
