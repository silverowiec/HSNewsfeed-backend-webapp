/**
 * Created by Dawid on 26/04/16.
 */
var google = google || {};
google.devrel = google.devrel || {};
google.devrel.functions = google.devrel.functions || {};
var isTeacher = false;


google.devrel.functions.print = function(resp) {
	document.getElementById("messages-container").innerHTML +=
        '<div class="panel panel-default"> <div class="panel-heading"> <h3 class="panel-title">' +resp.title+'</h3></div>'+'<div class="panel-body">'+resp.description+'</div><div class="panel-footer">'+resp.dates+'</div></div>';


};

google.devrel.functions.getLucky = function(schoolcode){
    gapi.client.messagingService.getLucky({'schoolKey': schoolcode}).execute(
        function(resp) {
            if (!resp.code) {
				document.getElementById("lucky-container").innerHTML += resp.luckyS;
            }
        });
};

google.devrel.functions.asTeacherList = function(){
    var pass = prompt("Podaj hasło", "");
    var schoolcode = localStorage.getItem("schoolCode");
    gapi.client.messagingService.getValid({'schoolKey': schoolcode, 'password': pass}).execute(
        function (resp) {
            if (!resp.code) {
                if (resp.valid == true) {
                    alert("Hasło poprawne");
                    document.getElementById("messages-container").innerHTML ="";
                    isTeacher = true;
                    google.devrel.functions.listMessages(schoolcode);
                } else {
                    alert("Hasło błędne");
                    isTeacher = false;
                }
            }


        });
};

google.devrel.functions.listMessages = function(schoolcode) {
    gapi.client.messagingService.listMessages({'schoolKey': schoolcode}).execute(
        function(resp) {
            if (!resp.code) 
                resp.items = resp.items || [];
            if(isTeacher==true){
                for (var i = 0; i < resp.items.length; i++) {
                    google.devrel.functions.print(resp.items[i]);
                }}else{
                for (var i = 0; i < resp.items.length; i++) {
                    if(resp.items[i].isForTeacher==false)
                    google.devrel.functions.print(resp.items[i]);
                }
            }
            
        });
};







google.devrel.functions.init = function (apiRoot) {
    gapi.client.load('messagingService', 'v2', callback, apiRoot);

};
var callback = function() {
    var schoolcode = localStorage.getItem("schoolCode");
    google.devrel.functions.getLucky(schoolcode);
    google.devrel.functions.listMessages(schoolcode);

	


   
};