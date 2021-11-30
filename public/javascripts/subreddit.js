const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);
const product = urlParams.get('term');
console.log(product);
console.log("Waiting for WebSocket");
ws = new WebSocket("ws://" + location.host + "/subredditws");
ws.onopen = () => ws.send(JSON.stringify({query:product }));

var count = 0;
ws.onmessage = function(event){
    message = JSON.parse(event.data);
    if (message.term == product ){
        count++;
        return parseReddits(message);
    }
};
function parseReddits(message) {
    //handle blank
    //console.log(message);
    console.log(count);
    redditListQuery = $("#resquery");
    var size = document.getElementsByTagName("li");
    console.log(size);
    if (count > 10){
        count-=1;
        redditListQuery.prepend('<li style="margin-bottom:10px "> Author: <a href="http://localhost:9000/user/profile?author='+ message.author+'" target="_blank">'+  message.author+'</a>, <a href="http://localhost:9000/searchsub?term='+message.subReddit+'" target="_blank">'+  message.subReddit+'</a>,'+  message.title+'</li>');
         redditListQuery.deleteRow(size-1);
    }


}




