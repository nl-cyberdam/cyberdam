<%@ page	language="java"
			contentType="text/html; charset=ISO-8859-1"
			pageEncoding="ISO-8859-1"%>
<script type="text/javascript">
	Event.observe
	(	window, 'load',
		function()
		{
			var form    = "mainform";
			Form.getElements(form).each
			(
				function(e)
				{
					Form.Element.disable(e);
				}
			);
		}		
	);
</script>
