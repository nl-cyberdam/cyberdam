<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <c:choose>
  <c:when test="${viewerMode}">
    <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="playgroundlistpage.htm" mlkey="playgroundlistpage.title"/><cyb:csep /><cyb:crumb link="playgrounddetail.htm?id=${command.playground.id}" mlkey="playgrounddetail.title"/><cyb:csep /><cyb:crumb mlkey="playgroundobjectviewer.title"/></tiles:putAttribute>
  	<tiles:putAttribute name="title" ><spring:message code="playgroundobjectviewer.title"/> <c:out value="${command.name}" /></tiles:putAttribute>
  	<tiles:putAttribute name="introduction"><spring:message code="playgroundobjectviewer.introduction"/></tiles:putAttribute>
  </c:when>
  <c:otherwise>
  	<tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="playgroundlistpage.htm" mlkey="playgroundlistpage.title"/><cyb:csep /><cyb:crumb link="playgrounddetail.htm?id=${command.playground.id}" mlkey="playgrounddetail.title"/><cyb:csep /><cyb:crumb mlkey="playgroundobjecteditor.title"/></tiles:putAttribute>
  	<tiles:putAttribute name="title" ><spring:message code="playgroundobjecteditor.title"/> <c:out value="${command.name}" /></tiles:putAttribute>
  	<tiles:putAttribute name="introduction"><spring:message code="playgroundobjecteditor.introduction"/></tiles:putAttribute>
  </c:otherwise>
  </c:choose>
  <tiles:putAttribute name="content">
  		<c:if test="${viewerMode}"><%@ include file="include_formdisables.jsp" %></c:if>
        <form:form id="mainform">
        <table class="edit">
        <tr><td colspan="2"><form:errors path="*" cssClass="errorBox" /></td></tr>
        <tr><td class="first"><form:label path="uri"><spring:message code="uri"/></form:label></td><td><form:input path="uri" cssClass="fill" /></td></tr>
        <tr><td><form:label path="name"><spring:message code="name"/></form:label></td><td><form:input path="name" cssClass="fill" /></td></tr>                
        <tr><td><spring:message code="caption" /></td><td><form:textarea path="caption" cssClass="fill" /></td></tr>
        <tr><td><spring:message code="address" /></td><td><form:textarea path="address" cssClass="fill" /></td></tr>
        <tr><td><form:label path="category"><spring:message code="category"/></form:label></td>
            <td><form:select path="category">
            <c:forEach var="cat" items="${categoryOptions}" >
			  <form:option value="${cat}" ><spring:message code="category.${cat}"/></form:option>
			</c:forEach>
            </form:select></td></tr>
        <tr><td><spring:message code="description" /></td>
        <td class="richeditcell">
        	<c:choose>
        		<c:when test="${viewerMode}">
        			<c:out value="${command.description}" escapeXml="false"/>
        		</c:when>
        		<c:otherwise>
        			<form:textarea id="descriptiontextarea" path="description"/>
        		</c:otherwise>
        	</c:choose>
        </td></tr>
        <tr><td><spring:message code="thumbnail" /></td>
        <td>
        <c:choose>
        <c:when test="${command.id > 0}">
			<c:out value="${command.thumbnail.fileName}" /> 
			<c:if test="${!viewerMode}">
				<a href="resourceupload.htm?redirectTo=playgroundobjecteditor.htm&amp;id=${command.id}&target=thumbnail" class="button"><spring:message code="upload.new" /></a>
			</c:if>
        </c:when>
        <c:otherwise><spring:message code="please.save.first" /></c:otherwise>
        </c:choose>
        </td></tr>
        <tr><td><spring:message code="picture" /></td>
        <td>
        <c:choose>
        <c:when test="${command.id > 0}">
			<c:out value="${command.picture.fileName}" /> 
			<c:if test="${!viewerMode}">
				<a href="resourceupload.htm?redirectTo=playgroundobjecteditor.htm&amp;id=${command.id}&target=picture" class="button" ><spring:message code="upload.new" /></a>
			</c:if>
        </c:when>
        <c:otherwise><spring:message code="please.save.first" /></c:otherwise>
        </c:choose>
        </td></tr>
        <tr><td><spring:message code="url" /></td><td><form:input path="url" cssStyle="width:22em" /></td></tr>
        <tr><td><spring:message code="x" /></td><td><form:input path="x" /></td></tr>
        <tr><td><spring:message code="y" /></td><td><form:input path="y"/></td></tr>
        <tr><td><spring:message code="createdby" /></td><td><cyb:displayuser user="${command.creator}"/></td></tr>
        <tr><td><spring:message code="createdon" /></td><td><fmt:formatDate value="${command.created}" pattern="dd MMM yyyy HH:mm" /></td></tr>
        <tr><td><spring:message code="lastmutatedby" /></td><td><cyb:displayuser user="${command.lastModifier}"/></td></tr>
        <tr><td><spring:message code="lastmutatedon" /></td><td><fmt:formatDate value="${command.lastModified}" pattern="dd MMM yyyy HH:mm" /></td></tr>
        <tr><td><spring:message code="visibleonmap" /></td><td><form:checkbox path="onMap"/></td></tr>
        <tr><td><spring:message code="listedindirectory" /></td><td><form:checkbox path="inDirectory" /></td></tr>
        <tr><td><form:label path="status"><spring:message code="status"/></form:label></td>
            <td>
            <c:choose>
            <c:when test="${command.statusEditable}">
            <form:select path="status" >
            <c:forEach var="status" items="${statusOptions}" >
			  <form:option value="${status}" ><spring:message code="status.${status}"/></form:option>
			</c:forEach>
            </form:select>
            </c:when>
            <c:otherwise>
            <spring:message code="status.${command.status}" />
            </c:otherwise>
            </c:choose>
            </td></tr>
        </table>
	      <input type="submit" style="display:none"/>
	      <input type="hidden" id="action"/>
       </form:form>
        <hr />
	    <c:choose>
	    <c:when test="${viewerMode}">
	    	<a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.close"/></a>
	    </c:when>
	    <c:otherwise>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
	    </c:otherwise>
	    </c:choose>     		
       <c:if test="${!viewerMode}">
       		<cyb:richtext textareaid="descriptiontextarea" />
       </c:if>		
  </tiles:putAttribute>
</tiles:insertTemplate>
