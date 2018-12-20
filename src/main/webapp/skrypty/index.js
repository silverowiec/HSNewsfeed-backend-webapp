/**
 * Created by dawid on 03/08/16.
 */
/**
 * Created by Dawid on 26/04/16.
 */
var google = google || {};
google.devrel = google.devrel || {};
google.devrel.functions = google.devrel.functions || {};


google.devrel.functions.changeSchool = function(){
    var code = document.getElementById('schoolselect').value;
    var cookieName = "schoolCode";
    var cookieValue = code;
    localStorage.setItem("schoolCode", cookieValue);
    

};




google.devrel.functions.listSchools = function () {
    gapi.client.messagingService.listSchools().execute(
        function (resp) {
            if(!resp.code)
                resp.items = resp.items || [];


            for(var o=0; o<resp.items.length; o++)
            {
                var name = resp.items[o].code;
                var desc = resp.items[o].name;
                var newOption = document.createElement("option");
                newOption.value = name;
                newOption.innerHTML = desc;
                document.getElementById('schoolselect').options.add(newOption);
            }


        });

};


google.devrel.functions.init = function (apiRoot) {
    gapi.client.load('messagingService', 'v2', callback, apiRoot);

};
var callback = function() {
    if (localStorage.getItem("schoolCode")!=null) {
        window.location.href = '';
    }



    google.devrel.functions.listSchools();



};