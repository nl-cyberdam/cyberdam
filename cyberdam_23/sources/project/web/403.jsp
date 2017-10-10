<%@ taglib prefix="cyb" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="org.acegisecurity.context.SecurityContextHolder" %>
<%@ page import="org.acegisecurity.Authentication" %>
<%@ page import="org.acegisecurity.ui.AccessDeniedHandlerImpl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><cyb:systemname/>: <spring:message code="error.access.denied"/></title>
        
<link rel="stylesheet" href="themes/default/style.css" type="text/css" />
    </head>
    <body>
        <h1 id="pagetitle"><spring:message code="error.access.denied"/></h1>
        <div class="top-nav"></div>
        <div class="content">
        <div id="pageintroduction"><h3><spring:message code="error.occurred"/></h3></div>
  		<div class="errorBox"><spring:message code="error.access.denied"/></div>
<p>
	<%= request.getAttribute(AccessDeniedHandlerImpl.ACEGI_SECURITY_ACCESS_DENIED_EXCEPTION_KEY)%>
</p>
<%		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) { %>
			Authentication object as a String: <%= auth.toString() %><BR><BR>
<%      } %>
		<br/>
  		Suggestions:
  		<ul>
  		<li>Return to <a href="<%= request.getContextPath() %>/">Cyberdam Main</a></li>
  		<li>Return to <script language="Javascript" type="text/javascript">document.write("<a href=\"" + document.referrer + "\">")</script>Previous page<script language="Javascript" type="text/javascript">document.write("</a>")</script></li>
  		</ul>
  		</div>
		<div class="bottom-nav">
			<a href="<%= request.getContextPath() %>/">Home</a>
			<span class="footermessage"><a href="http://www.cyberdam.nl">www.cyberdam.nl</a></span>
		</div>
    </body>
</html>	
