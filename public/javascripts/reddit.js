// function loadDoc() {
//     var a = document.getElementById("searchKey").value;
//     console.log(a);
//
//     var xhttp = new XMLHttpRequest();
//     xhttp.onreadystatechange = function() {
//         if (this.readyState === 4 && this.status === 200) {
//             console.log(this.responseText);
//             document.getElementById("text").innerHTML = this.responseText;
//         }
//     };
//     xhttp.open("GET", "https://api.pushshift.io/reddit/search/submission/?q="+a+"&size=10&sort=asc", true);
//     xhttp.send();
// }

$(document).ready(function () {
    // $("#loadData").click(function () {                     //这里用到jQuery，需要在布局页中引用jQuery
    //     htmlobj = jQuery.ajax({url: "/Content/txtData.txt",async: false});
    //     $("#demo").text(htmlobj.responseText);       //将从后台获取的数据展现在h3元素上
    // })
    //
    // $("#loadDataFromDb").click(function () {
    //     htmlobj = jQuery.ajax({
    //         url: "/GetData/Index", async: false });
    //     $("#demo").text(htmlobj.responseText);
    // })



    jQuery("#search").click(function () {
        var a = document.getElementById("searchKey").value;
        htmlobj = jQuery.ajax({
            url: "/search?",                            //将数据data发送到的路径
            data: "term="+a.toString(),
            async: true,
            type: "get",
            success: function(data){
                console.log(data);

                $("#text").append(
                    `<div><br><h2>Search terms: ${a.toString()}</h2></div>`
                );
                $.each(data, function(i, item) {
                    if(i>=10){
                        return false;
                    }
                    $("#text").append(
                        `<div>${i + 1}. Author: ${item.author}, ${item.subReddit}, "${item.submission}"</div>`);
                });
             //document.getElementById("text").innerHTML = data;

            }

        });
    });

    $('#searchKey').bind('keypress', function (event) {
        if (event.keyCode == "13") {
            $("#search").click();
        }
    })
});
