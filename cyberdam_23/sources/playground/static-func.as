// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// Initialize Variables
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
var mapSizeN;
// counter for array of mapsizes
var n;
// counter
var mapSizes = [50, 60, 75, 100, 140, 200, 280, 380];
// all the sizes of the map in %
var moveFactor = 10;
// number of pixels the map should move per step
var soButtonsPathRoot = "_level10.MapZoomable_mc.MapwithButtons_mc.SOButtons.";
// root of the path of the buttons
var soButtonsPath;
var fotoButtonsPathRoot = "_level10.MapZoomable_mc.MapwithButtons_mc.FOTOButtons.";
// root of the path of the buttons
var fotoButtonsPath;
var NavSquareX = _level10.NavWindow_mc.NavZoomable_mc._x;
var NavSquareY = _level10.NavWindow_mc.NavZoomable_mc._y;
// In the starting position is possible to drag the map
allowMapDraggin = true;
// mapsize would otherwise be unknown at the start (zoombuutons not being pushed yet)
getmapSize();
// Hide InfoScreen
_level10.infoScreen_mc._visible = false;
// Hide Streetnames and Photo Buttons
_level10.MapZoomable_mc.MapwithButtons_mc.streetNames_mc._visible = false;
_level10.MapZoomable_mc.MapwithButtons_mc.FOTOButtons._visible = false;
modus = "mini"; // form of the icons
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
function switchVisibilityNavigatie() {
	// of the navWindow and Legenda
	if (_level10.NavWindow_mc._visible == true) {
		_level10.NavWindow_mc._visible = false;
		_level10.NavButton_mc.gotoAndStop(1);
	} else {
		_level10.NavWindow_mc._visible = true;
		_level10.NavButton_mc.gotoAndStop(2);
		// hide the other window
		_level10.LegWindow_mc._visible = false;
		_level10.LegButton_mc.gotoAndStop(1);
	}
}
function switchVisibilityLegenda() {
	// of the navWindow and Legenda
	if (_level10.LegWindow_mc._visible == true) {
		_level10.LegWindow_mc._visible = false;
		_level10.LegButton_mc.gotoAndStop(1);
	} else {
		_level10.LegWindow_mc._visible = true;
		_level10.LegButton_mc.gotoAndStop(2);
		// hide the other window
		_level10.NavWindow_mc._visible = false;
		_level10.NavButton_mc.gotoAndStop(1);
	}
}
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
// hide Navigation and Legenda fields
switchVisibilityNavigatie();
switchVisibilityLegenda();
// Create RollOver text field for the mapIcons
_level10.createEmptyMovieClip("altText_mc", 2);
// ---------------------------------------------------------------
// B U T T O N S
// -----------------------------------------------------------------
// position altText_mc (which doesn't exist untill pushbutton) at mousepoint
this.onEnterFrame = function() {
	_level10.altText_mc._x = _xmouse;
	_level10.altText_mc._y = _ymouse;
	
	
};
// NavLegButtons_mc.navButton_btn.onRollOver = function() {
// btnTXTover = new TextFormat();
// btnTXTover.color = 0xFFFFFF
// NavLegButtons_mc.navigator_txt.setTextFormat(btnTXTover);
// }
// NavLegButtons_mc.navButton_btn.onRollOut = function() {
// btnTXTout = new TextFormat();
// btnTXTout.color = 0xCCFF00
// NavLegButtons_mc.navigator_txt.setTextFormat(btnTXTout);
// }
_level10.NavButton_mc.navButton_btn.onRelease = function() {
	switchVisibilityNavigatie();
};
_level10.LegButton_mc.legButton_btn.onRelease = function() {
	switchVisibilityLegenda();
};
_level10.MapZoomable_mc.MapwithButtons_mc.ppt_btn.onRelease = function() {
};
// PRINTS ALL THE BUTTONS ON THE MAP TO THE FEEDBACK WINDOW!!!
_level10.FeedBack_mc.feedBackWindow += "\nObjects found on map:\n";
for (each in _level10.MapZoomable_mc.MapwithButtons_mc.SOButtons) {
	// "each" can be called anything you want -
	// print objects to feedback window
	_level10.FeedBack_mc.feedBackWindow += _level10.MapZoomable_mc.MapwithButtons_mc.SOButtons[each]._name add "; ";
	// this writes the names to the output window, you could put them in an array or whatever.
}
_level10.FeedBack_mc.feedBackWindow += "\nObjects in XML:\n";
for (n=0; n<_level10.movieClipName_array.length; n++) {

	// Add button
	_level10.MapZoomable_mc.MapwithButtons_mc.SOButtons.attachMovie(_level10.Type_array[n], _level10.movieClipName_array[n], n);
	eval(soButtonsPathRoot+_level10.movieClipName_array[n])._x = 1.4*int(_level10.LocationX_array[n]) - 5*140;// - 2550;
	eval(soButtonsPathRoot+_level10.movieClipName_array[n])._y = 1.4*int(_level10.LocationY_array[n]) - 5*140;// - 2550;
	
	// print objects to feedback window
	_level10.FeedBack_mc.feedBackWindow += _level10.movieClipName_array[n] add "; ";
	soButtonsPath = eval(soButtonsPathRoot+_level10.movieClipName_array[n]);	
	// Rollover the Icons
	soButtonsPath.onRollOver = function() {
		// prevent mapdragging when mouse is on a button
		allowMapDraggin = false;
		trace("allowMapDraggin= " add allowMapDraggin);
		_level10.altText_mc._visible = true;
		for (m=0; m<_level10.movieClipName_array.length; m++) {
			// trace(this._name + "m=" + m + "/name=" + _level10.movieClipName_array[m]);
			if (this._name == _level10.movieClipName_array[m]) {
				// buttons instancename is now known
				// CheckWindowCoverage()
				// I should put a ref to a function here so I can use this more often
				// if (WindowCoverage == true) {
				// //make the buttons dissappear:
				// eval(eval(soButtonsPathRoot add _level10.movieClipName_array[m])._visible = false);
				// trace("!!!" add soButtonsPathRoot add _level10.movieClipName_array[m] add "._visible = false");
				// trace("!!!" add testi);
				// }
				_level10.altText_mc.createTextField("rolloverText", 1, 10, -20, 1, 1);
				if (_level10._xmouse>300) {
					_level10.altText_mc.rolloverText.autoSize = "right";
				} else {
					_level10.altText_mc.rolloverText.autoSize = "left";
				}
				// I should put a ref to a function here so I can use this more often
				CheckWindowCoverage();
				if (WindowCoverage == false) {
					_level10.altText_mc.rolloverText.border = true;
					_level10.altText_mc.rolloverText.background = true;
					_level10.altText_mc.rolloverText.selectable = false;
					_level10.altText_mc.rolloverText.wordWrap = false;
					_level10.altText_mc.rolloverText.text = String(_level10.ObjectsName_array[m]);
					// BUTTONBEHAVIOUR:
					with (eval(soButtonsPathRoot+_level10.movieClipName_array[m])) {
						//gotoAndStop(2);
						overmodus = modus add "2";
						gotoAndStop(overmodus);
						trace ("modus=" add overmodus);
						//nextFrame(); // changed in favor of the minibutton next to the original
					}
					trace(targetPath(this));
					format = new TextFormat();
					format.bold = true;
					format.font = "Verdana";
					_level10.altText_mc.rolloverText.setTextFormat(format);
				}
				break;
				// breaks the loop when name is found
			}
		}
	};
	soButtonsPath.onRollOut = function() {
		_level10.altText_mc._visible = false;
		allowMapDraggin = true;
		// BUTTONBEHAVIOUR:
		with (eval(soButtonsPathRoot+_level10.movieClipName_array[m])) {
			//gotoAndStop(1);
			trace ("modus=" add modus);
			gotoAndStop(modus);
			//prevFrame();
		}
	};
	soButtonsPath.onDragOut = function() {
		_level10.altText_mc._visible = false;
		allowMapDraggin = true;
		// BUTTONBEHAVIOUR:
		with (eval(soButtonsPathRoot+_level10.movieClipName_array[m])) {
			//gotoAndStop(1);
			//prevFrame();
			trace ("modus=" add modus);
			gotoAndStop(modus);
		}
	};
	// -------------------------------------------------------------------------------------
	// INFO WINDOW
	// -------------------------------------------------------------------------------------
	// Display the infoWindow with Content
	soButtonsPath.onRelease = function() {
		// make shure site button is visible because foto button hides it
		_level10.infoScreen_mc.site_btn._visible = true;
		// info will not be opened when button has windowcover
		CheckWindowCoverage();
		if (WindowCoverage == false) {
			_level10.infoScreen_mc._visible = true;
			_level10.infoScreen_mc.objectsName = String(_level10.objectsName_array[m]);
			// _level10.infoScreen_mc.movieClipName = string(_level10.movieClipName_array[m]); NO NEED FOT THIS INFO
			_level10.infoScreen_mc.description = String(_level10.description_array[m]);
			_level10.infoScreen_mc.webUrl = String(root_url add _level10.webUrl_array[m]);
			_level10.infoScreen_mc.jpgUrl = String(root_url add _level10.jpgUrl_array[m]);
			// the root_url (a var from the html) will be put before the image url next)
			_level10.infoScreen_mc.jpgHolder_mc.loadMovie(_level10.infoScreen_mc.jpgUrl);
			// If no image is defined in the xml then the following text will show:
			if (_level10.jpgUrl_array[m] == null) {
				_level10.infoScreen_mc.ImgAvailability = "Geen plaatje beschikbaar";
			} else {
				_level10.infoScreen_mc.ImgAvailability = "Plaatje wordt geladen..";
			}
		}
	};
}
// -------------------------------------------------------------------------------------
// FOTO BUTTONS
// -------------------------------------------------------------------------------------
// PRINTS ALL THE FOTOBUTTONS ON THE MAP!!!
_level10.fotoClipName_array = new Array();
// _level10.FeedBack_mc.feedBackWindow += "Straatbeelden found on map:\n";
for (eachp in _level10.MapZoomable_mc.MapwithButtons_mc.FOTOButtons) {
	// "eachp" can be called anything you want -
	// print objects to feedback window
	// DOUBLE!!!   _level10.FeedBack_mc.feedBackWindow += _level10.MapZoomable_mc.MapwithButtons_mc.FOTOButtons[eachp]._name add "; ";
	_level10.fotoClipName_array.push(_level10.MapZoomable_mc.MapwithButtons_mc.FOTOButtons[eachp]._name);
	// this writes the names to the output window, you could put them in an array or whatever.
}
_level10.FeedBack_mc.feedBackWindow += "\n" add _level10.fotoClipName_array.length add " straatbeelden found on map:\n";
for (n=0; n<_level10.fotoClipName_array.length; n++) {
	// print objects to feedback window
	// _level10.FeedBack_mc.feedBackWindow += _level10.fotoClipName_array[n] add "\n";
	fotoButtonsPath = eval(fotoButtonsPathRoot+_level10.fotoClipName_array[n]);
	// Rollover the Buttons
	fotoButtonsPath.onRollOver = function() {
		// prevent mapdragging when mouse is on a button
		allowMapDraggin = false;
		trace("WindowCoverage= " add WindowCoverage);
		trace("allowMapDraggin= " add allowMapDraggin);
		_level10.altText_mc._visible = true;
		for (p=0; p<_level10.fotoClipName_array.length; p++) {
			// trace(this._name + "p=" + p + "/name=" + _level10.fotoClipName_array[p]);
			if (this._name == _level10.fotoClipName_array[p]) {
				// buttons instancename is now known
				_level10.altText_mc.createTextField("rolloverText", 1, 10, -20, 1, 1);
				if (_level10._xmouse>300) {
					_level10.altText_mc.rolloverText.autoSize = "right";
				} else {
					_level10.altText_mc.rolloverText.autoSize = "left";
				}
				// I should put a ref to a function here so I can use this more often
				CheckWindowCoverage();
				if (WindowCoverage == false) {
					_level10.altText_mc.rolloverText.border = true;
					_level10.altText_mc.rolloverText.background = true;
					_level10.altText_mc.rolloverText.selectable = false;
					_level10.altText_mc.rolloverText.wordWrap = false;
					_level10.altText_mc.rolloverText.text = String(this.name);
					// BUTTONBEHAVIOUR:
					with (eval(fotoButtonsPathRoot+_level10.fotoClipName_array[p])) {
						gotoAndStop("over");
					}
					trace(targetPath(this));
					format = new TextFormat();
					format.bold = true;
					format.font = "Verdana";
					_level10.altText_mc.rolloverText.setTextFormat(format);
				}
				break;
				// breaks the loop when name is found
			}
		}
	};
	fotoButtonsPath.onPress = function() {
		// BUTTONBEHAVIOUR:
		with (eval(fotoButtonsPathRoot+_level10.fotoClipName_array[p])) {
			gotoAndStop("down");
		}
	};
	fotoButtonsPath.onRollOut = function() {
		_level10.altText_mc._visible = false;
		allowMapDraggin = true;
		// BUTTONBEHAVIOUR:
		with (eval(fotoButtonsPathRoot+_level10.fotoClipName_array[p])) {
			gotoAndStop("out");
		}
	};
	fotoButtonsPath.onDragOut = function() {
		_level10.altText_mc._visible = false;
		allowMapDraggin = true;
		// BUTTONBEHAVIOUR:
		with (eval(fotoButtonsPathRoot+_level10.fotoClipName_array[p])) {
			gotoAndStop("out");
		}
	};
	// Turn off mapdragging for the outlands!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
	_level10.MapZoomable_mc.MapwithButtons_mc.ppt_btn.onRollOver = function() {
		_level10.stop.startDrag(true);
		Mouse.hide();
		setProperty("_level10.stop", _visible, true);
		allowMapDraggin = false;
	};
	_level10.MapZoomable_mc.MapwithButtons_mc.ppt_btn.onRollOut = function() {
		setProperty("_level10.stop", _visible, false);
		stopDrag();
		Mouse.show();
		allowMapDraggin = true;
	};
	_level10.MapZoomable_mc.MapwithButtons_mc.ppt_btn.onDragOut = function() {
		setProperty("_level10.stop", _visible, false);
		stopDrag();
		Mouse.show();
		allowMapDraggin = true;
	};
	// -------------------------------------------------------------------------------------
	// SHOW INFOWINDOW FOR PHOTO'S
	// -------------------------------------------------------------------------------------
	// Display the infoWindow with Content
	fotoButtonsPath.onRelease = function() {
		with (eval(fotoButtonsPathRoot+_level10.fotoClipName_array[p])) {
			gotoAndStop("out");
		}
		// Hide site button because we will not need it
		_level10.infoScreen_mc.site_btn._visible = false;
		// info will not be opened when button has windowcover
		CheckWindowCoverage();
		if (WindowCoverage == false) {
			_level10.infoScreen_mc._visible = true;
			_level10.infoScreen_mc.objectsName = "Straatbeeld";
			// _level10.infoScreen_mc.movieClipName = string(_level10.movieClipName_array[m]); NO NEED FOT THIS INFO
			_level10.infoScreen_mc.description = this.name;
			_level10.infoScreen_mc.jpgUrl = straatbeelden_url add this.straatbeeldURL;
			// straatbeelden_url staat in map.swf gedefinieerd
			_level10.infoScreen_mc.jpgHolder_mc.loadMovie(_level10.infoScreen_mc.jpgUrl);
			// If no image is defined (=field is empty) in the map photo buttons then the following text will show:
			if (this.straatbeeldURL == null) {
				_level10.infoScreen_mc.ImgAvailability = "Geen plaatje beschikbaar";
			} else {
				_level10.infoScreen_mc.ImgAvailability = "Plaatje '" add this.straatbeeldURL add "' wordt geladen..";
			}
		}
	};
}
// drag the NavWindowSquare
_level10.infoScreen_mc.infoDragger_btn.onPress = function() {
	_level10.infoScreen_mc.startDrag(false, -100, 40, 400, 340);
};
_level10.infoScreen_mc.infoDragger_btn.onRelease = function() {
	stopDrag();
};
// close infoField window
_level10.infoScreen_mc.closer_btn.onRelease = function() {
	_level10.infoScreen_mc._visible = false;
};
// gotoURL
_level10.infoScreen_mc.site_btn.onRelease = function() {
	getURL(_level10.infoScreen_mc.webUrl);
};
// Do not allow dragging for CloseInfoFieldWindow and Site_btn:
_level10.infoScreen_mc.closer_btn.onRollOver = function() {
	allowMapDraggin = false;
};
_level10.infoScreen_mc.site_btn.onRollOver = function() {
	allowMapDraggin = false;
};
_level10.infoScreen_mc.closer_btn.onDragOut = function() {
	allowMapDraggin = true;
};
_level10.infoScreen_mc.site_btn.onDragOut = function() {
	allowMapDraggin = true;
};
_level10.infoScreen_mc.closer_btn.onRollOut = function() {
	allowMapDraggin = true;
};
_level10.infoScreen_mc.site_btn.onRollOut = function() {
	allowMapDraggin = true;
};
// Zoom
function mapResize() {
	// Scale Map
	// trace(mapSizes[mapSizeN]);
	_level10.MapZoomable_mc._xscale = _level10.MapZoomable_mc._yscale=mapSizes[mapSizeN];
	// resize thumb
	_level10.NavWindow_mc.NavZoomable_mc._xscale = _level10.NavWindow_mc.NavZoomable_mc._yscale=10000/Number(mapSizes[mapSizeN]);
	// Scale Buttons
	for (n=0; n<_level10.movieClipName_array.length; n++) {
		soButtonsPath = eval(soButtonsPathRoot+_level10.movieClipName_array[n]);
		soButtonsPath._xscale = soButtonsPath._yscale = 10000/Number(mapSizes[mapSizeN]);
		soButtonsPath._xscale = soButtonsPath._yscale = 10000/Number(mapSizes[mapSizeN]);
	}
	// Scale Photo Buttons
	for (n=0; n<_level10.fotoClipName_array.length; n++) {
		fotoButtonsPath = eval(fotoButtonsPathRoot+_level10.fotoClipName_array[n]);
		fotoButtonsPath._xscale = fotoButtonsPath._yscale = 10000/Number(mapSizes[mapSizeN]);
	}
	// Hide all the Icons if zoomed out much
//	if (Number(_level10.MapZoomable_mc._xscale)<80) {
//		_level10.MapZoomable_mc.MapwithButtons_mc.SOButtons._visible = false;
//	} else {
//		_level10.MapZoomable_mc.MapwithButtons_mc.SOButtons._visible = true;
//	}
	// Swap the buttons if zoomed out much
	if (Number(_level10.MapZoomable_mc._xscale)<=200) {
		for (n=0; n<_level10.movieClipName_array.length; n++) {
			with (eval(soButtonsPathRoot+_level10.movieClipName_array[n])) {
				modus = "mini";
				gotoAndStop(modus);
			}
		}
	} else {
		for (n=0; n<_level10.movieClipName_array.length; n++) {
			with (eval(soButtonsPathRoot+_level10.movieClipName_array[n])) {
				modus = "normal";
				gotoAndStop(modus);
			}
		}
	}
	// Hide all the tekst fot the streetnames if zoomed out much
	if (Number(_level10.MapZoomable_mc._xscale)>200) {
		_level10.MapZoomable_mc.MapwithButtons_mc.streetNames_mc._visible = true;
	} else {
		_level10.MapZoomable_mc.MapwithButtons_mc.streetNames_mc._visible = false;
	}
	// Hide all the Photo Buttons if zoomed out much
	if (Number(_level10.MapZoomable_mc._xscale)>200) {
		// or 280 or 140
		_level10.MapZoomable_mc.MapwithButtons_mc.FOTOButtons._visible = true;
	} else {
		_level10.MapZoomable_mc.MapwithButtons_mc.FOTOButtons._visible = false;
	}
	trace("Zoomfactor= " add mapSizes[mapSizeN]);
	// IMPORTANT - I always forget this variable!!!!!
}
// Next function is only needed if it possible to zoom out on startup
function getmapSize() {
	for (n=0; n<mapSizes.length; n++) {
		if (Number(_level10.MapZoomable_mc._xscale) == mapSizes[n]) {
			mapSizeN = n;
		}
	}
}
function enlarge() {
	getmapSize();
	if (mapSizeN<mapSizes.length-1) {
		mapSizeN += 1;
		// trace(mapSizeN)
		// trace(mapSizes[mapSizeN])
		mapResize();
		// correction for the NavSquare
	}
}
function shrink() {
	getmapSize();
	if (mapSizeN>0) {
		mapSizeN -= 1;
		// trace(mapSizeN)
		// trace(mapSizes[mapSizeN])
		mapResize();
	}
}
// Move
function moveDown() {
	_level10.MapZoomable_mc.MapwithButtons_mc._y -= moveFactor;
	// trace ("x= " add _level10.MapZoomable_mc.MapwithButtons_mc._x)
	// trace ("y= " add _level10.MapZoomable_mc.MapwithButtons_mc._y)
	_level10.NavWindow_mc.NavZoomable_mc._y += moveFactor/10;
}
function moveUp() {
	_level10.MapZoomable_mc.MapwithButtons_mc._y += moveFactor;
	// trace ("x= " add _level10.MapZoomable_mc.MapwithButtons_mc._x)
	// trace ("y= " add _level10.MapZoomable_mc.MapwithButtons_mc._y)
	_level10.NavWindow_mc.NavZoomable_mc._y -= moveFactor/10;
}
function moveRight() {
	_level10.MapZoomable_mc.MapwithButtons_mc._x -= moveFactor;
	// trace ("x= " add _level10.MapZoomable_mc.MapwithButtons_mc._x)
	// trace ("y= " add _level10.MapZoomable_mc.MapwithButtons_mc._y)
	_level10.NavWindow_mc.NavZoomable_mc._x += moveFactor/10;
}
function moveLeft() {
	_level10.MapZoomable_mc.MapwithButtons_mc._x += moveFactor;
	// trace ("x= " add _level10.MapZoomable_mc.MapwithButtons_mc._x)
	// trace ("y= " add _level10.MapZoomable_mc.MapwithButtons_mc._y)
	_level10.NavWindow_mc.NavZoomable_mc._x -= moveFactor/10;
}
// Zoom Buttons
_level10.zoomIn_btn.onRelease = function() {
	enlarge();
};
_level10.zoomOut_btn.onRelease = function() {
	shrink();
};
// Move Buttons:
_level10.moveUp_btn.onPress = function() {
	moveUp();
};
_level10.moveDown_btn.onPress = function() {
	moveDown();
};
_level10.moveLeft_btn.onPress = function() {
	moveLeft();
};
_level10.moveRight_btn.onPress = function() {
	moveRight();
};
_level10.moveUp_btn.onRollOver = function() {
	allowMapDraggin = false;
};
_level10.moveDown_btn.onRollOver = function() {
	allowMapDraggin = false;
};
_level10.moveLeft_btn.onRollOver = function() {
	allowMapDraggin = false;
};
_level10.moveRight_btn.onRollOver = function() {
	allowMapDraggin = false;
};
_level10.moveUp_btn.onDragOut = function() {
	allowMapDraggin = true;
};
_level10.moveDown_btn.onDragOut = function() {
	allowMapDraggin = true;
};
_level10.moveLeft_btn.onDragOut = function() {
	allowMapDraggin = true;
};
_level10.moveRight_btn.onDragOut = function() {
	allowMapDraggin = true;
};
_level10.moveUp_btn.onRollOut = function() {
	allowMapDraggin = true;
};
_level10.moveDown_btn.onRollOut = function() {
	allowMapDraggin = true;
};
_level10.moveLeft_btn.onRollOut = function() {
	allowMapDraggin = true;
};
_level10.moveRight_btn.onRollOut = function() {
	allowMapDraggin = true;
};
// Use +- and Arrow Keys as Zoom and Move Buttons:
keyListener = new Object();
keyListener.onKeyDown = function() {
	// trace (Key.getCode());
	if (Key.getCode() == 107 || Key.getCode() == 65) {
		// Num Pad + or A
		enlarge();
	}
	if (Key.getCode() == 109 || Key.getCode() == 90) {
		// Num Pad - or Z
		shrink();
	}
	if (Key.getCode() == Key.UP) {
		moveUp();
	}
	if (Key.getCode() == Key.DOWN) {
		moveDown();
	}
	if (Key.getCode() == Key.LEFT) {
		moveLeft();
	}
	if (Key.getCode() == Key.RIGHT) {
		moveRight();
	}
};
Key.addListener(keyListener);
// ---------------------------------------------------------------
// Drag with NavWindow
// -----------------------------------------------------------------
// 
// The square sometimes remained seated when dragged and released because a dragout
// had occured. This was no major problem and there was no great need to fix this
// Yet apperently I did by writing this onDragOver statement
_level10.NavWindow_mc.NavZoomable_mc.NavZoomButton_btn.onDragOver = function() {
	allowMapDraggin = false;
	trace("draggin is " add allowMapDraggin);
};
_level10.NavWindow_mc.NavZoomable_mc.NavZoomButton_btn.onRollOver = function() {
	allowMapDraggin = false;
	trace("draggin is " add allowMapDraggin);
};
_level10.NavWindow_mc.NavZoomable_mc.NavZoomButton_btn.onRollOut = function() {
	allowMapDraggin = true;
	trace("draggin is " add allowMapDraggin);
};
_level10.NavWindow_mc.NavZoomable_mc.NavZoomButton_btn.onDragOut = function() {
	allowMapDraggin = true;
	trace("draggin is " add allowMapDraggin);
};
// drag the NavSquare
_level10.NavWindow_mc.NavZoomable_mc.NavZoomButton_btn.onPress = function() {
	// Correction for the OutOfCenter NavSquare
	ZoomCorrectionNav = 1-(100/_levell0.mapSizes[_levell0.mapSizeN]);
	maxNavL = -42-(21*ZoomCorrectionNav);
	maxNavT = -45-(10*ZoomCorrectionNav);
	maxNavR = 29+(18*ZoomCorrectionNav);
	maxNavB = 25+(30*ZoomCorrectionNav);
	_level10.NavWindow_mc.NavZoomable_mc.startDrag(false, maxNavL, maxNavT, maxNavR, maxNavB);
	// ...startDrag(lockcenter,left top right bottom)
	startPosX = _level10.NavWindow_mc.NavZoomable_mc._x;
	startPosY = _level10.NavWindow_mc.NavZoomable_mc._y;
	trace("START: X=" add startPosX add ", Y=" add startPosY);
};
// move map to position
_level10.NavWindow_mc.NavZoomable_mc.NavZoomButton_btn.onRelease = function() {
	trace("draggin is " add allowMapDraggin);
	stopDrag();
	endPosX = _level10.NavWindow_mc.NavZoomable_mc._x;
	endPosY = _level10.NavWindow_mc.NavZoomable_mc._y;
	_level10.MapZoomable_mc.MapwithButtons_mc._x -= 10*(endPosX-startPosX);
	_level10.MapZoomable_mc.MapwithButtons_mc._y -= 10*(endPosY-startPosY);
	// onderstaaande werkt niet!!!!!!!!!!!!!1
	_level10.MapZoomable_mc.BuoyLT_mc._x -= 10*(endPosY-startPosY);
	_level10.MapZoomable_mc.BuoyLT_mc._y -= 10*(endPosY-startPosY);
	_level10.MapZoomable_mc.BuoyRB_mc._x -= 10*(endPosY-startPosY);
	_level10.MapZoomable_mc.BuoyRB_mc._y -= 10*(endPosY-startPosY);
};
// ---------------------------------------------------------------
// Drag the Stage
// -----------------------------------------------------------------
// The stage can be dragged but it will be ignored afterwards because the real movement
// of the stage will be happening by means of measuring the interval.
// The position of the stage (mapX and mapY) is recorded. The stage moves from there on mouseup.
// 
trace("infoScreen x=" add _level10.infoScreen_mc._x);
trace("infoScreen y=" add _level10.infoScreen_mc._y);
trace("infoScreen w=" add _level10.infoScreen_mc.width);
// Hide cursors at startup
setProperty("_level10.hand", _visible, false);
setProperty("_level10.stop", _visible, false);
function CheckWindowCoverage() {
	trace("_level10.infoScreen_mc._visible---" add _level10.infoScreen_mc._visible);
	if (_level10.NavWindow_mc._visible == true && _xmouse<160 && _ymouse<190) {
		// nothing happens when the navigation window is open and you drag inside
		WindowCoverage = true;
		trace("WindowCoverage" add WindowCoverage);
	} else if (_level10.LegWindow_mc._visible == true && _xmouse>115 && _xmouse<315 && _ymouse<275) {
		// nothing happens when the legenda window is open and you drag inside
		WindowCoverage = true;
		trace("WindowCoverage" add WindowCoverage);
	} else if (_xmouse<10 || _ymouse<40 || _xmouse>490 || _ymouse>490) {
		WindowCoverage = true;
		trace("WindowCoverage" add WindowCoverage);
		// Don't allow dragging of the map when mouse is on the infoWindow
	} else if (_level10.infoScreen_mc._visible == true && _xmouse>_level10.infoScreen_mc._x && _xmouse<_level10.infoScreen_mc._x+182 && _ymouse>_level10.infoScreen_mc._y && _ymouse<_level10.infoScreen_mc._y+245) {
		WindowCoverage = true;
		trace("WindowCoverage" add WindowCoverage);
	} else {
		WindowCoverage = false;
		trace("WindowCoverage" add WindowCoverage);
	}
}
_root.onMouseDown = function() {
	// I should put a ref to a function here so I can use this more often
	mapX = _level10.MapZoomable_mc.MapwithButtons_mc._x;
	mapY = _level10.MapZoomable_mc.MapwithButtons_mc._y;
	CheckWindowCoverage();
	if (allowMapDraggin == true && WindowCoverage == false) {
		navX = _level10.NavWindow_mc.NavZoomable_mc._x;
		navY = _level10.NavWindow_mc.NavZoomable_mc._y;
		trace("mapX=" add mapX add ", mapY=" add mapY);
		trace("navX=" add navX add ", navY=" add navY);
		setProperty("_level10.hand", _visible, true);
		Mouse.hide();
		// Defining the area where Mapdrag is allowed is not that easy:
		// 
		// ZoomCorrectionMap = 1 - (100/_levell0.mapSizes[_levell0.mapSizeN]);
		// maxMapL = -373// - (1 * ZoomCorrectionMap); //LEFT
		// maxMapT = -195// - (1 * ZoomCorrectionMap); //TOP
		// maxMapR = 337// + (1 * ZoomCorrectionMap); //RIGHT
		// maxMapB = 506// + (1 * ZoomCorrectionMap); //BOTTOM
		// 
		ZoomCorrectionMap = _levell0.mapSizes[_levell0.mapSizeN]/2;
		trace("ZoomCorrectionMap" add ZoomCorrectionMap);
		maxMapL = -9373+50-ZoomCorrectionMap;
		// When dragged RIGHT
		maxMapT = -9195+50-ZoomCorrectionMap;
		// When dragged DOWN!!
		maxMapR = 9337-50+ZoomCorrectionMap;
		// When dragged LEFT
		maxMapB = 9456-50+ZoomCorrectionMap;
		// When dragged UP!!
		trace("BuoyLT_mc X= " add _level10.MapZoomable_mc.BuoyLT_mc._x);
		trace("BuoyLT_mc Y= " add _level10.MapZoomable_mc.BuoyLT_mc._y);
		startDrag("_level10.MapZoomable_mc.MapwithButtons_mc", false, maxMapL, maxMapT, maxMapR, maxMapB);
	}
};
_root.onMouseUp = function() {
	if (allowMapDraggin == true) {
		_level10.MapZoomable_mc.MapwithButtons_mc._x = 10*Math.round((_level10.MapZoomable_mc.MapwithButtons_mc._x)/10);
		_level10.MapZoomable_mc.MapwithButtons_mc._y = 10*Math.round((_level10.MapZoomable_mc.MapwithButtons_mc._y)/10);
		// The buoyz now follow the map when mouse is released thus making it possible to trace the postion of the buoyz cq the map
		_level10.MapZoomable_mc.BuoyLT_mc._x -= 10*Math.round((mapX-_level10.MapZoomable_mc.MapwithButtons_mc._x)/10);
		_level10.MapZoomable_mc.BuoyLT_mc._y -= 10*Math.round((mapY-_level10.MapZoomable_mc.MapwithButtons_mc._y)/10);
		_level10.MapZoomable_mc.BuoyRB_mc._x -= 10*Math.round((mapX-_level10.MapZoomable_mc.MapwithButtons_mc._x)/10);
		_level10.MapZoomable_mc.BuoyRB_mc._y -= 10*Math.round((mapY-_level10.MapZoomable_mc.MapwithButtons_mc._y)/10);
		_level10.NavWindow_mc.NavZoomable_mc._x = -_level10.MapZoomable_mc.MapwithButtons_mc._x/10+NavSquareX;
		// Absolute positioning minus navSquare deviation
		_level10.NavWindow_mc.NavZoomable_mc._y = -_level10.MapZoomable_mc.MapwithButtons_mc._y/10+NavSquareY;
		// Absolute positioning minus navSquare deviation
		setProperty("_level10.hand", _visible, false);
		Mouse.show();
		stopDrag();
	}
};
_root.onMouseMove = function() {
	_level10.hand._x = _level10._xmouse;
	_level10.hand._y = _level10._ymouse;
	
	// constantly aware of coverage:
	// CheckWindowCoverage()
};

