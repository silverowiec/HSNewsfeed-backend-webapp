/**
 * Created by dawid on 03/08/16.
 */
/**
 * Created by Dawid on 26/04/16.
 */
var google = google || {};
google.devrel = google.devrel || {};
google.devrel.functions = google.devrel.functions || {};
var schoolcode = "";
var hours = [];
var mins = [];
var authed = false;


google.devrel.functions.deleteLesson = function () {
    if(authed==true) {
        var classcode = document.getElementById('classselect').value;
        var number = document.getElementById('deletelessonselect').value;
        gapi.client.messagingService.deleteLesson({
            'schoolCode': schoolcode,
            'classCode': classcode, 'count': number
        }).execute(function (resp) {
            if (!resp.code)
                google.devrel.functions.getTimeTable(classcode);
            var select = document.getElementById('deletelessonselect');
            var length = select.options.length;
            for (i = 0; i < length; i++) {
                select.options[i] = null;
            }
            changeday();
        });
    }};



changeday = function(){
    var day = document.getElementById('deletedayselect').value;
    var classcode = document.getElementById('classselect').value;
    gapi.client.messagingService.listLessonsByDay({'schoolCode':schoolcode,
    'classCode': classcode, 'Day': day}).execute(function (resp) {
        if(!resp.code)
            resp.items = resp.items || [];
        for (var o = 0; o < resp.items.length; o++) {
            var number = resp.items[o].count;
            var name = number+" "+resp.items[o].name;
            var newOption = document.createElement("option");
            newOption.value = number;
            newOption.innerHTML = name;
            document.getElementById('deletelessonselect').options.add(newOption);
        }
    });
}

google.devrel.functions.listClassesDelete = function () {
    if (authed == true) {
        gapi.client.messagingService.listClasses({'SchoolCode': schoolcode}).execute(
            function (resp) {
                if (!resp.code)
                    resp.items = resp.items || [];


                for (var o = 0; o < resp.items.length; o++) {
                    var name = resp.items[o].code;
                    var newOption = document.createElement("option");
                    newOption.value = name;
                    newOption.innerHTML = name;
                    document.getElementById('classselect').options.add(newOption);
                }

                google.devrel.functions.getTimeTable(resp.items[0].code);
            });

    }
};





google.devrel.functions.deleteTimeTable = function(){
    if(authed==true) {
        if (confirm("Na pewno?") == true) {
            gapi.client.messagingService.deleteTimeTable({'schoolCode': schoolcode}).execute(function () {
                if (!resp.code)
                    alert("Plan lekcji został usunięty.");
                else alert("Wystąpił błąd. Spróbuj ponownie lub skontaktuj się z administratorem aplikacji.");
            });

        }
    }
};



google.devrel.functions.addClass = function () {
    if (authed == true) {
        var classcode = document.getElementById('classCode').value;
        if (classcode != "") {
            gapi.client.messagingService.addClass({'SchoolCode': schoolcode, 'classCode': classcode}).execute(
                function (resp) {
                    alert("dodano klase: " + resp.code);
                    document.getElementById('classes').innerHTML += resp.code + " ";
                    var newOption = document.createElement("option");
                    newOption.value = resp.code;
                    newOption.innerHTML = resp.code;
                    document.getElementById('classelect').options.add(newOption);

                }
            );
        } else alert("Podaj kod klasy!");
    }
};


google.devrel.functions.addLesson = function () {
    if (authed == true) {
        var klasa = document.getElementById('classelect').value;
        var day = document.getElementById('dayselect').value;
        var number = document.getElementById('hourselect').value;
        var classroom = document.getElementById('classRoomField').value;
        var name = document.getElementById('lessonname').value;
        var hour = hours[number];
        var minute = mins[number];
        if (name != "" && classroom != "") {
            gapi.client.messagingService.addLesson({
                'schoolCode': schoolcode,
                'classCode': klasa, 'name': name, 'classRoom': classroom,
                'hour': hour, 'minute': minute, 'count': number, 'weekDay': day
            }).execute(
                function (resp) {
                    if (!resp.code)
                        alert("Dodano lekcje: " + resp.name + " o godzinie " + resp.hour + ":" + resp.minute);
                    google.devrel.functions.getTimeTable(klasa);

                });


        } else alert("Wypełnij wszystkie pola!");
    }
};


