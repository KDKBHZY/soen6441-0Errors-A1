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
        });
       console.log(htmlobj.responseText);
      //jQuery("#text").html(htmlobj.responseText);
    });
});
