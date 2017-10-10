<%@tag body-content="empty" %><%@
   attribute name="pagedListHolder" required="true" type="org.springframework.beans.support.PagedListHolder"%><%@
   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@
   taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"
%>
<div class="psize">
    <select name="pageSize" onChange="document.forms[0].submit()">
        <c:forTokens items="5,10,15,20,30,40,80" delims="," var="crtps">
            <option<c:if test="${pagedListHolder.pageSize == crtps}"> selected</c:if> value="<c:out value="${crtps}"/>"><c:out value="${crtps} per pagina"/>
            </option>
        </c:forTokens>
    </select>
</div>