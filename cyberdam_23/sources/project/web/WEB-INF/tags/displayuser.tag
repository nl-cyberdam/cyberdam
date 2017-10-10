<%@tag body-content="empty" %><%@
   attribute name="user" required="true" type="nl.cyberdam.domain.User"%><%@
   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><span class="user"><c:choose>
<c:when test="${!empty user}"><c:url var="link" value="edituser.htm" ><c:param name="id" value="${user.id}" /></c:url><c:out value="${user.firstName} ${user.lastName}"/></c:when>
<c:otherwise>--</c:otherwise>
</c:choose></span>
