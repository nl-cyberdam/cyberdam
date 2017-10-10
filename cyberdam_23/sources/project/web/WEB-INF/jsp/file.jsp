<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_simple.jsp">
  <tiles:putAttribute name="title" ><spring:message code="file.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="file.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
    <div><h2><spring:message code="File" /></h2>
    </div>
  </tiles:putAttribute>
</tiles:insertTemplate>