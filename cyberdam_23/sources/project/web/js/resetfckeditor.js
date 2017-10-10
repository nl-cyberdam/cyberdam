// Just in case someone presses the reset button
window.resetEditors = function () 
{ 
    // Loop through all FCK instances, in case there are several editors 
    for (var sEditorName in FCKeditorAPI.__Instances) 
    { 
        // The initial value that was set when the form was created 
        // is stored in a hidden <INPUT> with the same name as the 
        // editor (the editor itself is in an <IFRAME> with ___Frame 
        // appended to the name.  Check whether that INPUT exists 
        if (document.getElementById(sEditorName)) 
        { 
            // Get the initial value 
            var sInitialValue = document.getElementById(sEditorName).value; 

            // Overwrite the editor's current value 
            FCKeditorAPI.__Instances[sEditorName].SetHTML(sInitialValue); 
        } 
    } 
}