google.devrel.functions.getHours = function () {
    gapi.client.messagingService.listHours({'schoolCode': schoolcode}).execute(
        function (resp) {
            if (!resp.items)
                resp.items = resp.items || [];

            for (var i = 0; i < resp.items.length; i++) {
                var number = resp.items[i].number;
                mins[i] = resp.items[i].startMinute;
                hours[i] = resp.items[i].startHour;
                var name = resp.items[i].startHour + ":" + resp.items[i].startMinute + " - " + resp.items[i].endHour + ":" + resp.items[i].endMinute;
                var newOption = document.createElement("option");
                newOption.value = number;
                newOption.innerHTML = number + ": " + name;
                document.getElementById('hourselect').options.add(newOption);
            }
        }
    );

};


google.devrel.functions.listClasses = function () {
    if (authed == true) {
        gapi.client.messagingService.listClasses({'SchoolCode': schoolcode}).execute(
            function (resp) {
                if (!resp.code)
                    resp.items = resp.items || [];


                for (var o = 0; o < resp.items.length; o++) {
                    var name = resp.items[o].code;
                    var newOption = document.createElement("option");
                    newOption.value = name;
                    newOption.innerHTML = name;
                    document.getElementById('classelect').options.add(newOption);
                    document.getElementById('classes').innerHTML += name + " ";
                }

                google.devrel.functions.getTimeTable(resp.items[0].code);
            });

    }
};

var days = ["Poniedzialek", "Wtorek", "Sroda", "Czwartek", "Piatek"];


google.devrel.functions.getTimeTable = function (classcode) {
    if (authed == true) {
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
                    var name = resp.items[i].name + " " + resp.items[i].classRoom;
                    var hour = resp.items[i].hour;
                    var minute = resp.items[i].minute;
                    var hour2 = hour;
                    var minute2 = minute + 45;
                    if (minute2 >= 60) {
                        hour2++;
                        minute2 -= 60;
                    }

                    timetable.addEvent(name, day, new Date(2015, 7, 17, hour, minute), new Date(2015, 7, 17, hour2, minute2));
                    if (i == resp.items.length - 1) {
                        var renderer = new Timetable.Renderer(timetable);
                        renderer.draw('.timetable');
                    }
                }

            });
    }
};

onchange = function () {
    var classcode = document.getElementById('classelect').value;
    google.devrel.functions.getTimeTable(classcode);
};

val = function () {
    var p = prompt("Podaj haslo: ", "");
    gapi.client.messagingService.getValid({'schoolKey': schoolcode, 'password': p}).execute(
        function (resp) {
            if (!resp.code) {
                if (resp.valid == true) {
                    authed = true;
                    alert("Haslo poprawne");
                    google.devrel.functions.listClasses();
                    google.devrel.functions.listClassesDelete();
                    google.devrel.functions.getHours();
                } else {
                    authed = false;
                    alert("Haslo bledne, odswiez strone i sprobuj ponownie.");
                }
            }


        });
};

google.devrel.functions.enablebuttons = function () {
    document.getElementById('addLesson').onClick = function () {
        if (document.getElementById('lessonname').value != null && document.getElementById('classRoomField').value != null)
            google.devrel.functions.addLesson();
        else alert("Proszę wypełnić wszystkie pola!");
    }

};

google.devrel.functions.init = function (apiRoot) {
    gapi.client.load('messagingService', 'v2', callback, apiRoot);
};
var callback = function () {
    schoolcode = prompt("Podaj kod szkoly: ", "");
    val();


};
