<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep /><cyb:crumb mlkey="variable.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="variable.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="variable.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
				<table>
                    <tr>
                    	<th><spring:message code="variable.name"/></th>
                    	<th><spring:message code="variable.initialized"/></th>
                    	<th><spring:message code="variable.initialValue"/></th>
                    </tr>
                    <c:if test="${variables.nrOfElements == 0}">
                		<tr><td colspan="3" ><spring:message code="noitemsfound.variables" /></td></tr>
                    </c:if>
                    <c:forEach items="${variables.pageList}" var="variable">
                    	<tr>
                    		<td><a href="variableeditor.htm?id=${variable.id}"><c:out value="${variable.name}"/></a></td>
							<td><input type="checkbox" disabled="disabled" <c:if test="${!empty variable.initialValue}"> checked </c:if>/></td>
                    		<td><c:out value="${variable.initialValue}"/></td>
                            <td class="icons">
                                <a href="variableeditor.htm?id=${variable.id}" title="<spring:message code="edit"/>"><img src="themes/default/button_edit.gif"/></a>
                                <a href="variabledelete.htm?id=${variable.id}" title="<spring:message code="delete"/>"><img src="themes/default/button_delete.gif"/></a>
                            </td>
                    	</tr>
                    </c:forEach>
                </table>
                <br/>
                <a href="<c:url value="variableeditor.htm"/>" class="button"><spring:message code="button.addvariable"/></a>
                <hr />
                <a href="<c:url value="systemadministration.htm"/>" class="button" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>
