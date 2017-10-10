<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep /><cyb:crumb link="gamemodeldetail.htm?id=${gameModel.id}" mlkey="gamemodel.title"/> - <cyb:crumb mlkey="gamemodelstep.editactivity"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gamemodel${command.type}activityedit.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gamemodel${command.type}activityedit.introduction"/></tiles:putAttribute>
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
				<td><spring:message code="activity.enabledScript" /></td>
				<td class=""><form:textarea rows="10" cols="100" path="disabledScript" /></td>
			</tr>
			<tr>
				<td><spring:message code="activity.script" /></td>
				<td class=""><form:textarea rows="10" cols="100" path="script" /></td>
			</tr>
			<tr>
				<td><spring:message code="instructions" /></td>
				<td class="richeditcell"><form:textarea id="instructions_textarea" path="instructions" /></td>
			</tr>
			<tr>
				<td><spring:message code="activity.attachments" /></td>
				<td><form:select path="attachmentIds" multiple="true" cssStyle="width:20em;">
					<form:options items="${gameModel.resources}" itemValue="id" itemLabel="name"/>
				</form:select></td>
			</tr>
			<c:choose>
			<c:when test="${command.type == 'message'}">
			<tr>
				<td><spring:message code="default.text" /></td>
				<td><form:textarea path="defaultMessageText" id="defaultmessagetext_textarea" /></td>
			</tr>
			<cyb:richtext textareaid="defaultmessagetext_textarea" />
			<tr>
				<td><spring:message code="gamemodel.recipients"/></td>
				<td><form:select path="recipientIds" multiple="true" cssStyle="width:20em;">
					<form:options items="${gameModel.roles}" itemValue="id" itemLabel="name"/>
				</form:select></td>
			</tr>
			</c:when>
			<c:when test="${command.type == 'progress'}">
			<tr>
				<td><spring:message code="defaultnextstep" /></td>
				<td><form:select path="defaultOption" cssStyle="width:20em;">
				<c:forEach items="0,1,2,3,4,5,6,7,8,9,10,11,12,13,14" var="item">
					<form:option value="${item}">${item + 1}</form:option>
				</c:forEach></form:select></td>
			</tr>
			<tr>
				<td colspan="2">progress options</td>
			</tr>
			<c:forEach items="0,1,2,3,4,5,6,7,8,9,10,11,12,13,14" var="item">
			<tr>
				<td><spring:message code="description" /></td>
	 			<td><form:input path="nextStepOfPlayOptions[${item}].description" size="60"/></td>
	 		</tr>
			<tr>
				<td><spring:message code="step" /></td>
				<td><form:select path="nextStepOfPlayOptions[${item}].step" cssStyle="width:20em;">
					<form:option value=""><spring:message code="dropdown.selectone" /></form:option>
					<c:forEach items="${gameModel.stepsOfPlay}" var="step">
						<form:option value="${step}">${step.name}</form:option>
					</c:forEach>
					</form:select></td>
			</tr>
			</c:forEach>
			</c:when>
			<c:when test="${command.type == 'form'}">
			<tr><td><spring:message code="variable.title" /></td>
				<td>
				<table>
					<tr>
						<th><spring:message code="activityvariable.caption"/></th>
						<th><spring:message code="activityvariable.mandatory"/></th>
						<th><spring:message code="activityvariable.variable"/></th>
						<th><spring:message code="variable.initialValue"/></th>
					</tr>
					<c:forEach items="${command.activityVariables}" var="actvariable" varStatus="rowCounter">
					<tr>
							<td><form:input path="activityVariables[${rowCounter.index}].caption"/></td>
							<td><form:checkbox path="activityVariables[${rowCounter.index}].mandatory"/></td>
							<td><form:select path="activityVariables[${rowCounter.index}].variable" cssStyle="width:20em;" onchange="$('action').value='updateActivityVariable';$('mainform').submit()">
								<form:option value=""><spring:message code="dropdown.selectone" /></form:option>
								<c:set var="variableId" value="${command.activityVariables[rowCounter.index].variable.id}" />
								<c:forEach items="${variableOptions[rowCounter.index]}" var="variableOption">
									<option value="${variableOption.id}" <c:if test="${variableOption.taken}">disabled</c:if> <c:if test="${variableOption.id == variableId}">selected</c:if>>${variableOption.name}</option>
								</c:forEach>
							</form:select></td>
							<td><c:out value="${actvariable.variable.initialValue}"/></td>
							<td class="icons">
								<a href="#" onclick="$('action').value='deleteActivityVariable';$('activityVariableIndex').value='${rowCounter.index}';$('mainform').submit()" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a>
							</td>
					</tr>
					</c:forEach>
				</table>
<script type="text/javascript">
	Event.observe(window, 'load',
    	function() { 
        	document.location.hash="scroll";
    	});
	// This is a workaround for IE: it does not support disabled option elements
	// It can be removed if MS finally comply to w3 standards for option elements
	if (/MSIE/.test(navigator.userAgent)) {
		$$('option').each(function(o) { if (o.disabled) Element.remove(o) });
	}
</script>
				<a <c:if test="${scroll}">name="scroll"</c:if> href="#" onclick="$('action').value='addActivityVariable';$('mainform').submit()" class="button"><spring:message code="button.addvariable"/></a>
				</td>
			</tr>
			</c:when>
			</c:choose>
		</table>
		<input type="submit" style="display:none"/>
		<input type="hidden" id="action" name="action"/>
		<input type="hidden" id="activityVariableIndex" name="activityVariableIndex"/>
 		</form:form>
		<table class="noborders"><tr>
			<td><a href="#" onclick="$('mainform').submit()" class="button"><spring:message code="button.save"/></a></td>
			<td><a href="#" onclick="$('action').name='_cancel';$('mainform').submit()" class="button"><spring:message code="button.cancel"/></a></td>
		</tr></table>
		<cyb:richtext textareaid="instructions_textarea" />
  </tiles:putAttribute>
</tiles:insertTemplate>
