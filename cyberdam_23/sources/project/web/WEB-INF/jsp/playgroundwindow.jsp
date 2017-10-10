<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_playgroundwindow.jsp">
  <tiles:putAttribute name="title" ><spring:message code="playgroundwindow.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="playgroundwindow.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content"> </tiles:putAttribute>
</tiles:insertTemplate>
