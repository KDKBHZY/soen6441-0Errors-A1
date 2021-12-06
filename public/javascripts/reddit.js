
var resArr =[];

var searchterm;
var ws;
console.log("Waiting for WebSocket");
ws = new WebSocket("ws://" + location.host + "/ws");

(function() {
    var parseReddits;
    $(function() {
        if($("#search").length === 1) {

            ws.onmessage = function (event) {

                message = JSON.parse(event.data);

               if (resArr.indexOf(message.term)>=0){
                   return parseReddits(message);
               }


            };
            return $("#searchForm").submit(function (event) {
                event.preventDefault();

                if ($("#searchKey").val() !== '') {
                    searchterm = $("#searchKey").val();
                    if (resArr.indexOf(searchterm)>=0){
                        alert("You have searched "+ searchterm +" before");
                        // var repeat = document.getElementById(a.toString());
                        // repeat.style.color= "lightgreen";
                        // $("html,body").animate({scrollTop: $("#"+a.toString()).offset().top}, 1000);
                    }else {
                        console.log(resArr);
                        resArr.push(searchterm);
                        searchterm = $("#searchKey").val();
                        console.log("Sending WS with value " + $("#searchKey").val());
                        ws.send(JSON.stringify({
                            query: $("#searchKey").val()
                        }));
                        return $("#searchKey").val("");
                    }
                }else {
                    alert("Please enter a keyword");
                }

            });
        }
    });

    parseReddits = function(message) {
        //handle blank
        var query = searchterm.replace(/ /g,'');
        console.log(message);

        redditListQuery = $("#redditsList"+message.term);
        if (redditListQuery.length === 0) {
            $("#result").prepend('<div class="results"><h2><a href="http://localhost:9000/wordstats?term=' + query +'" target="_blank">Search terms: '+query+'</a></h2><ol id="redditsList'+query+'"></ol>');
            redditListQuery1 = $("#redditsList"+query);
            redditListQuery1.append('<li style="margin-bottom:10px "> Author: <a href="http://localhost:9000/user/profile?author=' + message.author + '" target="_blank">' + message.author + '</a>, <a href="http://localhost:9000/searchsub?term=' + message.subReddit + '" target="_blank">' + message.subReddit + '</a>,' + message.title + '</li>');
        }
         console.log(message.term);
        var oUl = document.querySelector("#redditsList"+message.term);
        var oList = oUl.querySelectorAll("li");
        var size = oList.length;

        if (size>9) {
            console.log(size);
            oList[size-1].remove();
            redditListQuery.prepend('<li style="margin-bottom:10px "> Author: <a href="http://localhost:9000/user/profile?author=' + message.author + '" target="_blank">' + message.author + '</a>, <a href="http://localhost:9000/searchsub?term=' + message.subReddit + '" target="_blank">' + message.subReddit + '</a>,' + message.title + '</li>');
        } else {
            redditListQuery.append('<li style="margin-bottom:10px "> Author: <a href="http://localhost:9000/user/profile?author=' + message.author + '" target="_blank">' + message.author + '</a>, <a href="http://localhost:9000/searchsub?term=' + message.subReddit + '" target="_blank">' + message.subReddit + '</a>,' + message.title + '</li>');
        }

    };
    $('#searchKey').bind('keypress', function (event) {
        if (event.keyCode == "13") {
            $("#searchForm").submit();
        }
    });

}).call(this);
