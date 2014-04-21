 function checkAuthentication() {
    var ticket = sessionStorage.getItem("AuthenticationPass");    
    if (ticket == 'Yes') {}
    else {window.location = "login.html";}}
  
 function validate() {
        $.ajax
             ({
                 type: "GET",
                 url: "js/POM.txt",
                 dataType: "json",
                 data: "",
                 contentType: "application/json; charset=utf-8",
                 error: function (jqXHR, textStatus, errorThrown) {
                     alert("Error:" + errorThrown);
                 },

                 success: function (dataR) {                     
                     var data = dataR;                 
                     var usn = data.username;
                     var psw = data.password;
                     var un = document.myform.username.value;
                     var pw = document.myform.password.value;

        if (usn == un && psw == pw) {
            sessionStorage.setItem("AuthenticationPass", "Yes");
            sessionStorage.setItem("user", document.myform.username.value);
            window.location = "home.html";
            return false;
        }
        else {
            alert("Invalid username or password.");
            sessionStorage.setItem("AuthenticationPass", "No");
        }
                 }
             });

    } 

    



