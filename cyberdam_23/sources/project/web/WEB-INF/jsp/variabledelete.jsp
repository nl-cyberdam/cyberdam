<%@ include file="includes.jsp"%>
<tiles:insertTemplate template="layout_simple.jsp">
	<tiles:putAttribute name="title">
		<spring:message code="deletevariable.title" />
	</tiles:putAttribute>
	<tiles:putAttribute name="content">
		<c:if test="${!empty iiuerror}"><div class="errorBox">${iiuerror}</div>
			<form id="mainform" method="post">
				<input type="hidden" name="id" value="${variable.id}">
				<input type="hidden" id="action"/>
			</form>
			<a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a>
		</c:if>
		<c:if test="${empty iiuerror}">
			<spring:message code="areyousuredelete" arguments="${variable.name}" />
			<form id="mainform" method="post">
				<input type="hidden" name="id" value="${variable.id}"/>
				<input type="hidden" id="action"/>
			</form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.getElementById('action').name='_delete';document.forms.mainform.submit()" class="button"><spring:message code="delete.ok"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="delete.cancel"/></a></td>
	    </tr></table>
		</c:if>
	</tiles:putAttribute>
</tiles:insertTemplate>
