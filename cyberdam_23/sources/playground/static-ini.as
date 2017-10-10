/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
						Initialize Variables
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^*/
//url to the xml:
//xmlURL = "citybuild/map-xml";
//Write the next line to make shure the xml refreshes (only works online):
//xmlURL += "?rnd=" + random(9999)



ArrayNames = ["_level10.movieClipName_array", "_level10.ObjectsName_array", "_level10.webUrl_array", "_level10.jpgUrl_array", "_level10.Description_array", "_level10.LocationX_array", "_level10.LocationY_array", "_level10.Type_array"];
_level10.movieClipName_array = new Array();
_level10.ObjectsName_array = new Array();
_level10.webUrl_array = new Array();
_level10.jpgUrl_array = new Array();
_level10.Description_array = new Array();

_level10.LocationX_array = new Array();
_level10.LocationY_array = new Array();
_level10.Type_array = new Array();

_level10.xmlDataLoaded = false;

/* ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
						Load XML Data
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^*/
if(!alreadyLoading){
	
	stageObjects_xml = new XML();
	stageObjects_xml.ignoreWhite = true;
	stageObjects_xml.onLoad = function(success) {
		if (success) {
			alreadyLoading = true;
			processData(stageObjects_xml);
		}
	};	
	stageObjects_xml.load(xmlroot);
}

function processData(doc_xml) {
	// doc_xml is now a reference to the XML object where the information is stored
//	data = new Object(); //don't think we'll be needing this one!
	for (var n = 0; n<doc_xml.firstChild.childNodes.length; n++) { 	//loops for all the Children
		for (var m = 0; m<ArrayNames.length; m++) 	{									//for all the ArrayNames pushes the data in
			eval(ArrayNames[m]).push(doc_xml.firstChild.childNodes[n].childNodes[m].firstChild.nodeValue);
		}
	}
			for (var m = 0; m<ArrayNames.length; m++) {
			trace(eval(ArrayNames[m]).toString())// displays the 5 arrays
			trace("number of objects: " add doc_xml.firstChild.childNodes.length)
		}
	_level10.xmlDataLoaded = true;
}
