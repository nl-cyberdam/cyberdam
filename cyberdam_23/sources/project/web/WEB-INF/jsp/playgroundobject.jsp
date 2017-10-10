<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_playgroundwindow.jsp">
  <tiles:putAttribute name="title" ><c:out value="${playgroundobject.playground.name}"/>, <c:out value="${playgroundobject.name}"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="playgroundobject.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
	<script type="text/javascript">
	    Event.observe (window, 'load',
	        function() {
	    	   $$("div.descriptiondiv a").each( function(a) {
		    	   if (a.target == "") {
		    		   a["target"] = "_blank";
		    	   }
	    	   })
	    	}
	    );
	</script>
     <table class="edit"><tbody style="vertical-align: top">
         <tr><td class="first"><spring:message code="name" /></td><td><c:out value="${playgroundobject.name}" /></td></tr>
         <tr><td><spring:message code="category" /></td><td><spring:message code="category.${playgroundobject.category}" /></td></tr>
         <tr><td><spring:message code="caption" /></td><td><c:out value="${playgroundobject.caption}" /></td></tr>
         <tr><td><spring:message code="address" /></td><td><c:out value="${playgroundobject.address}" /></td></tr>
         <tr><td><spring:message code="picture" /></td>
         <td>
         <div class="playgroundobjectimage">
         <c:choose>
         <c:when test="${! empty playgroundobject.picture}">
			<c:url value="/resource" var="resourceurl" ><c:param name="resourceId" value="${playgroundobject.picture.id}" /></c:url>
         	<img src="${resourceurl}"/>
         </c:when>
         <c:otherwise><spring:message code="noimage" /></c:otherwise>
         </c:choose>
         </div>
         </td></tr>
         <tr><td><spring:message code="description" /></td><td><div class="descriptiondiv"><c:out value="${description}" escapeXml="false"/></div></td></tr>
         <c:if test="${playgroundobject.onMap}">
         <tr><td><spring:message code="playgroundobject.link" /></td><td>
         <c:choose>
         <c:when test="${! empty playgroundobject.url}">
         <a href="${playgroundobject.url}" target="_blank">${playgroundobject.url}</a>
         </c:when>
         <c:otherwise>
         <c:url value="playground.htm" var="playgroundlink">
         <c:param name="playgroundId" value="${playgroundobject.playground.id}"/>
         <c:param name="playgroundObjectId" value="${playgroundobject.id}"/>
         <c:param name="participantId" value="${participant.id}"/>
         </c:url>
         <a href="${playgroundlink}"><spring:message code="playgroundobject.maplink" /></a>
         </c:otherwise>
         </c:choose>
         </td></tr>
         </c:if>
         <c:if test="${! empty sessionResources }">
         	<tr><td><spring:message code="label.downloads"/></td>
         	<td>
	            <c:forEach items="${sessionResources}" var="resource" varStatus="rowCounter">
	            	<c:url value="sessionresource" var="openlink"><c:param name="id" value="${resource.id}" /></c:url>
	            	<a href="${openlink}" title="<spring:message code="open"/>">${resource.name}</a>
	            </c:forEach>
         	</td>
         </c:if>
     </tbody></table>
     <c:if test="${canEdit}">
         <table class="noborders">
	         <tr>
				<td>
	            	<c:url value="playgroundobjectowner.htm" var="playgroundobjectownerlink">
						<c:param name="id" value="${playgroundobject.id}" />
						<c:param name="participantId" value="${participant.id}" />
	            	</c:url>
					<a href="${playgroundobjectownerlink}" class="button nomargin"><spring:message code="playgroundobject.edit"/></a>
				</td>
			</tr>
		</table>
	     
     </c:if>
  </tiles:putAttribute>
</tiles:insertTemplate>
