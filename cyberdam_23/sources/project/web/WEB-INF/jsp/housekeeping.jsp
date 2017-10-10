<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep/><cyb:crumb mlkey="housekeeping.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="housekeeping.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="housekeeping.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
        <spring:message code="xxx.housekeeping.nofunction"/>
        <div><spring:message code="lasthousekeepingperformed"/>:</div>
        <form id="mainform">
        <a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="run.housekeeping"/></a>
        </form>
        <a class="back button" href="<c:url value="systemadministration.htm"/>" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>