function loadDoc() {
    var a = document.getElementById("searchKey").value;
    console.log(a);

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === 4 && this.status === 200) {
            console.log(this.responseText);
            document.getElementById("text").innerHTML = this.responseText;
        }
    };
    xhttp.open("GET", "https://api.pushshift.io/reddit/search/submission/?q="+a+"&size=10&sort=asc", true);
    xhttp.send();
}