<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep /><cyb:crumb mlkey="languagepacks.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="languagepacks.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="languagepacks.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
                <table>
                    <tr><th><spring:message code="languagepack.code"/></th><th><spring:message code="languagepack.language"/></th></tr>
                    <c:forEach items="${list}" var="languagePack">
                    	<tr>
                    		<td><a href="languagepackeditor.htm?languagePackId=${languagePack.id}"><c:out value="${languagePack.locale}"/></a></td>
                    		<td><a href="multilanguage.htm?languagePack=${languagePack.id}">
                    				<c:out value="${languagePack.name}"/></a></td>
                    		<td class="icons">
                                <a href="languagepackeditor.htm?languagePackId=${languagePack.id}" title="<spring:message code="edit"/>">
                                    <img src="themes/default/button_edit.gif"/></a>
                                <a href="deletelanguagepack.htm?languagePackId=${languagePack.id}" title="<spring:message code="delete"/>">
                                    <img src="themes/default/button_delete.gif"/></a>
                    			<a href="languagepackexport.htm?languagePack=${languagePack.id}" title="<spring:message code="export"/>">
                    				<img src="themes/default/button_export.gif"/></a>
                    		</td>    
                    	</tr>
                    </c:forEach>
                </table>
                <br/>
                <a href="<c:url value="languagepackeditor.htm"/>" class="button"><spring:message code="languagepack.new"/></a>
                <a href="<c:url value="languagepackimport.htm"></c:url>" class="button" ><spring:message code="languagepack.import"/></a>
                <a href="<c:url value="systemadministration.htm"/>" class="button" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>