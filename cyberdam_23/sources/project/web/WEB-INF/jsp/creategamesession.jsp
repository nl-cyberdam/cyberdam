<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gamemaster.htm" mlkey="gamemaster.title"/><cyb:csep/><cyb:crumb mlkey="creategamesession.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="creategamesession.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="creategamesession.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
	<script type="text/javascript">
	function selectGameManifest(id) {
		if ($('manifest').value != id) {
			$('manifest').value = id;
			$('action').value = 'changeGameManifest';
			$('mainform').submit();
		}

		// close iframe
		Control.Modal.close();
	}
	</script>
        <form:form id="mainform">
        <table>
            <tr><td colspan="2">
            <form:errors path="*" cssClass="errorBox" />
            </td></tr>
            <tr>
            <td><form:label path="name"><spring:message code="name"/></form:label></td>
            <td><form:input path="name" /></td></tr>
            <tr><td colspan="2"><form:errors path="name" cssClass="error" /></td></tr>
            <tr>
            	<td><form:label path="manifest"><spring:message code="manifest"/></form:label></td>
					<td><c:choose>
						<c:when test="${empty command.manifest}">
							<a href="browsegamemanifests.htm?forceRefresh=true" id="modal_link_browse" class="button"><spring:message	code="select.gamemodel" /></a>
						</c:when>
						<c:otherwise>
							<table class="noborders"><tr>
							<td><c:out value="${command.manifest.name}" /> (<spring:message code="status" />: <spring:message code="status.${command.manifest.status}" />)&nbsp;&nbsp;</td>
							<td><a href="browsegamemanifests.htm?forceRefresh=true" id="modal_link_browse" class="button"><spring:message code="change.gamemanifest" /></a></td>
							</tr></table>
						</c:otherwise>
					</c:choose>
					<script>
			new Control.Modal('modal_link_browse',{
				iframe: true,
				width: 700,
				height: 480
			});
				</script></td>
			</tr>
        </table>
	    <form:hidden path="manifest"/>
	    <input type="hidden" id="action" name="action"/>
	    <input type="submit" style="display:none"/>
  	    </form:form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>
