<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="includes.jsp" %>
<%@ page import="org.acegisecurity.ui.webapp.AuthenticationProcessingFilter" %>
<html>
    <head>
        <title><spring:message code="app.title"/> <spring:message code="app.version"/></title>
        <%@ include file="header_include.jsp" %>
        <authz:authorize ifAnyGranted="ROLE_ANONYMOUS">
            <script type="text/javascript">
                window.onload = function() {
                    Form.Element.activate('j_username')
                };
            </script>
        </authz:authorize>
    </head>
    <body>
        <authz:authorize ifAnyGranted="ROLE_ANONYMOUS">
        <h1 id="pagetitle"><spring:message code="homepage.title"/></h1>
        <div class="contentcontainer">
	        <div class="left-nav" style="height: 400px">
                <font color="red">
		            <c:if test="${not empty param.login_error}">
	                    <spring:message code="login.not.successful"/><BR><BR>
		            </c:if>
		            <c:if test="${not empty resetMissing}">
	                    <spring:message code="login.reset.missing"/><BR><BR>
		            </c:if>
		            <c:if test="${not empty resetDone}">
	                    <spring:message code="login.reset.done"/><BR><BR>
		            </c:if>
                </font>
	            <form id="mainform" action="<c:url value='j_acegi_security_check'/>" method="GET">
	                <table class="noborders" style="width: 80%; margin: 0px 0px 0px 10px;">
	                	<%-- inline styles are an IE fix - please keep them on the input fields  --%>
	                    <tr><td><spring:message code="username"/></td></tr>
	                    <tr><td><input type='text' id="j_username" name='j_username' style='font-family: "Trebuchet MS", sans-serif; width: 100%' <c:if test="${not empty param.login_error}">value='<%= session.getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY) %>'</c:if>></td></tr>
	                    <tr><td><spring:message code="password"/></td></tr>
	                    <tr><td><input type='password' id="j_password" name='j_password' style='font-family: "Trebuchet MS", sans-serif;width: 100%'></td></tr>
	                    <tr><td><div><a href="#" onclick="document.forms.mainform.submit()" class="loginButton"><spring:message code="button.login"/></a></div></td></tr>
	                    <tr><td style="height:275px;vertical-align: bottom;"><div><a href="#" onclick="document.forms.mainform.action='resetpassword.htm';document.forms.mainform.submit()" class="loginButton"><spring:message code="button.resetPassword"/></a></div></td></tr>
	                </table>
	            	<input type="submit" style="display:none"/>
	            </form>
	        </div>
	        <div class="content">
	            <div id="systemdetails">
	            <spring:message code="version"/>: <%= nl.cyberdam.domain.SystemParameters.VERSION %><br />
	            <spring:message code="copyright"/>: <%= nl.cyberdam.domain.SystemParameters.COPYRIGHT %>
	        	</div>
	            <div id="pageintroduction"><spring:message code="homepage.pleaselogin"/></div>
	            <div id="playgroundwindowintroduction"><a href="javascript:raw_popup_returnfalse('playgroundwindow.htm','playground','width=800,height=800,resizable=yes,scrollbars=yes')"><spring:message code="homepage.openplaygrounds"/></a></div>
	        </div>
        </div>
        <%@ include file="footer.jsp" %>
        </authz:authorize>
            
        <authz:authorize ifNotGranted="ROLE_ANONYMOUS">
        <h1 id="pagetitle"><spring:message code="homepage.title"/></h1>
        <div class="top-nav"><div class="mypage"><cyb:mypage /></div><div class="breadcrumb"><cyb:crumb mlkey="homepage.title"/></div></div>
        <div class="contentcontainer">
          <%@ include file="leftmainnav.jsp" %>
          <div class="content">
            <div id="systemdetails">
            <spring:message code="version"/>: <%= nl.cyberdam.domain.SystemParameters.VERSION %><br />
            <spring:message code="copyright"/>: <%= nl.cyberdam.domain.SystemParameters.COPYRIGHT %>
        	</div>
        	<div id="pageintroduction"><spring:message code="homepage.introduction"/></div>
          </div>
        </div>
        <%@ include file="footer.jsp" %>
        </authz:authorize>
    </body>
</html>