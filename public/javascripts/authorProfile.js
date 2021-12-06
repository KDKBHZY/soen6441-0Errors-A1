const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);
const authorName = urlParams.get('author');
console.log(authorName);
console.log("Waiting for WebSocket");
ws = new WebSocket("ws://" + location.host + "/authorprofilews");
ws.onopen = () => ws.send(JSON.stringify({query: authorName}));

var count = 0;
ws.onmessage = function (event) {
    message = JSON.parse(event.data);
    console.log(message);
    if (message.name === authorName) {
        return parseUser(message);
    }
};

function parseUser(message) {
    const name = document.getElementById("name");
    name.innerHTML = "Author: " + message.name;
    const userId = document.getElementById("user_id");
    userId.innerHTML = "User ID: " + message.id;
    const createDate = document.getElementById("create_date");
    createDate.innerHTML = "Create Date: " + message.created;
    const total = document.getElementById("total");
    total.innerHTML = "Total Karma: " + message.total_karma;
    const awardee = document.getElementById("awardee");
    awardee.innerHTML = "Awardee Karma: " + message.awardee_karma;
    const awarder = document.getElementById("awarder");
    awarder.innerHTML = "Awarder Karma: " + message.awarder_karma;
    const comment = document.getElementById("comment");
    comment.innerHTML = "Comment Karma: " + message.comment_karma;
    const link = document.getElementById("link");
    link.innerHTML = "Link karma: " + message.link_karma;
    const snoovatar = document.getElementById("snoovatar");
    if (message.snoovatar_img !== "") {
        snoovatar.src = message.snoovatar_img;
    }

    parseReddits(message.postedReddits);
}

function parseReddits(message) {
    console.log(message);

    $("#submissions").html("");
    $("#submissions").prepend('<ol id="posts"></ol>');
    redditListQuery1 = $("#posts");

    for (let i = 0; i < message.length; i++) {
        redditListQuery1.append('<li style="margin-bottom:10px "> Author: <a href="http://localhost:9000/user/profile?author=' + message[i].author + '" target="_blank">' + message[i].author + '</a>, <a href="http://localhost:9000/searchsub?term=' + message[i].subReddit + '" target="_blank">' + message[i].subReddit + '</a>,' + message[i].title + '</li>');
    }
}




