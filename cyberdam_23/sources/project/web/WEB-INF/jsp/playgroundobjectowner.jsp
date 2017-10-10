<%@ include file="includes.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<tiles:insertTemplate template="layout_playgroundwindow.jsp">
  <tiles:putAttribute name="title" ><c:out value="${playgroundobject.playground.name}"/>, <c:out value="${playgroundobject.name}"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="playgroundobject.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
	  <form:form id="mainform">
		<input type="hidden" name="action" id="action" value="_submit"/>
	     <table class="edit"><tbody style="vertical-align: top">
	         <tr>
	         	<td class="first"><spring:message code="name" /></td>
	         	<td><c:out value="${command.sessionObject.playgroundObject.name}" /></td></tr>
	         <tr>
				<td><spring:message code="description" /></td>
				<td class="richeditcell">
					<textarea id="defaultDescription" style="display:none">${defaultDescription}</textarea>
	        		<form:textarea id="descriptiontextarea" path="sessionObject.description"/>
	        	</td>
	        </tr>
	         <tr>
	         <td><spring:message code="label.downloads"/></td>
	         <td>
	            <c:forEach items="${command.participant.sessionResources}" var="resource" varStatus="rowCounter">
	            	<form:checkbox path="participant.sessionResources[${rowCounter.index}].published" />${resource.name}<br/>
	            </c:forEach>
	         </td>
	         </tr>
	     </tbody></table>
     </form:form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    	<td><a href="#" onclick="document.forms.mainform.descriptiontextarea.value=document.forms.mainform.defaultDescription.value;resetEditors()" class="button"><spring:message code="button.reset"/></a></td>
	    </tr></table>
     <cyb:richtext textareaid="descriptiontextarea" />
  </tiles:putAttribute>
</tiles:insertTemplate>
