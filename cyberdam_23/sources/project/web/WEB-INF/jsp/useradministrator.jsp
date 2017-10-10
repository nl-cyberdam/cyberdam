<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb mlkey="useradministrator.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="useradministrator.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="useradministrator.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <p><a href="useradministration.htm" class="button"><spring:message code="useradministrationpage" /></a></p>
        <p><a href="groupadministration.htm" class="button"><spring:message code="groupadministrationpage" /></a></p>
        <hr />
        <a href="<c:url value="index.htm"/>" class="button" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>