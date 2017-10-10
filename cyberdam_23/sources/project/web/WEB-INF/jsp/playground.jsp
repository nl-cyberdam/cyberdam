<%@ include file="includes.jsp" %>
<tiles:insertTemplate template="layout_playgroundwindow.jsp">
  <tiles:putAttribute name="breadcrumb" ><cyb:crumb link="index.htm" mlkey="homepage.title"/><cyb:csep/><cyb:crumb link="gameparticipant.htm" mlkey="gameparticipant.title"/><cyb:csep/><cyb:crumb mlkey="session.title"/></tiles:putAttribute>
  <tiles:putAttribute name="title" >${playground.name} <spring:message code="playground.title"/></tiles:putAttribute>
  <tiles:putAttribute name="introduction">${playground.caption}</tiles:putAttribute>
  <tiles:putAttribute name="menu">
  <%@ include file="session_leftnav.jsp" %>
  </tiles:putAttribute>
  <tiles:putAttribute name="content">
  <c:choose>
  	<c:when test="${externalSwf}">
  		<a href="${playgroundLink}" target="_blank"><c:out value="${playgroundLink}"/></a>
  	</c:when>
 	<c:otherwise>
    <div id="flashcontent" style="display: none">
		Detecting flash player...
	</div>
    <div id="scriptcontent" style="position: relative;">
		<img id="mapImage" style="position: relative;" />
		<div id="infoPanel" style="z-index: 1; display: none; position: absolute; width: 200px; right: 50px; top: 10px; border: solid black 2px; background: white;">
		<!-- tsjee, dit ziet er net uit als een Straat kaart in Monopoly! -->
			<div id="infoTitleBar" style="background: green; height: 30px; overflow: hidden; color: black; border-bottom: solid black 2px; ">
				<div id="infoClose" style="float: right; border-left: solid black 2px; border-bottom: solid black 2px; padding: 2px 4px"><strong><a href="#" onClick="closeInfoBox();return false;" title="Sluiten" style="text-decoration: none; color: black">X</a></strong></div>
				<div id="infoTitle" style="padding: 2px 4px 2px 4px; ">Stad Cyberdam Verzekeringen</div>
			</div>
			<div id="infoBodyText" style='padding: 2px 4px 2px 4px;' >
			Stad Cyberdam Verzekeringen is een middelgrote verzekeraar, landelijk opererend vanuit Zoetermeer.
			</div>
			<div style='padding: 2px 4px 2px 4px; position: relative; ' >
				<img id="infoThumbnail" src="" />
				<div style="position: absolute; bottom: 6px; right: 10px; background: green; padding: 3px 5px; border: solid black 1px;">
					<a id="infoLink" style="color: black; text-decoration: none" title="Bezoek dit object">Site</a>					
				</div>
			</div>
		</div>
	</div>
	<div>&nbsp;</div>
	<script type="text/javascript">
		// <![CDATA[
		if ("${playgroundLink}".match (/\.swf/))
		{
			var so = new SWFObject("${swfPath}/${playgroundLink}", "playground", "500", "500", "9", "#C9EDDA");
			so.addVariable("xmlroot", "<%=request.getContextPath()%>%2Fmap.xml%3FparticipantId%3D${param.participantId}%26playgroundId%3D${param.playgroundId}");
			so.addVariable("playgroundObjectId", "${param.playgroundObjectId}");
			if ("${playgroundLink}" == "static-map-digidam.swf") 
			{
				so.addVariable("dynamicSwf", "<%=request.getContextPath()%>/${swfPath}/dynamic-map-digidam.swf")
			}
			else
			{
				so.addVariable("dynamicSwf", "<%=request.getContextPath()%>/${swfPath}/dynamic-map.swf")
			}
			so.write("flashcontent");
			document.getElementById ("flashcontent").style.display = "block"
			document.getElementById ("scriptcontent").style.display = "none"
		}
		else
		{
			var objects = { }
			
			function openInfoPanel (childId)
			{
				//alert (childId)
				//alert (objects[childId].name)
				var object = objects[childId]
				document.getElementById ("infoTitle").childNodes[0].nodeValue = object.name 
				document.getElementById ("infoBodyText").childNodes[0].nodeValue = object.description 
				document.getElementById ("infoThumbnail").src = object.thumbnailURL
				document.getElementById ("infoLink").href = object.URL
				var panel = document.getElementById ("infoPanel")
				panel.style.display = "block"
			}
			
			function closeInfoBox ()
			{
				var panel = document.getElementById ("infoPanel")
				panel.style.display = "none"
			}
			
			var xmlRequest
			
			function parseXML ()
			{
				if (xmlRequest.readyState == 4)
				{
					var panel = document.getElementById ("scriptcontent")
					panel.style.width = panel.getElementsByTagName ("img")[0].width + "px"
					//panel.style.display = "block"
				
					function textContent (node)
					{
						if (node.textContent)
							return node.textContent
						var text = ""
						for (var textNode = 0 ; textNode < node.childNodes.length ; ++textNode)
						{
							text = text + node.childNodes[textNode].nodeValue
						}
						return text
					}
					
					var xmlDoc = xmlRequest.responseXML.documentElement
					var children = xmlDoc.childNodes
					var child = children[0]
					while (child)
					{
						var num = child.getAttribute ("num")
						var childId = textContent (child.getElementsByTagName ("id")[0])
						var childName = textContent (child.getElementsByTagName ("name")[0])
						var childURL = textContent (child.getElementsByTagName ("url")[0])
						var childType = textContent (child.getElementsByTagName ("type")[0])
						var childThumbnailURL = textContent (child.getElementsByTagName ("thumbnail_url")[0])
						var childDescription = textContent (child.getElementsByTagName ("description")[0])
						var childX = textContent (child.getElementsByTagName ("locationx")[0])
						var childY = textContent (child.getElementsByTagName ("locationy")[0])
						
						objects[childId] = { "id": childId, "name" : childName, "URL" : childURL, "type" : childType, "thumbnailURL" : childThumbnailURL, "description" : childDescription } 
						var widget = document.createElement ("div")
						var picture = document.createElement ("img")
						var anchor = document.createElement ("a")
						anchor.href = "#"
						var func = "new Function ('openInfoPanel (" + childId + "); return false;')"
						anchor.onclick = eval (func)
						anchor.title = childName
						picture.src = "themes/default/map_location.png"
						anchor.appendChild (picture)
						widget.appendChild (anchor)
						widget.style.position = "absolute"

						widget.style.left = childX + "px"
						widget.style.top = childY + "px"
						if (childX >= 0 && childY >= 0)
						{
							document.getElementById ("scriptcontent").appendChild (widget)
						}
						child = child.nextSibling
					}
				}
			}
			
			function loadXML ()
			{
				try
				{
					xmlRequest = new ActiveXObject ("Msxml2.XMLHTTP")
				}
				catch (e)
				{
					try
					{
						xmlRequest = new XMLHttpRequest ()
					}
					catch (e)
					{
						alert (e.message)
						return
					}
				}
				xmlRequest.onreadystatechange = parseXML
				xmlRequest.open ("GET", "xmlmap.htm?participantId=${param.participantId}&playgroundId=${param.playgroundId}", true)
				xmlRequest.send (null)
			}
			
			loadXML ()
			if ("${playgroundLink}".match(/^http:\/\//) != null)
			{
				document.getElementById ("mapImage").src = "${playgroundLink}"
			}
			else
			{
				document.getElementById ("mapImage").src = "images/${playgroundLink}"
			}
			// document.getElementById ("scriptcontent").style.display = "block"
		}	
		// ]]>
	</script>
  	</c:otherwise>
  </c:choose>
  </tiles:putAttribute>
</tiles:insertTemplate>