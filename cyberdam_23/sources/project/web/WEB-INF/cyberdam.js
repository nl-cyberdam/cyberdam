// stole a recursive dumper
var odump = (function(){
  var max, INDENT = "                                   "; // As long as you need :)
  
  function valueToStr(value, depth) {
    switch (typeof value) {
      case "object":   return objectToStr(value, depth + 1);
      case "function": return "function";
      default:         return value;
    }
  }
  
  function objectToStr(object, depth) {
    if (depth > max)
      return false;
    
    var output = "";
    for (var key in object)
      output += "\n" + INDENT.substr(0,2*depth) + key + ": " + valueToStr(object[key], depth);

    return output;  
  };
  
  return function odump(object, depth, _max) {
    max = _max || 2;
    return objectToStr(object, depth || 0);
  };
})();

var inputVariables = variables

this.cyberdam = { variables: { } }

for (var entry in Iterator (variables.entrySet ()))
{
	var key = "" + new String (entry.key)
	var value = "" + new String (entry.value)
	var pathParts = key.split (/\./)
	var parentObject = cyberdam.variables
	for (var parentIndex in pathParts)
	{
		var name = pathParts[parentIndex]
		if (parentIndex == pathParts.length - 1)
		{
			parentObject[name] = value
		}
		else
		{
			if (parentObject[name] === null || parentObject[name] === undefined)
			{
				parentObject[name] = { }
			}
			parentObject = parentObject[name]
		}
	}
}


// return variable values to Java in the variables array
variables = cyberdam.variables

cyberdam.activities = new Array ()
for (var mappedActivity in Iterator (session.manifest.gameModel.activities))
{
	cyberdam.activities[new String (mappedActivity.name)] = mappedActivity
}

cyberdam.stepsofplay = new Array ()
for (var step in Iterator (session.manifest.gameModel.stepsOfPlay))
{
	cyberdam.stepsofplay[step.name] = step
}

cyberdam.makeActivityAvailable = function ()
{
	participant.enableActivity (activity);
}

cyberdam.setNextStepOfPlay = function (stepOfPlayName)
{
	var step = cyberdam.stepsofplay[stepOfPlayName]
	session.gotoStep (step)
}

cyberdam.getMessage = function (messageID)
{
	return messageSource.getMessage (messageID, null, locale)
}

cyberdam.listVariables = function ()
{
	var list = ""

	for (var entry in Iterator (inputVariables.entrySet ()))
	{
		var key = "" + new String (entry.key)
		var value = "" + new String (entry.value)
		if (key != "debug")
		{
			list = list + key + "=" + value + "<br>"
		}
	}
	return list
}

// Usage in activity scripts:
// variables["spits"]= "Johan Cruijff" - overrides a model variable value in the current session
// cyberdam.setActivityStatus ("Voortgang", false) - disables activity 'Voortgang' in the current session
// cyberdam.setNextStepOfPlay ("Einde") - set next step of play to 'Einde' for the current session

// return something tru'ish in case the appended user script is empty

true
