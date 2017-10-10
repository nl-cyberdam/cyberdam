<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_popup.jsp">
  <tiles:putAttribute name="content">
    <h1><spring:message code="help" /></h1>
    <spring:message code="${param.item}" />
  </tiles:putAttribute>
</tiles:insertTemplate>