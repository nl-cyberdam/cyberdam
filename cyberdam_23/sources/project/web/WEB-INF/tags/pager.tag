<%@tag body-content="empty" %><%@
   attribute name="pagedListHolder" required="true" type="org.springframework.beans.support.PagedListHolder"%><%@
   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@
   taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%@
   taglib prefix="spring" uri="http://www.springframework.org/tags"
%>
<span class="pager">
                <fmt:message key="pg.records">
                    <c:choose>
                    	<c:when test="${pagedListHolder.nrOfElements == 0}">
                    		<fmt:param value="0"/>
                    		<fmt:param value="0"/>
                    		<fmt:param value="0"/>
                    	</c:when>
                    	<c:otherwise>
                    		<fmt:param value="${pagedListHolder.firstElementOnPage + 1}"/>
                    		<fmt:param value="${pagedListHolder.lastElementOnPage + 1}"/>
                    		<fmt:param value="${pagedListHolder.nrOfElements}"/>                    	
                    	</c:otherwise>
                    </c:choose>
                </fmt:message>
      &nbsp;|&nbsp;
                <fmt:message key="pg.pages">
                    <fmt:param value="${pagedListHolder.page + 1}"/>
                    <fmt:param value="${pagedListHolder.pageCount}"/>
                </fmt:message>
      &nbsp;|&nbsp;
				<select class="pagesize" name="pageSize" onChange="document.forms[0].submit()">
				<c:forTokens items="5,10,15,20,30,40,80" delims="," var="crtps">
					<option
						<c:if test="${pagedListHolder.pageSize == crtps}"> selected</c:if>
						value="<c:out value="${crtps}"/>">
					<c:out value="${crtps} " /><spring:message code="per.page" />
					</option>
				</c:forTokens>
				</select>
</span>
