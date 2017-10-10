<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep /><cyb:crumb link="gamemodeldetail.htm?id=${gameModel.id}" mlkey="gamemodel.title"/> - <cyb:crumb mlkey="gamemodelstep.gameplay"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gamemodelgameplay.title"/>&nbsp;<c:out value="${gameModel.name}" /></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gamemodelgameplay.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
	<script type="text/javascript" src="<c:url value="/js/scriptaculous/effects.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/scriptaculous/dragdrop.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/json2.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/grid.js"/>"></script>
	<script type="text/javascript">
		grid.gridData = JSON.parse('${gridData}');
		grid.shiftLeftString = '<spring:message code="shift.left"/>';
		grid.shiftRightString = '<spring:message code="shift.right"/>';
		grid.deleteString = '<spring:message code="delete"/>';
		grid.nrRoles = ${fn:length(gameModel.roles)};
		grid.editable = true;
	</script>
		<form:form id="mainform">
		<form:errors path="*" cssClass="errorBox" htmlEscape="false"/>
		<h3><spring:message code="gamemodelgameplay.title"/></h3>
		<div id="grid">
			<table>
				<tr>
					<th <c:if test="${fn:length(gameModel.roles) == 0}">class="buttondimension"</c:if>/>
					<th class="button"><a class="button" href="javascript:grid.addStep()"><spring:message code="add.step"/></a></th>
				</tr>
			<c:forEach items="${gameModel.roles}" var="role" varStatus="rowCounter">
				<tr>
					<td><div id="dragRole${rowCounter.index}" class="draggable role"><form:input path="roles[${rowCounter.index}].name"/><img src="themes/default/button_delete.gif" onclick="javascript:grid.deleteRole(${rowCounter.index})" class="input"/></div></td>
				</tr>
			</c:forEach>
			</table>
			<a class="button" href="javascript:grid.addRole()"><spring:message code="add.role"/></a>
		</div>
		<br/>
		<h3><spring:message code="initialstepofplay" /></h3>
		<form:select path="initialStepOfPlay" cssStyle="width:20em;" onmousedown="javascript:grid.updateSelect(this)">
			<form:option value=""><spring:message code="dropdown.selectone" /></form:option>
			<c:forEach items="${gameModel.stepsOfPlay}" var="step">
				<form:option value="${step}">${step.name}</form:option>
			</c:forEach>
		</form:select>
		<br/>
		<br/>
		<h3><spring:message code="Activities" /></h3>
		<table>
			<tr><th><spring:message code="activity.type" /></th><th><spring:message code="activity.name" /></th></tr>
			<c:if test="${fn:length(activityList) == 0}">
			<tr><td colspan="2" ><spring:message code="noitemsfound.activities" /></td></tr>
			</c:if>
			<c:forEach items="${activityList}" var="activity">
			<tr>
				<td><spring:message code="activitytype.${activity.type}" /></td>
				<td><div id="dragActivity${activity.id}" class="draggable activity"><c:out value="${activity.name}" /></div></td>
			</tr>
			<script type="text/javascript">new Draggable("dragActivity${activity.id}", {scroll:window, revert:true});</script>
			</c:forEach>
		</table>
		<br/>
		<input type="hidden" id="action" name="action"/>
		<input type="hidden" id="gridData" name="gridData"/>
		</form:form>
		<table class="noborders"><tr>
			<td><a href="#" onclick="grid.submit()" class="button"><spring:message code="button.save"/></a></td>
			<td><a href="#" onclick="$('action').name='_cancel';grid.submit()" class="button"><spring:message code="button.cancel"/></a></td>
		</tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>
