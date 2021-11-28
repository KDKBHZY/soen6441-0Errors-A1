var resArr =[];
(function() {
    var parseTweets;
    $(function() {
        if($("#search").length === 1) {
            var ws;
            console.log("Waiting for WebSocket");
            ws = new WebSocket("ws://" + location.host + "/ws");
            ws.onmessage = function (event) {
                var message;
                message = JSON.parse(event.data);

                switch (message.type) {
                    case "status":
                        return parseTweets(message);
                    default:
                        return console.log(message);
                }
            };
            return $("#searchTweetsForm").submit(function (event) {
                event.preventDefault();
                if ($("#query").val() !== '') {
                    console.log("Sending WS with value " + $("#query").val());
                    ws.send(JSON.stringify({
                        query: $("#query").val()
                    }));
                    return $("#query").val("");
                }
            });
        }
    });

    parseTweets = function(message) {
        var query = message.query.replace(/ /g,'');
        tweetsListQuery = $("#tweetsList"+query);
        if (tweetsListQuery.length === 0) {
            $("#tweets").prepend('<div class="results"><p>Tweets for '+message.query+'</p><ul id="tweetsList'+query+'"></ul></div>');
        }
        tweetsListQuery.prepend('<li><a href="http://localhost:9000/profile/'+message.user.screen_name+'">'
            +message.user.screen_name+'</a> wrote: '+message.full_text+'</li>');

    }

}).call(this);

// $(document).ready(function () {
//     jQuery("#search").click(function () {
//         var a = document.getElementById("searchKey").value;
//         if (a == ""){
//             alert("Please enter a keyword")
//         }
//         else if (resArr.indexOf(a)>=0){
//             alert("You have searched "+ a +" before");
//             var repeat = document.getElementById(a.toString());
//             repeat.style.color= "lightgreen";
//             $("html,body").animate({scrollTop: $("#"+a.toString()).offset().top}, 1000);
//         }
//
//         else {
//             console.log(resArr);
//             resArr.push(a);
//             htmlobj = jQuery.ajax({
//                 url: "/search?",                            //将数据data发送到的路径
//                 data: "term=" + a.toString(),
//                 async: true,
//                 type: "get",
//                 success: function (data) {
//                     var resdiv = $('<div></div>');        //创建一个父div
//                     //给父div设置id
//                     $("#result").prepend(resdiv);
//                     resdiv.append(
//                         `<h2 >Search terms:<a href="http://localhost:9000/wordstats?term=${a.toString()}" target="_blank" id=${a.toString()}> ${a.toString()}</a></h2>
//                     <ol id="result-list"></ol> <br/>`
//                     );
//                     $.each(data, function (i, item) {
//                         if (i >= 10) {
//                             return;
//                         }
//
//                         $("#result-list").append(
//                             ` <li style="margin-bottom:10px "> Author: <a href="http://localhost:9000/user/profile?author=${item.author}" target="_blank"> ${item.author}</a>, <a href="http://localhost:9000/searchsub?term=${item.subReddit}" target="_blank">${item.subReddit}</a>, "${item.title}"</li></ol>
//                               `);
//                     })
//
//                 },
//                 err: function () {
//                     alert("error!");
//                     window.location.reload();
//                 }
//             });
//         }
//     });
//
//     $('#searchKey').bind('keypress', function (event) {
//         if (event.keyCode == "13") {
//             $("#search").click();
//         }
//     })
// });
