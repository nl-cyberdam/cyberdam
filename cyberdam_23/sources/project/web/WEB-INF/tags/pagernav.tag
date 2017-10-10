<%@tag body-content="empty" %><%@
   attribute name="pagedListHolder" required="true" type="org.springframework.beans.support.PagedListHolder"%><%@
   attribute name="lnk" required="true" %><%@
   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@
   taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%@
   taglib prefix="spring" uri="http://www.springframework.org/tags"
%>
<span class="pagernav">
  <c:if test="${pagedListHolder.pageCount > 1}">
      <a href="<c:url value="${lnk}"><c:param name="page" value="0"/></c:url>"><spring:message code="pager.first" /></a>
      &nbsp;...&nbsp;
      <c:forEach begin="${pagedListHolder.firstLinkedPage}" end="${pagedListHolder.lastLinkedPage}" var="crtpg">
          <c:choose>
              <c:when test="${crtpg == pagedListHolder.page}">
                  <strong><c:out value="${crtpg + 1}"/></strong>
              </c:when>
              <c:otherwise>
                  <a href="<c:url value="${lnk}"><c:param name="page" value="${crtpg}"/></c:url>"><c:out value="${crtpg + 1}"/></a>
              </c:otherwise>
          </c:choose>
      </c:forEach>
      &nbsp;...&nbsp;
      <a href="<c:url value="${lnk}"><c:param name="page" value="${pagedListHolder.pageCount - 1}"/></c:url>"><spring:message code="pager.last" /></a>
      <fmt:message key="psize" var="ps"/>
      &nbsp;|&nbsp;
  </c:if>
</span>
