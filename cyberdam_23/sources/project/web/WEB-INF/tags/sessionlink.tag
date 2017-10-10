<%@tag 
%><%@attribute name="href" required="true"
%><%@attribute name="status" required="true"
%><%@attribute name="styleClass" required="false"
%><%@attribute name="tooltip" required="false"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="spring" uri="http://www.springframework.org/tags"
%><c:choose>
    <c:when test="${status eq 'RUNNING'}">  
        <a href="${href}" <c:if test="${!empty styleClass}">class="${styleClass}"</c:if> <c:if test="${!empty tooltip}">title="<spring:message code="${tooltip}"/>"</c:if> ><jsp:doBody/></a>
    </c:when>
    <c:otherwise>
		<a class="disabled<c:if test="${!empty styleClass}"> ${styleClass}</c:if>" title="<spring:message code="statenotrunning.message"/>"><jsp:doBody/></a>
    </c:otherwise>
</c:choose>
