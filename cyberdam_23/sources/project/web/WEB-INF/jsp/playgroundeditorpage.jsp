<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="playgroundlistpage.htm" mlkey="playgroundlistpage.title"/><cyb:csep /><cyb:crumb link="playgrounddetail.htm?id=${command.id}" mlkey="playgrounddetail.title"/><cyb:csep /><cyb:crumb mlkey="playgroundeditorpage.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="playgroundeditorpage.title"/> <c:out value="${command.name}" /></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="playgroundeditorpage.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
          <form:form id="mainform">
        <table class="edit">
        <tr><td colspan="2"><form:errors path="*" cssClass="errorBox" /></td></tr>
        <tr><td class="first"><form:label path="name"><spring:message code="playground.name "/></form:label></td><td><form:input path="name" cssClass="fill" /></td></tr>
        <tr><td><spring:message code="caption" /></td><td><form:textarea path="caption" cssClass="fill" /></td></tr>
        <tr><td><form:label path="uriId"><spring:message code="uri"/></form:label></td><td><form:input path="uriId" cssClass="fill" /></td></tr>
        <tr><td colspan="2"><form:errors path="name" cssClass="error" /></td></tr>
        <tr><td><spring:message code="description" /></td><td class="richeditcell"><form:textarea id="descriptiontextarea" path="description" /></td></tr>
        <tr><td><spring:message code="version" /></td><td><form:input path="version" cssClass="fill" /></td></tr>
        
        <tr><td><spring:message code="lastmutatedby" /></td><td><cyb:displayuser user="${command.lastModifier}"/></td></tr>
        <tr><td><spring:message code="lastmutatedon" /></td><td><fmt:formatDate value="${command.lastModified}" pattern="dd MMM yyyy HH:mm" /></td></tr>
        <tr><td><spring:message code="playground.link" /></td><td><form:input path="link" cssClass="fill" cssStyle="width:21em"/></td></tr>
        <tr><td><form:label path="status"><spring:message code="status"/></form:label></td>
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
            <spring:message code="status.${command.status}" />
            </c:otherwise>
            </c:choose>
            </td></tr>
        </table>
		<input type="submit" style="display:none"/>
		<input type="hidden" id="action"/>
  </form:form>
	<table class="noborders"><tr><td>
  		<a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a>
  		</td><td>
  		<a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a>
  		</td></tr>
		<cyb:richtext textareaid="descriptiontextarea"/>
	</table>
  </tiles:putAttribute>
</tiles:insertTemplate>