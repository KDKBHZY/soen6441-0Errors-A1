const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);
const product = urlParams.get('term');
console.log(product);
console.log("Waiting for WebSocket");
ws = new WebSocket("ws://" + location.host + "/wordstatsws");
ws.onopen = () => ws.send(JSON.stringify({query:product }));

var count = 0;
ws.onmessage = function(event){
    message = JSON.parse(event.data);
    if (message.term == product ){
        count++;
        return parseWordstats(message);
    }
};

function parseWordstats(message) {



}