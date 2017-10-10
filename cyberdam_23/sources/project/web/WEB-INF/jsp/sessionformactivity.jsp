<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_menu_session.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="sessionformactivity.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="sessionformactivity.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
  <h1 class="activityname" ><c:out value="${activity.name}"/></h1>
  <h2 class="activityinstructions" ><spring:message code="instructions"/></h2>
  <div class="activityinstructions" ><c:out value="${substitutedInstructions}" escapeXml="false"/></div>
  <h2 class="activityattachments" ><spring:message code="activity.attachments"/></h2>
  <c:forEach items="${activity.sortedAttachments}" var="attachment" varStatus="varStatus" >
	  <c:url value="/resource" var="resourceurl" >
	  <c:param name="resourceId" value="${attachment.id}" />
	  </c:url>
	  <a href="${resourceurl}" target="_blank"> <c:out value="${attachment.name}" /></a>
	  <br/>
  </c:forEach>
  <c:if test="${empty activity.attachments}" ><spring:message code="activity.no_attachments"/></c:if>
  <br/><br/>
  <form:form id="mainform" commandName="varList">
        <table class="edit">
        <tr>
        	<th><spring:message code="caption"/></th><th><spring:message code="value"/></th>
        </tr>
          <c:if test="${empty varList.variableValues}">
			<tr>
				<td colspan="2"><spring:message code="variable.emptylist"/></td>
			</tr>
		  </c:if>
		  <c:forEach items="${varList.variableValues}" var="variableValue" varStatus="varStatus" >
		  <tr>
		    <td class="first<c:if test="${not varList.enabledList[varStatus.index]}"> disabled</c:if>">
		    ${variableValue.activityVariable.caption}
		    </td>
            <td>
         	   <c:if test="${varList.enabledList[varStatus.index]}">
         	   <spring:bind path="variableValues[${varStatus.index}].value">
                 <form:input cssStyle="width:100%" path="${status.expression}"/>
         	   </spring:bind>
		  	  </c:if>
			<c:if test="${not varList.enabledList[varStatus.index]}">
				<input style="width:100%" value="${varList.variableValues[varStatus.index].value}" disabled/>
			</c:if>
		   </td>
		   <form:errors path="variableValues[${varStatus.index}].value">
		   <td style="width:120px;border:0px">
		     <form:errors path="variableValues[${varStatus.index}].value" cssClass="errorBox"/>
		   </td>
		   </form:errors>
		  </tr> 
      	  </c:forEach>
        </table>
          <input type="hidden" name="activityId" value="${activity.id}" >
  	  	  <input type="hidden" name="participantId" value="${participant.id}" >
	      <input type="submit" style="display:none"/>
	      <input type="hidden" id="action"/>
  </form:form>
	    <table class="noborders"><tr>
			<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
	    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
	    </tr></table>
  </tiles:putAttribute>
</tiles:insertTemplate>
