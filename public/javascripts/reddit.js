$(document).ready(function () {
    jQuery("#search").click(function () {
        var a = document.getElementById("searchKey").value;
        htmlobj = jQuery.ajax({
            url: "/search?",                            //将数据data发送到的路径
            data: "term=" + a.toString(),
            async: true,
            type: "get",
            success: function (data) {
                console.log(data);
                    var resdiv=$('<div></div>');        //创建一个父div
                      //给父div设置id
                $("#result").prepend(resdiv);
                $.each(data, function (i, item) {
                        if (i >= 10) {
                            return;
                        }
                        if (i == 0){
                            resdiv.append(
                                `<div>
                                  <br>
                                      <h2>Search terms: <a href="http://localhost:9000/wordstats?term=${a.toString()}" target="_blank">${a.toString()}</a></h2>
                                      <ol>`
                            );
                        }
                        resdiv.append(
                            ` <li style="margin-bottom:10px ">${i+1}: Author: <a href="http://localhost:9000/user/profile?author=${item.author}" target="_blank"> ${item.author}</a>, <a href="http://localhost:9000/searchsub?term=${item.subReddit}">${item.subReddit}</a>, "${item.submission}"</li></ol>
                              </div>`);
                    })
            },
            err: function () {
                     alert("error!");
                window.location.reload();
            }
        });
    });

    $('#searchKey').bind('keypress', function (event) {
        if (event.keyCode == "13") {
            $("#search").click();
        }
    })
});
