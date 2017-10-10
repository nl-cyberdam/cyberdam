<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_basic.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep /><cyb:crumb link="systemadministration.htm" mlkey="systemadministration.title"/><cyb:csep /><cyb:crumb link="languagepacks.htm" mlkey="languagepacks.title"/><cyb:csep /><cyb:crumb mlkey="multilanguage.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" ><spring:message code="multilanguage.title"/> <c:out value="${languagePack.name}" /></tiles:putAttribute>
  <tiles:putAttribute name="introduction"><spring:message code="multilanguage.introduction"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
<c:set var="lnk" ><c:url value="multilanguage.htm"><c:param name="languagePack" value="${param.languagePack}" /></c:url></c:set>
<c:set var="refreshLink" ><c:url value="${lnk}"><c:param name="forceRefresh" value="true"/></c:url></c:set>
<div><spring:message code="language" />: <spring:message code="languagepacks.${languagePack.name}"/></div>
<form id="mainform" action="" method="POST">
        <%@ include file="textfilter.jsp" %>
<table class="usertable">
<thead>
    <tr class="title">
        <th><cyb:sortlink lnk="${lnk}" sort="code"><spring:message code="multilanguage.code"/></cyb:sortlink></th>
        <th><cyb:sortlink lnk="${lnk}" sort="message"><spring:message code="multilanguage.message"/></cyb:sortlink></th>
    </tr>
</thead>
<tbody>
    <c:forEach items="${multilanguagelist.pageList}" var="message">
        <tr><td><a href="<c:url value="editmultilanguagemessage.htm"><c:param name="id" value="${message.id}"/><c:param name="languagePack" value="${languagePack.id}"/></c:url>"><c:out value="${message.code}"/></a></td>
    		<td><c:out value="${message.message}"/></td>
    		<td class="icons"><a href="<c:url value="editmultilanguagemessage.htm"><c:param name="id" value="${message.id}"/><c:param name="languagePack" value="${languagePack.id}"/></c:url>" title="<spring:message code="link.edit"/>"><img src="themes/default/button_edit.gif"/></a>
        		<a href="<c:url value="deletemultilanguagemessage.htm"><c:param name="id" value="${message.id}"/></c:url>" onclick="return confirmdeleteandredirect('<spring:message code="confirm.delete"/>','<c:out value="${message.message}"/>');" title="<spring:message code="link.delete"/>"><img src="themes/default/button_delete.gif"/></a>
		</td></tr>    
    </c:forEach>
</tbody>
</table>

<cyb:pagernav pagedListHolder="${multilanguagelist}" lnk="${lnk}"/>
		<cyb:pager pagedListHolder="${multilanguagelist}" />
        </form>
        <br />
		<c:url value="editmultilanguagemessage.htm" var="newmessagelink"><c:param name="languagePack" value="${param.languagePack}" /></c:url>
		<a href="${newmessagelink}" class="button" ><spring:message code="link.addnewmultilanguagemessage"/></a>
		<br /><hr /><br />
		<a class="button back" href="<c:url value="languagepacks.htm"/>" ><spring:message code="link.back"/></a>
  </tiles:putAttribute>
</tiles:insertTemplate>
