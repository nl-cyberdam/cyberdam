<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_simple.jsp">
  <tiles:putAttribute name="title" ><spring:message code="uploadusers.title"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
		<h2><spring:message code="ImportResult" /></h2>
		<pre><c:out value="${result}" /></pre>
		<div><a href="${returnView}.htm?forceRefresh=true" class="button" ><spring:message
			code="backtouseradministrationpage" /></a></div>
	</tiles:putAttribute>
</tiles:insertTemplate>