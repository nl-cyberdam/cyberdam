<%@ include file="includes.jsp" %>

<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep /><cyb:crumb link="gamemodeldetail.htm?id=${command.id}" mlkey="gamemodel.title"/><cyb:csep/><cyb:crumb mlkey="gamemodelstep.metadata"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gamemodelmetadata.title"/> <c:out value="${command.name}" /></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="gamemodelmetadata.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <form:form id="mainform">
        <table class="edit">
            <tr><td colspan="2">
            <form:errors path="*" cssClass="errorBox" /><br />
            </td></tr>
            <tr>
            <td class="first"><form:label path="name"><spring:message code="name"/></form:label></td>
            <td><form:input path="name" size="60"/></td></tr>
            <tr><td colspan="2"><form:errors path="name" cssClass="error" /></td></tr>
            <tr>
            <td><form:label path="caption"><spring:message code="caption"/></form:label></td>
            <td class="textarea_cell"><form:textarea path="caption" cssClass="caption_area" /></td></tr>
            <tr>
            <td><form:label path="description"><spring:message code="description"/></form:label></td>
            <td class="richeditcell"><form:textarea id="description_area" path="description" /></td></tr>
            <tr>
            <td><form:label path="statusTemplate"><spring:message code="model.statusTemplate"/></form:label></td>
            <td class="richeditcell">
            	<form:textarea id="statusTemplate_area" path="statusTemplate" /><br/><br/>
	    		<a href="#" onclick="$('action').name='_setDefault';$('mainform').submit()" class="button"><spring:message code="button.todefault"/></a>
            </td></tr>
            <tr>
            <td><form:label path="headerTemplate"><spring:message code="model.headerTemplate"/></form:label></td>
            <td class="richeditcell"><form:textarea id="headerTemplate_area" path="headerTemplate" /></td></tr>
            <tr><td colspan="2"><form:errors path="description" cssClass="error" /></td></tr>
            <tr><td><form:label path="script"><spring:message code="model.script"/></form:label></td>
            <td class="textarea_cell"><form:textarea id="script_area" path="script" /></td></tr>
            <tr>
            <td><spring:message code="numberofroles"/></td>
            <td><c:out value="${fn:length(command.roles)}"/></td></tr>
            <tr>
            <td><form:label path="status"><spring:message code="status"/></form:label></td>
            <td>
            <c:choose>
            <c:when test="${command.statusEditable}">
            <form:select path="status" >
              <c:forEach var="status" items="${statusOptions}" >
			    <form:option value="${status}" ><spring:message code="status.${status}"/></form:option>
			  </c:forEach>
            </form:select>
            </c:when>
            <c:otherwise>
            <spring:message code="status.${command.status}"/>
            </c:otherwise>
            </c:choose>
            </td></tr>
            <tr>
            <td><form:label path="sessionClassicLayout"><spring:message code="model.classicLayout"/></form:label></td>
            <td><form:checkbox path="sessionClassicLayout"/></td></tr>
            <tr><td colspan="2"><form:errors path="status" cssClass="error" /></td></tr>
            </table>
	      <input type="submit" style="display:none"/>
	      <input type="hidden" id="action"/>
  	    </form:form>
	    <table class="noborders"><tr>
	    	<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
        <cyb:richtext textareaid="description_area" />
        <cyb:richtext textareaid="statusTemplate_area" />
        <cyb:richtext textareaid="headerTemplate_area" />
  </tiles:putAttribute>
</tiles:insertTemplate>