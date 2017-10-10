<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@taglib prefix="spring" uri="http://www.springframework.org/tags"
%><br>
<table class="noborders"><tr><td style="vertical-align:middle">
<label for="filter.searchText"><spring:message code="filter" />:</label>
</td><td style="vertical-align:middle">
	<input type="text" name="filter.searchText"
		value="<c:out value="${filter.searchText}"/>"
		onchange="document.forms.mainform.submit()">
	</td><td>
		<a href="javascript:document.forms.mainform['filter.searchText'].value='';document.forms.mainform.submit()">
			<img src="themes/default/button_reset.gif" alt="<spring:message code="link.filter.clear" />" />
		</a>
			<c:if test="${empty refreshLink}">
				<c:set var="refreshLink" value="?forceRefresh=true" />
			</c:if>
	</td><td><a href="${refreshLink}"><img src="images/refresh.png" alt='<spring:message code="forcerefresh"/>' /></a></td></tr></table>
<br>
