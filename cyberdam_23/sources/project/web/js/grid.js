var grid = {
	threshold: 90, // table width in % of parent div
	leftNav: false,
	rightNav: false,
	firstIndex: 0,
	nrVisibleIndex: 0,
	editable: false,
	init: function() {
		grid.div = $('grid');
		grid.tbody = grid.div.getElementsByTagName('tbody')[0];
		grid.form = $('mainform');
		grid.gridDataInput = $('gridData');
		grid.action = $('action');
		grid.nrSteps = grid.gridData.steps.length;
		if (!grid.editable && grid.nrRoles == 0 && grid.nrSteps == 0) {
			grid.div.innerHTML = grid.noGridString;
			return;
		}
		if (grid.editable) {
			if (grid.gridData.addStep) { // shift to right
				var i = 0;
				while (i < grid.nrSteps) {
					grid.showDataColumn(i++);
				}				
			} else {
				var i = grid.nrSteps - 1;
				var x = grid.gridData.firstIndex || 0;
				// correct x when it was at last entry when this step was deleted
				if (x > 0 && x == grid.nrSteps) x--;
				while (i >= x) {
					grid.showDataColumn(i--);
				}
				if (x > 0) grid.addLeftNav();
			}
			// add role draggables
			for (var i = 0;i < grid.nrRoles;i++) {
				new Draggable("dragRole" + i, {
								  scroll:window,
								  revert:true,
								  onStart: grid.addRoleDroppables,
								  onEnd: grid.removeRoleDroppables
					});
			}
			// select first step in case of only 1 step
			if (grid.form.initialStepOfPlay.length == 2) {
				grid.form.initialStepOfPlay.selectedIndex = 1;
			}
		} else {
			var i = grid.nrSteps - 1;
			while (i >= 0) {
				grid.showDataColumn(i--);
			}
		}
	},
	getColumnData: function(k) {
		var columnData, header, step = grid.gridData.steps[k];
		if (grid.editable) {
			var input = document.createElement('input');
			input.value = step.name;
			input.type = 'text';
			var image = document.createElement('img');
			image.onclick = new Function('grid.deleteStep(' + k + ')');
			image.title = grid.deleteString;
			image.src = 'themes/default/button_delete.gif';
			image.className = 'input';
			var div = document.createElement('div');
			div.id = 'dragStep' + k;
			div.className = 'draggable step';
			div.appendChild(input);
			div.appendChild(image);
			header = div;
		} else {
			header = document.createTextNode(step.name);
		}
		columnData = [header];
		for (var i = 0;i < step.activitiesarray.length;i++) {
			var activities = step.activitiesarray[i];
			var content = [];
			for (var j = 0;j < activities.length;j++) {
				var activity = activities[j];
				content.push(document.createTextNode(activity.name));
				if (grid.editable) {
					var image = document.createElement('img');
					image.onclick = 
						new Function('grid.deleteActivity(' + k + ',' + i + ',' + activity.id + ')');
					image.title = grid.deleteString;
					image.src = 'themes/default/button_delete.gif';
					image.className = 'input';
					content.push(image);
				}
				if (j < activities.length - 1) {
					content.push(document.createElement('br'));
				}
			}
			if (grid.editable) {
				var droppable = document.createElement('div');
				droppable.id = 'drop' + k + 'x' + i;
				droppable.stepindex = k;
				droppable.roleindex = i;
				if (content.length == 0) droppable.appendChild(document.createTextNode("\u00a0"));
				while (content.length > 0) droppable.appendChild(content.shift());
				content = droppable;
			}
			columnData.push(content);
		}
		if (!grid.editable) {
			var a = document.createElement('a');
			a.className = 'button';
			a.href = grid.editStepUrl + '&objectId=' + k;
			a.innerHTML = grid.editStepString;
			columnData.push(a);
		}
		return columnData;
	},
	showDataColumn: function(i) {
		if (grid.editable) grid.setDivHeights(true);
		var columnData = grid.getColumnData(i);
		// remove any navigation columns if necessary
		if (i == 0 && grid.leftNav) {
			grid.removeTableColumn(1);
			grid.leftNav = false;
		} else if (i == (grid.nrSteps -1 ) && grid.rightNav) {
			var x = grid.nrVisibleIndex + 1;
			if (grid.leftNav) x++;
			grid.removeTableColumn(x);
			grid.rightNav = false;
		}
		// do insertion/deletion
		var x;
		if (i < grid.firstIndex || grid.nrVisibleIndex == 0) { // left side
			x = 1;
			if (grid.leftNav) x++;
			grid.insertTableColumn(x, columnData);
			grid.firstIndex = i;
			grid.nrVisibleIndex++;
			while (!grid.columnsFit() && grid.nrVisibleIndex > 1) {
				x = grid.firstIndex + grid.nrVisibleIndex  - 1;
				grid.hideDataColumn(x);
			}
			x = grid.firstIndex + grid.nrVisibleIndex  - 1;
			if (x < (grid.nrSteps - 1) && !grid.rightNav) {
				grid.addRightNav();
			}
		} else { // right side
			x = grid.nrVisibleIndex + 1;
			if (grid.leftNav) x++;
			grid.insertTableColumn(x, columnData);
			grid.nrVisibleIndex++;
			while (!grid.columnsFit() && grid.nrVisibleIndex > 1) {
				grid.hideDataColumn(grid.firstIndex);
			}
			if (grid.firstIndex > 0 && !grid.leftNav) {
				grid.addLeftNav();
			}
		}
		if (grid.editable) {
			grid.setDivHeights(false);
			// add step draggable
			new Draggable(columnData[0].id, {
						scroll:window,
						revert: true,
						onStart: grid.addStepDroppables,
						onEnd: grid.removeStepDroppables
						});
			// add activity droppables
			for (var y = 1;y < grid.nrRoles + 1;y++) {
				Droppables.add(columnData[y].id, {
						hoverclass: "showBorders",
							accept: "activity",
							onDrop: function(drag, drop) {
							grid.addActivity(drop.stepindex, drop.roleindex, parseInt(drag.id.match(/\d+/)));
							// prevent revert on successful drop
							Draggables.activeDraggable.options.revert = false;
						}
					});
			}
		}
	},
	hideDataColumn: function(i) {
		var x = i - grid.firstIndex + 1;
		if (grid.leftNav) x++;
		if (grid.editable) {
			for (var y = 0;y < grid.nrRoles + 1;y++) {
				var item = grid.getTableItem(x,y);
				var id = item.firstChild.id;
				var div = $(id);
				if (y == 0) {
					var drag;
					Draggables.drags.each(function(d) { if (d.element==div) drag = d; });
					drag.destroy();
				} else {
					Droppables.remove(id);
				}
			}
		}
		grid.removeTableColumn(x);
		grid.nrVisibleIndex--;
		if (i == grid.firstIndex) grid.firstIndex++;
	},
	insertTableColumn: function(x, columnData) {
		i = grid.nrRoles + 1;
		if (!grid.editable) i++;
		for (var y = 0;y < i;y++) {
			var tr = grid.tbody.rows[y];
			var nextitem = tr.cells[x];
			var item = document.createElement(y == 0 ? 'th' : 'td');
			var data = columnData[y] || [];
			if (data instanceof Array) {
				while (data.length > 0) item.appendChild(data.shift());
			} else {
				item.appendChild(data);
			}
			if (nextitem) {
				tr.insertBefore(item, nextitem);
			} else {
				tr.appendChild(item);
			}
		}
	},
	removeTableColumn: function(x) {
		i = grid.nrRoles + 1;
		if (!grid.editable) i++;
		for (var y = 0;y < i;y++) {
			var tr = grid.tbody.rows[y];
			var item = tr.cells[x];
			tr.removeChild(item);
		}
	},
	setDivHeights: function (reset) {
		var i,y,x = 1;
		if (grid.leftNav) x++;
		for (y = 0;y < grid.nrRoles;y++) {
			var offsetHeight = (grid.getTableItem(0, y + 1).offsetHeight - 3) + 'px';
			for (i = 0;i < grid.nrVisibleIndex;i++) {
				var div = grid.getTableItem(i + x, y + 1).getElementsByTagName('div')[0];
				if (reset) {
					div.style.height = null;
				} else {
					div.style.height = offsetHeight;
				}
			}
		} 
	},
	shiftLeft: function () {
		if (grid.editable) grid.makePersistent();
		grid.showDataColumn(grid.firstIndex - 1);
	},
	shiftRight: function () {
		if (grid.editable) grid.makePersistent();
		grid.showDataColumn(grid.firstIndex + grid.nrVisibleIndex);
	},
	addLeftNav: function() {
		var image = document.createElement('img');
		image.className = 'nav';
		image.onclick = function() { grid.shiftLeft() };
		image.title = grid.shiftLeftString;
		image.src = 'themes/default/button_left.gif';
		grid.insertTableColumn(1, [image]);
		grid.leftNav = true;
	},
	addRightNav: function() {
		var image = document.createElement('img');
		image.className = 'nav';
		image.onclick = function() { grid.shiftRight() };
		image.title = grid.shiftRightString;
		image.src = 'themes/default/button_right.gif';
		var x = grid.nrVisibleIndex + 1;
		if (grid.leftNav) x++;
		grid.insertTableColumn(x, [image]);
		grid.rightNav = true;
	},
	addRoleDroppables: function(draggable) {
		var index = parseInt(draggable.element.id.match(/\d+/));
		for (var i = 0;i < grid.nrRoles + 1;i++) {
			if (i != index && i != index + 1) {
				var ti = grid.getTableItem(0, i);
				var position = Position.cumulativeOffset(ti);
				var image = document.createElement('img');
				image.className = 'pointer';
				image.src = 'themes/default/pointer_left.gif';
				var div = document.createElement('div');
				div.appendChild(image);
				var height = 15;
				var offset = 8;
				div.id = 'dropRole' + i;
				div.style.height = height + 'px';
				div.style.width = (ti.offsetWidth + 2 * offset) + 'px';
				div.style.left = (position[0] - offset) + 'px';
				div.style.top = (position[1] + ti.offsetHeight - parseInt((height + 1)/2)) + 'px';
				div.style.position = 'absolute';
				document.body.appendChild(div);
				Droppables.add(div.id, {
						hoverclass: "showPointer",
						accept: "role",
						onDrop: function(drag, drop) {
							grid.changeRoleOrder(parseInt(drag.id.match(/\d+/)), parseInt(drop.id.match(/\d+/)));
							// prevent revert on successful drop
							Draggables.activeDraggable.options.revert = false;
						}
					});
			}
		}
	},
	removeRoleDroppables: function(draggable) {
		var index = parseInt(draggable.element.id.match(/\d+/));
		for (var i = 0;i < grid.nrRoles + 1;i++) {
			if (i != index && i != index + 1) {
				var id = 'dropRole' + i;
				Droppables.remove(id);
				Element.remove(id);
			}
		}
	},
	addStepDroppables: function(draggable) {
		var index = parseInt(draggable.element.id.match(/\d+/));
		var i,x = 1;
		if (grid.leftNav) x++;
		for (i = 0;i < grid.nrVisibleIndex + 1;i++) {
			var stepIndex = i + grid.firstIndex;
			if (stepIndex != index && stepIndex != index + 1) {
				var ti = grid.getTableItem(i + x, 0);
				var position = Position.cumulativeOffset(ti);
				var image = document.createElement('img');
				image.className = 'pointer';
				image.src = 'themes/default/pointer_down.gif';
				var div = document.createElement('div');
				div.appendChild(image);
				var offset = 8;
				var width = 15;
				div.id = 'dropStep' + stepIndex;
				div.style.height = (ti.offsetHeight + 2 * offset) + 'px';
				div.style.width = width + 'px';
				div.style.left = (position[0] - parseInt((width + 1)/2)) + 'px';
				div.style.top = (position[1] - offset) + 'px';
				div.style.position = 'absolute';
				document.body.appendChild(div);
				Droppables.add(div.id, {
						hoverclass: "showPointer",
						accept: "step",
						onDrop: function(drag, drop) {
							grid.changeStepOrder(parseInt(drag.id.match(/\d+/)), parseInt(drop.id.match(/\d+/)));
							// prevent revert on successful drop
							Draggables.activeDraggable.options.revert = false;
						}
					});
			}
		}
	},
	removeStepDroppables: function(draggable) {
		var index = parseInt(draggable.element.id.match(/\d+/));
		for (var i = 0;i < grid.nrVisibleIndex + 1;i++) {
			var stepIndex = i + grid.firstIndex;
			if (stepIndex != index && stepIndex != index + 1) {
				var id = 'dropStep' + stepIndex;
				Droppables.remove(id);
				Element.remove(id);
			}
		}
	},
	addStep: function () {
		grid.action.value = "addStep";
		grid.submit();
	},
	addRole: function () {
		grid.action.value = "addRole";
		grid.submit();
	},
	addActivity: function (stepIndex, roleIndex, activityId) {
		grid.action.value = "addActivity";
		grid.addToForm("stepIndex", stepIndex);
		grid.addToForm("roleIndex", roleIndex);
		grid.addToForm("activityId", activityId);
		grid.submit();
	},
	deleteStep: function (i) {
		grid.action.value = "deleteStep";
		grid.addToForm("deleteIndex", i);
		grid.submit();
	},
	deleteRole: function (i) {
		grid.action.value = "deleteRole";
		grid.addToForm("deleteIndex", i);
		grid.submit();
	},
	deleteActivity: function (stepIndex, roleIndex, activityId) {
		grid.action.value = "deleteActivity";
		grid.addToForm("stepIndex", stepIndex);
		grid.addToForm("roleIndex", roleIndex);
		grid.addToForm("activityId", activityId);
		grid.submit();
	},
	changeRoleOrder: function(oldIndex, newIndex) {
		grid.action.value = "changeRoleOrder";
		grid.addToForm("oldIndex", oldIndex);
		grid.addToForm("newIndex", newIndex);
		grid.submit();
	},
	changeStepOrder: function(oldIndex, newIndex) {
		grid.action.value = "changeStepOrder";
		grid.addToForm("oldIndex", oldIndex);
		grid.addToForm("newIndex", newIndex);
		grid.submit();
	},
	updateSelect: function(select) {
		grid.makePersistent();
		for (var i = 0;i < grid.nrSteps;i++) {
			select.options[i + 1].text = 
				grid.gridData.steps[i].name;
		}
	},
	addToForm: function(key, value) {
		var input = document.createElement('input');
		input.name = key;
		input.type = 'hidden';
		input.value = value;
		grid.form.appendChild(input);
	},
	columnsFit: function() {
		return grid.tbody.offsetWidth < (grid.div.offsetWidth * grid.threshold / 100);
	},
	getTableItem: function(x, y) {
		return grid.tbody.rows[y].cells[x];
	},
	makePersistent: function () {
		// store step names
		var i,x = 1;
		if (grid.leftNav) x++;
		for (i = 0;i < grid.nrVisibleIndex;i++) {
			grid.gridData.steps[i + grid.firstIndex].name = 
				grid.getTableItem(i + x, 0).getElementsByTagName('input')[0].value;
		} 
	},
	submit: function() {
		grid.makePersistent();
		grid.addToForm("firstIndex", grid.firstIndex);
		grid.gridDataInput.value = JSON.stringify(grid.gridData);
		grid.form.submit();
	}
};
Event.observe(window, 'load',
			  function() { 
				  grid.init();
			  });
