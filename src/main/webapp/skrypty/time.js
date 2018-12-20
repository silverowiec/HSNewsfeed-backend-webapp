/**
 * Created by dawid on 03/08/16.
 */
/**
 * Created by Dawid on 26/04/16.
 */
var google = google || {};
google.devrel = google.devrel || {};
google.devrel.functions = google.devrel.functions || {};




google.devrel.functions.listClasses = function (schoolcode) {
    gapi.client.messagingService.listClasses({'SchoolCode': schoolcode}).execute(
        function (resp) {
            if(!resp.code)
                resp.items = resp.items || [];


            for(var o=0; o<resp.items.length; o++)
            {
                var name = resp.items[o].code;
                var newOption = document.createElement("option");
                newOption.value = name;
                newOption.innerHTML = name;
                document.getElementById('classselect').options.add(newOption);
            }


        });

};

var days = ["Poniedzialek", "Wtorek", "Sroda", "Czwartek", "Piatek"];


google.devrel.functions.getTimeTable = function (){
    var classcode = document.getElementById("classselect").value;
    var timetable = new Timetable();
    timetable.setScope(7, 16);
    timetable.addLocations(days);

      gapi.client.messagingService.listLessons({
          'schoolCode': schoolcode,
          'classCode': classcode
      }).execute(
          function (resp) {
              if (!resp.code)
                  resp.items = resp.items || [];

              for (var i = 0; i < resp.items.length; i++) {
                  var day = resp.items[i].weekDay;
                  var name = resp.items[i].name+" "+resp.items[i].classRoom;
                  var hour = resp.items[i].hour;
                  var minute = resp.items[i].minute;
                  var hour2 = hour;
                  var minute2 = minute + 45;
                  if (minute2 >= 60) {
                      hour2++;
                      minute2 -= 60;
                  }
                  timetable.addEvent(name, day, new Date(2015, 7, 17, hour, minute), new Date(2015, 7, 17, hour2, minute2));
                if(i==resp.items.length-1){
                    var renderer = new Timetable.Renderer(timetable);
                    renderer.draw('.timetable');
                }
              }

          }
      );

};





var schoolcode="";


google.devrel.functions.init = function (apiRoot) {
    gapi.client.load('messagingService', 'v2', callback, apiRoot);

};
var callback = function() {
        schoolcode = localStorage.getItem("schoolCode");
        google.devrel.functions.listClasses(schoolcode);

};