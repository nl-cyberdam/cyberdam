<div class="bottom-nav">
    <a href="<c:url value="/"/>"><spring:message code="link.home"/></a>
    <c:set var="uri"><%= request.getRequestURI().substring(request.getRequestURI().lastIndexOf('/') + 1, request.getRequestURI().lastIndexOf('.')) %></c:set>
    <cyb:helplink code="link.help" helpcode="help.page.${uri}" />
    <span class="footermessage"><spring:message code="cyberdam.footermessage"/></span>
</div>