// ---------------------------------------------------------------
// Wouter Ram 15-09-2008
// Set the map so  that if a playgroundObjectId is given
// the playgroundObject with that id will be in the center and the info screen will be shown
//
// -----------------------------------------------------------------
if(_root.playgroundObjectId != undefined && _root.playgroundObjectId != ""){
	
	//movieClipName_array contains the Id
	for (i = 0; i < _level10.movieClipName_array.length; i++){
		if(_level10.movieClipName_array[i] == _root.playgroundObjectId){
			playgroundObjectIndex = i;
			break;
		}
	}
	
	
	//playgroundObjectIndex = _root.playgroundObjectId-1;
	
	//getting the position of the object
	xpos = _level10.LocationX_array[playgroundObjectIndex];
	ypos = _level10.LocationY_array[playgroundObjectIndex];
	
	// place the map so the objectpoint will be in the center
	_level10.MapZoomable_mc.MapwithButtons_mc._x += 700 - xpos * 1.4;
	_level10.MapZoomable_mc.MapwithButtons_mc._y += 800 - ypos * 1.4;
	
	// NOTE: the following code is a near duplicate of code executed by the soButtonsPath.onRelease function
	//
	// show the infoscreen for the object
	_level10.infoScreen_mc.site_btn._visible = true;
	_level10.infoScreen_mc._visible = true;
	_level10.infoScreen_mc.objectsName = String(_level10.objectsName_array[playgroundObjectIndex]);
	_level10.infoScreen_mc.description = String(_level10.description_array[playgroundObjectIndex]);
	_level10.infoScreen_mc.webUrl = String(root_url add _level10.webUrl_array[playgroundObjectIndex]);
	_level10.infoScreen_mc.jpgUrl = String(root_url add _level10.jpgUrl_array[playgroundObjectIndex]);
	if (_level10.jpgUrl_array[playgroundObjectIndex] == null) {
		_level10.infoScreen_mc.ImgAvailability = "Geen plaatje beschikbaar";
	} else {
		_level10.infoScreen_mc.ImgAvailability = "Plaatje wordt geladen..";
		_level10.infoScreen_mc.jpgHolder_mc.loadMovie(_level10.infoScreen_mc.jpgUrl);
	}
}


// ---------------------------------------------------------------
// Wouter Ram 15-09-2008 
// END
// -----------------------------------------------------------------


stop();
