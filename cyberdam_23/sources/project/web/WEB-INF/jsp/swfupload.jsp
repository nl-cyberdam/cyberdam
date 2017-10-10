<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb mlkey="systemadministration.title" /><cyb:csep/><cyb:crumb mlkey="sfwdirectory.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="swfupload.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="swfupload.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
  		<c:if test="${! empty errors}" >
  		<span id="*.errors" class="errorBox">
  		<c:forEach items="${errors}" var="error" >
  		<spring:message code="${error.code}" /><br />
  		</c:forEach>
  		</span>
  		</c:if>
  		<form id="mainform" method="post" action="swfupload.htm" enctype="multipart/form-data">
        	<input type="hidden" name="target" value="${param.target}" />
            <input type="file" name="file"/>
	      <input type="hidden" id="action"/>
        </form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.upload"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr><tr>
  		<td><a href="swfdirectory.htm?forceRefresh=true" class="button back"><spring:message code="link.back"/></a></td>
  		</tr></table>
	</tiles:putAttribute>
</tiles:insertTemplate>

