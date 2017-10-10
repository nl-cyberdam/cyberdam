FCKConfig.ToolbarSets["Cyberdam"] = [
	['Source','DocProps','-','Preview'],
	['Cut','Copy','Paste','PasteText','PasteWord','-','Print','SpellCheck'],
	['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
	['Style'],
	['TextColor','BGColor'],
	['FitWindow','ShowBlocks','-','About'],
	'/',
	['Bold','Italic','StrikeThrough','-','Subscript','Superscript'],
	['OrderedList','UnorderedList','-','Outdent','Indent','Blockquote'],
	['JustifyLeft','JustifyCenter','JustifyRight','JustifyFull'],
	['Link','Unlink','Anchor'],
	['Image','Flash','Table','Rule','Smiley','SpecialChar','PageBreak']
		// No comma for the last row.
];

FCKConfig.CustomStyles = 
{
	'Titel'	: { Element : 'h3' },
	'Alineakop'	: { Element : 'h4' },
	'Standaard'	: { }
};

FCKConfig.ImageUpload = false;
FCKConfig.ImageDlgHideAdvanced = true;
FCKConfig.ImageDlgHideLink = true;
FCKConfig.ImageBrowser = false;
