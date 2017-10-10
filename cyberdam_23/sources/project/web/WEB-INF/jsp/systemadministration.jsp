<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb mlkey="systemadministration.title" /></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="systemadministration.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="systemadministration.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <menu-el:useMenuDisplayer name="ListMenu" permissions="rolesAdapter"
	repository="menurepository">
	<c:forEach var="menuitem" items="${menurepository.topMenus}">
		<menu-el:displayMenu name="${menuitem.name}" />
	</c:forEach>
</menu-el:useMenuDisplayer>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
    <a href="<c:url value="index.htm"/>" class="button" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>
