<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%><%@ 
   include file="includes.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><cyb:systemname/>: popup</title>
        <%@ include file="header_include.jsp" %>
        <script type="text/javascript">
        	function exit() {
        		if (parent == self) {
        			self.close();
        		} else if (parent.Control.Modal) {
        			parent.Control.Modal.close();
        		}       		
        		return(void(0));
        	}
        </script>
        <style type="text/css">
        td {
        font-size: 0.8em;
        }
        form td {
        vertical-align: middle;
        }
        </style>
    </head>
    <body>
    <table style="width:100%">
	<tr>
		<td align="right"  style="border:none;background-color: transparent;">
			<a href="#" onclick="exit();" class="button"><b><spring:message code="close"/></b></a>
		</td>
	</tr>
	</table>
        <div class="content">
  		<tiles:insertAttribute name="content"/>
  		</div>
    </body>
</html>