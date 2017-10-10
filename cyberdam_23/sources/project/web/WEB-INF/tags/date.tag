<%@tag body-content="empty" %><%@
   attribute name="value" required="true" type="java.util.Date" %><%@
   taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><fmt:formatDate value="${value}" pattern="dd MMM yyyy HH:mm"/>