<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep /><cyb:crumb link="gamemodeldetail.htm?id=${gameModel.id}" mlkey="gamemodel.title"/> - <cyb:crumb mlkey="gamemodelstep.editstepofplay"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gamemodelstepofplayedit.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gamemodelstepofplayedit.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
		<form:form id="mainform">
		<table class="edit">
			<tr>
				<td colspan="2"><form:errors path="*" cssClass="errorBox" /></td>
			</tr>
			<tr>
				<td class="first"><spring:message code="name" /></td>
				<td><form:input path="name" size="60"/></td>
			</tr>
			<tr>
				<td><spring:message code="stepofplay.script" /></td>
				<td class=""><form:textarea rows="10" cols="100" path="script" /></td>
			</tr>
		</table>
		<input type="submit" style="display:none"/>
		<input type="hidden" id="action" name="action"/>
 		</form:form>
		<table class="noborders"><tr>
			<td><a href="#" onclick="$('mainform').submit()" class="button"><spring:message code="button.save"/></a></td>
			<td><a href="#" onclick="$('action').name='_cancel';$('mainform').submit()" class="button"><spring:message code="button.cancel"/></a></td>
		</tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>
