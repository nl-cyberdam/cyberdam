<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
	<tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep /><cyb:crumb link="languagepacks.htm.htm" mlkey="languagepacks.title"/><cyb:csep /><cyb:crumb mlkey="importlanguagepack.title"/></tiles:putAttribute>
    <tiles:putAttribute name="title" ><spring:message code="importlanguagepack.title"/></tiles:putAttribute>
    <tiles:putAttribute name="introduction"><spring:message code="importlanguagepack.introduction"/></tiles:putAttribute>
    <tiles:putAttribute name="content">  
        <c:if test="${! empty errors}" >
	        <span id="*.errors" class="errorBox">
	        <c:forEach items="${errors}" var="error" >
	           <spring:message code="${error.code}" /><br />
	        </c:forEach>
	        </span>
        </c:if>
        <c:choose>
            <c:when test="${areyousure == 1}">
                 <br/>
                 <spring:message code="importlanguagepack.exists"/>
                 <form:form id="mainform">
                    <input type="hidden" id="action"/>
		  	    </form:form>
		  	    <table class="noborders"><tr>
		  	    <td><a href="#" onclick="document.getElementById('action').name='_OK';document.forms.mainform.submit()" class="button"><spring:message code="overwrite"/></a></td>
		  	    <td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
		  	    </tr></table>
            </c:when>
            <c:otherwise>
				<form:form id="mainform" method="post" enctype="multipart/form-data">
				    <input type="file" name="file"/>
                    <input type="hidden" id="action"/>
				</form:form>
		  	    <table class="noborders"><tr>
		  	    <td><a href="#" onclick="document.getElementById('action').name='_upload';document.forms.mainform.submit()" class="button"><spring:message code="upload"/></a></td>
		  	    <td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
		  	    </tr></table>
            </c:otherwise>
        </c:choose>
    </tiles:putAttribute>
</tiles:insertTemplate>