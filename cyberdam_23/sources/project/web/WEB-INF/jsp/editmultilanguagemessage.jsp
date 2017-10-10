<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_simple.jsp">
  <tiles:putAttribute name="title" ><spring:message code="editmultilanguagemessage.title"/></tiles:putAttribute>
  <tiles:putAttribute name="content">
  			<spring:message code="language" />: <spring:message code="languagepacks.${multilanguagemessage.languagePack.name}"/>
            <form:form id="mainform" commandName="multilanguagemessage" >
            <table class="edit">
                <tr><td colspan="2"><form:errors path="*" cssClass="errorBox" /></td></tr>
                
                <tr><td class="first"><form:label path="code"><spring:message code="multilanguage.code" /></form:label></td>
                <td><form:input path="code" /></td></tr>
                <tr><td><spring:message code="multilanguage.edit.richtext" /></td><td><a href="#" onClick="toggleRichText(); return false;"><spring:message code="toggle" /></a></td></tr>
                <tr><td><form:label path="message"><spring:message code="multilanguage.message" /></form:label></td>
                <td class="richeditcell"><form:textarea id="editfield" rows="30" cols="100" path="message" /></td></tr>
		        </table>
			      <input type="submit" style="display:none"/>
			      <input type="hidden" id="action"/>
		  	    </form:form>
			    <table class="noborders"><tr>
					<td><a href="#" onclick="document.forms.mainform.submit()" class="button"><spring:message code="button.save"/></a></td>
			    	<td><a href="#" onclick="document.getElementById('action').name='_cancel';document.forms.mainform.submit()" class="button"><spring:message code="button.cancel"/></a></td>
			    </tr></table>
        <script type="text/javascript">  
        var oFCKeditor1 = new FCKeditor( 'editfield' ) ;
        oFCKeditor1.Config["CustomConfigurationsPath"] = '<c:url value="/js/customfckeditor.js" />';
        oFCKeditor1.BasePath = '<c:url value="/fckeditor/" />';
        oFCKeditor1.ToolbarSet = "Cyberdam" ;
        oFCKeditor1.Height = 400 ;
        var richtext = false;
        var editor;
        function toggleRichText() {
        	if (richtext) {
        		// replace the fck editor with simple textarea
        		// remove fckeditor frame (using prototype function)
        		var content = editor.GetHTML()
        		// window.alert(content);
        		Element.remove(oFCKeditor1.InstanceName + '___Frame');
        		
        		// put back the textarea
        		var oTextarea = document.getElementById( oFCKeditor1.InstanceName ) ;
        		// replace content
        		oTextarea.value = content;
        		
        		oTextarea.style.display = 'block' ;
        		richtext = false;
        		
        	} else {
        		oFCKeditor1.ReplaceTextarea() ;
        		richtext = true;
        	}
        }
        function FCKeditor_OnComplete( editorInstance )
		{
    		// window.alert( editorInstance.GetHTML() ) ;
    		editor = editorInstance;
		}
        </script>
  </tiles:putAttribute>
</tiles:insertTemplate>