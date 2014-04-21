<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>


<%! // Share the client objects across threads to
    // avoid creating new clients for each web request

 %>

<%
    /*
     * AWS Elastic Beanstalk checks your application's health by periodically
     * sending an HTTP HEAD request to a resource in your application. By
     * default, this is the root or default resource in your application,
     * but can be configured for each environment.
     *
     * Here, we report success as long as the app server is up, but skip
     * generating the whole page since this is a HEAD request only. You
     * can employ more sophisticated health checks in your application.
     */
    if (request.getMethod().equals("HEAD")) return;
%>



<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <title>Hello AWS Web World!</title>
    <link rel="stylesheet" href="styles/styles.css" type="text/css" media="screen">
    <script src="styles/jquery.js" type="text/javascript"></script>
</head>
<body>
   <div id="contenthere" >
   </div>
<script>
    $(document).ready(function () {
        // Get a reference to the content div (into which we will load content).
        var jContent = $("#contenthere");

        // Launch AJAX request.
        $.ajax(
        {
            // The type of request.
            type: "GET",
            // The link we are accessing.
            url: "http://localhost:8080/SearchRest/rest/search/searchservice/pohvendorid:1005071",
            contentType: "application/json; charset=utf-8",
            // The type of data that is getting returned.
            dataType: "json",
            success: function (strData) {
            alert(strData);
            jContent.html(strData);
            }
        });

    });
</script>   
    </div>
</body>
</html>