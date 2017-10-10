<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%><%@ 
   include file="includes.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><cyb:systemname/>: <tiles:insertAttribute name="title"/></title>
        <%@ include file="header_include.jsp" %>
    </head>
    <body>
        <h1 id="pagetitle"><tiles:insertAttribute name="title"/></h1>
        <div class="top-nav"><div class="mypage"><cyb:mypage /></div><div class="breadcrumb"><tiles:insertAttribute name="breadcrumb"/>&nbsp;</div></div>
        <div class="content">
        <div id="pageintroduction"><tiles:insertAttribute name="introduction"/></div>
  		<tiles:insertAttribute name="content"/>
  		</div>
        <%@ include file="footer.jsp" %>
    </body>
</html>