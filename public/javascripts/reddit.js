
var resArr =[];
(function() {
    var parseTweets;
    $(function() {
        if($("#search").length === 1) {
            var ws;
            console.log("Waiting for WebSocket");
            ws = new WebSocket("ws://" + location.host + "/ws");
            ws.onmessage = function (event) {

                console.log(event.data[10].author.toString());
                message = JSON.parse(event.data);

              // console.log(message);
                // switch (message.type) {
                //     case "status":
                        return parseTweets(message);
                //     default:
                //         return null;
                //
                // }
            };
            return $("#searchForm").submit(function (event) {
                event.preventDefault();
                if ($("#searchKey").val() !== '') {
                    console.log("Sending WS with value " + $("#searchKey").val());
                    ws.send(JSON.stringify({
                        query: $("#searchKey").val()
                    }));
                    return $("#searchKey").val("");
                }
            });
        }
    });

    parseTweets = function(message) {
        var query = $("#searchKey").val();
        tweetsListQuery = $("#tweetsList"+query);
        if (tweetsListQuery.length === 0) {
            $("#result").prepend('<div class="results"><h2>Search terms: '+query+'</h2><ol id="tweetsList'+query+'"></ol></div>');
        }
        tweetsListQuery.prepend('<li style="margin-bottom:10px "> Author: <a href="http://localhost:9000/user/profile?author='+ message.author+'" target="_blank">'+  message.author+'</a>, <a href="http://localhost:9000/searchsub?term='+message.subReddit+'" target="_blank">'+  message.subReddit+'</a>,'+  message.title+'</li>');

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
