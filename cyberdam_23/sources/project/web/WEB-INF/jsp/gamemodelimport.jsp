<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameauthor.htm" mlkey="gameauthor.title"/><cyb:csep/><cyb:crumb mlkey="gamemodelimport.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="gamemodelimport.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction">
    <c:choose>
        <c:when test="${form_type==2}">
            <spring:message code="gamemodelimportresult.introduction"/>
        </c:when>
        <c:otherwise>
            <spring:message code="gamemodelimport.introduction"/>
        </c:otherwise>
    </c:choose>   
  </tiles:putAttribute>
 <tiles:putAttribute name="content">
		<c:choose>
		<c:when test="${form_type==0}">
		<form name="form1" method="post" enctype="multipart/form-data">
            <input type="file" name="file"/>
			<input type="submit" style="display:none" name="_upload"/>
			<input type="hidden" id="action1"/>
        </form>
        <table class="noborders"><tr>
		<td><a href="#" onclick="document.getElementById('action1').name='_upload';document.form1.submit()" class="button"><spring:message code="upload"/></a></td>
		<td><a href="#" onclick="document.getElementById('action1').name='_cancel';document.form1.submit()" class="button"><spring:message code="cancel"/></a></td>
		</tr></table>
        </c:when>
        <c:when test="${form_type==2}">
        <c:if test="${resultStrings==null}">
        	<div class="errorBox"><spring:message code="gamemodelimportresult.no_gamemodel_found" /></div>
        </c:if>
        <c:if test="${resultStrings!=null}">
    	<div class="resultlist">
        <ul>
        <c:forEach var="item" items="${resultStrings}" >
        	<li><c:out value="${item}" /></li>
		</c:forEach>
		</ul>
		</div>
		</c:if>
        <form name="form2" method="post" enctype="multipart/form-data">
			<input type="hidden" id="action2"/>
        </form>
		<a href="#" onclick="document.getElementById('action2').name='_ok';document.form2.submit()" class="button"><spring:message code="button.return"/></a>
        </c:when>
        </c:choose>
</tiles:putAttribute>
</tiles:insertTemplate>