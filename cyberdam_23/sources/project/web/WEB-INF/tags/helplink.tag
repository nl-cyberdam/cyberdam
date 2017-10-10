<%@tag body-content="empty" %><%@
   attribute name="code" required="true" %><%@
   attribute name="helpcode" required="true" %><%@
   taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@
   taglib prefix="spring" uri="http://www.springframework.org/tags"
%><span class="helplink"><a href="<c:url value="help.htm" ><c:param name="item" value="${helpcode}" /></c:url>" onclick="link_popup(this,'width=500,height=300,resizable=yes,scrollbars=yes'); return false;"><spring:message code="${code}" /></a></span>