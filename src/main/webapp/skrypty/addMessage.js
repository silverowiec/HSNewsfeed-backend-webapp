/**
 * Created by Dawid on 26/04/16.
 */
var google = google || {};
google.devrel = google.devrel || {};
google.devrel.functions = google.devrel.functions || {};
var authed = false;



google.devrel.functions.print = function(resp) {
    document.getElementById("messages-container").innerHTML +=
        '<div class="panel panel-default"> <div class="panel-heading"> <h3 class="panel-title">' +resp.title+'</h3></div>'+'<div class="panel-body">'+resp.description+'</div><div class="panel-footer">'+resp.dates+'</div></div>';


};


google.devrel.functions.addMessage = function () {
    if(authed == true) {
        var title = document.getElementById('title').value;
        var message = document.getElementById('message').value;
        var x = document.getElementById('checkbox').checked;
        gapi.client.messagingService.createMessage({
            'schoolKey': schoolcode,
            'title': title, 'description': message, 'isForTeacher': x
        }).execute(function (resp) {
            if (!resp.code)
                alert("Wiadomość " + resp.title + " została dodana!");
            google.devrel.functions.print(resp);
        });
    }

};








var schoolcode="";


val = function () {
    var p = prompt("Podaj haslo: ", "");
    gapi.client.messagingService.getValid({'schoolKey': schoolcode, 'password': p}).execute(
        function (resp) {
            if (!resp.code) {
                if (resp.valid == true) {
                    authed = true;
                    alert("Haslo poprawne");
                } else {
                    authed = false;
                    alert("Haslo bledne, odswiez strone i sprobuj ponownie.");
                }
            }


        });
};




google.devrel.functions.init = function (apiRoot) {
    gapi.client.load('messagingService', 'v2', callback, apiRoot);

};
var callback = function() {
    schoolcode = prompt("Podaj kod szkoly: ", "");
    val();


	


   
};