<%@tag body-content="empty"
%><%@attribute name="textareaid" required="true"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%>
<script type="text/javascript">
        var oFCKeditor1 = new FCKeditor( '${textareaid}' ) ;
        oFCKeditor1.Config["CustomConfigurationsPath"] =  "<c:url value="/js/customfckeditor.js"/>";
        oFCKeditor1.BasePath = '<c:url value="/fckeditor/" />';
        oFCKeditor1.ToolbarSet = "Cyberdam" ;
        oFCKeditor1.Height = 400 ;
        oFCKeditor1.ReplaceTextarea() ;
</script>