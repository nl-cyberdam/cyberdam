<%@ include file="includes.jsp"%>
<div class="left-nav"><menu-el:useMenuDisplayer name="ListMenu" permissions="rolesAdapter"
	repository="menurepository">
	<c:forEach var="menuitem" items="${menurepository.topMenus}">
		<menu-el:displayMenu name="${menuitem.name}" />
	</c:forEach>
</menu-el:useMenuDisplayer> 
</div>
