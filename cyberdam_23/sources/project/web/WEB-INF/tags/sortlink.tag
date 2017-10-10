<%@tag 
%><%@attribute name="lnk" required="true"
%><%@attribute name="sort" required="true"
%><%@attribute name="query" required="false"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><c:set var="q" value=""/><c:if test="${!empty query}"><c:set var="q" value="&${query}"/></c:if><a href="<c:url value="${lnk}"><c:param name="sort.property" value="${sort}"/></c:url><c:out value="${q}" escapeXml="false"/>"><jsp:doBody /></a>