<%@tag import="nl.cyberdam.util.GameUtil" 
%><%@attribute name="user" type="nl.cyberdam.domain.User"
%><%@taglib prefix="spring" uri="http://www.springframework.org/tags"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%
Long userId = GameUtil.getCurrentUser().getId();
jspContext.setAttribute("userId", userId);
jspContext.setAttribute("userFullName", GameUtil.getCurrentUser().getFirstName() + " " + GameUtil.getCurrentUser().getLastName());
%>
<table class="noborders"><tr>
<td><a href="<c:url value="/mysettings.htm"/>"><c:out value="${userFullName}"/></a></td>
<td><a href="<c:url value="/j_acegi_logout"/>" class="button"><spring:message code="link.logout"/></a></td>
</tr></table>
