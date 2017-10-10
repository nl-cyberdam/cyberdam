<%@tag body-content="empty" %><%@
   attribute name="link" required="false" %><%@
   attribute name="mlkey" required="true" %><%@
   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@
   taglib prefix="spring" uri="http://www.springframework.org/tags"
%><span class="crumb"><c:choose><c:when test="${!empty link}"><a href="<c:url value="${link}" />" ><spring:message code="${mlkey}" /></a></c:when><c:otherwise><spring:message code="${mlkey}" /></c:otherwise></c:choose></span>