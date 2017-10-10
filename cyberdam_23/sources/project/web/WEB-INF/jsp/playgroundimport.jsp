<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="playgroundlistpage.htm" mlkey="playgroundlistpage.title"/><cyb:csep/><cyb:crumb mlkey="playgroundimport.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="playgroundimport.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction">
        <c:choose>
            <c:when test="${form_type==2}">
                <spring:message code="playgroundimportresult.introduction"/>
            </c:when>
            <c:otherwise>
                <spring:message code="playgroundimport.introduction"/>
            </c:otherwise>
        </c:choose>
  </tiles:putAttribute>
 <tiles:putAttribute name="content">
 
		<c:choose>
		<c:when test="${form_type==0}">
		<form id="mainform" method="post" enctype="multipart/form-data">
            <input type="file" name="file"/>
            <input type="hidden" id="action1"/>
        </form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.getElementById('action1').name='_upload';document.forms.mainform.submit()" class="button"><spring:message code="button.upload"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action1').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
        </c:when>
        <c:when test="${form_type==1}">
        <form id="mainform" method="post" enctype="multipart/form-data">
        <div class="errorBox"><spring:message code="playground.uri.nonunique" /></div>
        <table>
        	<tr><td><spring:message code="playgroundimport.renameuri"/></td><td><input name="playgrounduri" value="${playgroundUri}"/></td></tr>
        </table>
        <input type="hidden" id="action1"/>
        </form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.getElementById('action1').name='_save';document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action1').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
        </c:when>
        <c:when test="${form_type==2}">
        <c:if test="${SWF_NOT_SAVED}">
        	<div class="errorBox"><spring:message code="playgroundimportresult.swf_not_saved" /></div>
        </c:if>
        <c:if test="${resultStrings==null}">
        	<div class="errorBox"><spring:message code="playgroundimportresult.no_playground_found" /></div>
        	<c:if test="${not empty invalidvalues}">
        	<h3 class="errorHeader"><spring:message code="playgroundimportresult.invalid_values" /></h3>
        	<div class="errorBox">
        	<c:forEach var="invalidvalue" items="${invalidvalues}"><c:out value="${invalidvalue.message} ${invalidvalue.propertyPath}: ${invalidvalue.value} (${invalidvalue.beanClass})"/> <br/></c:forEach>
        	</div>
        	</c:if>
        </c:if>
        <c:if test="${resultStrings!=null}">
        <table>
        <tr><td><spring:message code="name"/></td><td><c:out value="${playground.name}"/></td></tr>
        <tr><td><spring:message code="caption" /></td><td><c:out value="${playground.caption}"/> </td></tr>
        <tr><td><spring:message code="uri"/></td><td><c:out value="${playground.uriId}"/></td></tr>
        <tr><td><spring:message code="version" /></td><td><c:out value="${playground.version}"/></td></tr>
        </table>
    	<div class="resultlist">
        <ul>
		<c:forEach var="item" items="${resultStrings}" >
			 <li><c:out value="${item}" /></li>
		</c:forEach>
		</ul>
		</div>
		</c:if>
        <form id="mainform" method="post" enctype="multipart/form-data">
	      <input type="hidden" id="action2"/>
        </form>
		<a href="#" onclick="document.getElementById('action2').name='_ok';document.forms.mainform.submit()" class="button"><spring:message code="button.return"/></a>
        </c:when>
        </c:choose>
</tiles:putAttribute>
</tiles:insertTemplate>