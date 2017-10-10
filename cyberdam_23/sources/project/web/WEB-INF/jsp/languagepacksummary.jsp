<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
    <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep /><cyb:crumb link="languagepacks.htm.htm" mlkey="languagepacks.title"/><cyb:csep /><cyb:crumb mlkey="importlanguagepack.title"/></tiles:putAttribute>
    <tiles:putAttribute name="title" ><spring:message code="importlanguagepack.title"/></tiles:putAttribute>
    <tiles:putAttribute name="introduction">
        <spring:message code="importlanguagepacksummary.introduction"/>
    </tiles:putAttribute>
    <tiles:putAttribute name="content">  
        <c:out value="${languagePack.name}"/>, <c:out value="${languagePack.locale}"/>
        <br/> 
        <spring:message code="languagepack.import.objects"/>: <c:out value="${numberOfObjects}"/>
        <br/><br/>
        <a href="<c:url value="languagepacks.htm"/>" class="button"><spring:message code="link.back"/></a>
    </tiles:putAttribute>
</tiles:insertTemplate>