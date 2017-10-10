<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
<c:choose>
	<c:when test="${viewerMode}">
		<tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gamemanifestcomposer.htm" mlkey="gamemanifestcomposer.title"/><cyb:csep/><cyb:crumb mlkey="viewgamemanifest.title"/></tiles:putAttribute>
		<tiles:putAttribute name="title" ><spring:message code="viewgamemanifest.title"/> <c:out value="${gameManifest.name}" /></tiles:putAttribute>
		<tiles:putAttribute name="introduction"><spring:message code="viewgamemanifest.introduction"/></tiles:putAttribute>
	</c:when>
	<c:otherwise>
		<tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gamemanifestcomposer.htm" mlkey="gamemanifestcomposer.title"/><cyb:csep/><cyb:crumb mlkey="editgamemanifest.title"/></tiles:putAttribute>
		<tiles:putAttribute name="title" ><spring:message code="editgamemanifest.title"/> <c:out value="${gameManifest.name}" /></tiles:putAttribute>
		<tiles:putAttribute name="introduction"><spring:message code="editgamemanifest.introduction"/></tiles:putAttribute>
	</c:otherwise>
</c:choose>
	<tiles:putAttribute name="content">
	<c:if test="${viewerMode}"><%@ include file="include_formdisables.jsp" %></c:if>
	<script type="text/javascript">
	function selectGameModel(id, status) {
		if(status != "PUBLIC") {
			$$("select#status option").each(function(o) {
				if (o.value == "UNDER_CONSTRUCTION") {
					o.selected = true;
				}
				if (o.value == "PUBLIC") {
					if (/MSIE/.test(navigator.userAgent)) {
						Element.remove(o);
					} else {
						o.disabled = "disabled";
					}
				}
			});
		}
		if ($('gameModel').value != id) {
			// remove select tags to prevent any binding
			for (var i = 0;i < ${fn:length(command.gameModel.roles)};i++) {
				Element.remove('rolesToPlaygroundObjects[' + i + '].playground');
				Element.remove('rolesToPlaygroundObjects[' + i + '].playgroundObject');
			}
			$('gameModel').value = id;
			$('action').value = 'changeGameModel';
			$('mainform').submit();
		}
		// close iframe
		Control.Modal.close();
	}
	</script>
	<form:form id="mainform">
			<table>
				<tr>
					<td colspan="2"><form:errors path="*" cssClass="errorBox" /></td>
				</tr>
				<tr>
					<td class="first"><form:label path="name"><spring:message code="name" /></form:label></td>
					<td><form:input path="name" /></td>
				</tr>
				<tr>
					<td><form:label path="gameModel"><spring:message code="gamemodel" /></form:label></td>
					<td><c:choose>
						<c:when test="${empty command.gameModel}">
							<a href="browsegamemodels.htm?forceRefresh=true" id="modal_link_browse" class="button"><spring:message	code="select.gamemodel" /></a>
						</c:when>
						<c:otherwise>
							<table class="noborders"><tr>
							<td><c:out value="${command.gameModel.name}" /> (<spring:message code="status" />: <spring:message code="status.${command.gameModel.status}" />)&nbsp;&nbsp;</td>
							<c:if test="${!manifestInUse}">
							<td><a href="browsegamemodels.htm?forceRefresh=true" id="modal_link_browse" class="button"><spring:message	code="change.gamemodel" /></a></td>
							</c:if>
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
				<tr>
					<td><spring:message code="numberofroles" /></td>
					<td><c:out value="${fn:length(command.gameModel.roles)}" /></td>
				</tr>
				<tr>
					<td><form:label path="status"><spring:message code="status" /></form:label></td>
					<td><c:choose>
						<c:when test="${command.statusEditable}">
							<form:select path="status" cssStyle="width:20em;">
								<c:forEach items="${statusOptions}" var="status">
									<form:option value="${status}"><spring:message code="status.${status}" /></form:option>
								</c:forEach>
							</form:select>
						</c:when>
						<c:otherwise>
							<spring:message code="status.${command.status}" />
						</c:otherwise>
					</c:choose></td>
				</tr>
				<tr>
					<td colspan="2">
					<table>
						<tr>
							<th><spring:message code="role" /></th>
							<th><spring:message code="objectname" /></th>
							<th><spring:message code="category" /></th>
							<th><spring:message code="caption" /></th>
							<th><spring:message code="uri" /></th>
							<th><spring:message code="playgroundname" /></th>
							<th><spring:message code="playgroundobject" /></th>
						</tr>
						<c:forEach items="${command.rolesToPlaygroundObjects}" var="roleMapping" varStatus="rowCounter">
							<tr>
								<td><c:out value="${roleMapping.role.name}" /></td>
								<c:choose>
									<c:when test="${roleMapping.playgroundObject != null}">
										<td><c:out value="${roleMapping.playgroundObject.name}" /></td>
										<td><spring:message code="category.${roleMapping.playgroundObject.category}" /></td>
										<td><c:out value="${roleMapping.playgroundObject.caption}" /></td>
										<td><c:out value="${roleMapping.playgroundObject.uri}" /></td>
									</c:when>
									<c:otherwise>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</c:otherwise>
								</c:choose>
								<td>
									<form:select path="rolesToPlaygroundObjects[${rowCounter.index}].playground" cssStyle="width:20em;" onchange="$('action').value='update';$('mainform').submit();">
									<form:option value=""><spring:message code="dropdown.selectone" /></form:option>
									<form:options items="${playgroundOptions}" itemLabel="name" itemValue="id" />
								</form:select></td>
								<td>
									<form:select path="rolesToPlaygroundObjects[${rowCounter.index}].playgroundObject" cssStyle="width:20em;" onchange="$('action').value='update';$('mainform').submit();">
									<form:option value=""><spring:message code="dropdown.selectone" /></form:option>
									<form:options items="${playgroundObjectOptions[rowCounter.index]}" itemLabel="name" itemValue="id" />
								</form:select></td>
							</tr>
						</c:forEach>
					</table>
					</td>
				</tr>
			</table>
			<input type="submit" style="display:none"/>
			<input type="hidden" id="action" name="action"/>
			<form:hidden path="gameModel"/>
  	    </form:form>
  	    <br/>
		<hr />
		<table class="noborders"><tr>
			<c:choose>
			<c:when test="${viewerMode}">
				<td><a href="#" onclick="$('action').name='_cancel';$('mainform').submit()" class="button"><spring:message code="button.close"/></a></td>
			</c:when>
			<c:otherwise>
				<td><a href="#" onclick="$('mainform').submit()" class="button"><spring:message code="manifest.save"/></a></td>
				<td><a href="#" onclick="$('action').name='_cancel';$('mainform').submit()" class="button"><spring:message code="manifest.cancel"/></a></td>
			</c:otherwise>
			</c:choose>
	    </tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>
