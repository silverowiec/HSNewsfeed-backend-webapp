/**
 * Created by dawid on 24/08/16.
 */

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
var code = "0";

google.devrel.functions.addSchool = function () {
    alert("Proszę czekać na odpowiedź serwera");
    var name = document.getElementById('schoolname').value;
    code = document.getElementById('schoolcode').value;
    var count = document.getElementById('pupilscount').value;
    var pass = document.getElementById('password').value;
    var email = document.getElementById('email').value;


    gapi.client.messagingService.addSchool({'schoolCode': code, 'name': name, 'pupilsCount': count,
    'pass': pass, 'email': email}).execute(function (resp) {
            alert("Twoja szkoła z kodem "+resp.code+" została dodana! Przedź do kroku 2");
            authed = true;
    });

};

var number = 0;

google.devrel.functions.addHour = function(){
    if(authed==false){alert("Wygląda na to, że nie zarejestrowano szkoły. Powróć do kroku 1");}
    else{
        var hour = document.getElementById('hour').value;
        var minute = document.getElementById('minute').value;
        gapi.client.messagingService.addHour({'schoolCode': code, 'number': number,
        'startHour': hour, 'startMinute': minute}).execute(function (resp) {
            if(!resp.code){
                document.getElementById('lessoncontainer').innerHTML += '<p>'+resp.number+': '+resp.startHour+':'+resp.startMinute+
                        ' - '+resp.endHour+':'+resp.endMinute+'</p>';
                number++;
                document.getElementById('lessoncount').innerHTML = number;
            }
        });
    }
   
};




google.devrel.functions.init = function (apiRoot) {
    gapi.client.load('messagingService', 'v2', callback, apiRoot);

};
var callback = function() {
    //there was 'api loaded' alert
};