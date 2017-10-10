<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="rename.file.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="rename.file.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
  		<c:if test="${! empty errors}" >
  		<span id="*.errors" class="errorBox">
  		<c:forEach items="${errors}" var="error" >
  		<spring:message code="${error.code}" /><br />
  		</c:forEach>
  		</span>
  		</c:if>

  		<h2><spring:message code="rename.file" /></h2>
		<form:form id="mainform" method="post">
		<input type="hidden" name="participantId" value="${param.participantId}" />
		<table class="edit">
            <tr><td class="first"><spring:message code="filename"/></td>
			<td><form:input path="name" /></td></tr>
        </table>
		<input type="submit" style="display:none"/>
		<input type="hidden" id="action"/>
        </form:form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr><tr>
  		<td><a href="filedirectory.htm?participantId=${param.participantId}" class="button back"><spring:message code="link.back"/></a></td>
  		</tr></table>
	</tiles:putAttribute>
</tiles:insertTemplate>