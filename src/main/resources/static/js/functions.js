api = "http://localhost:8080/api/"

function createUrl(params) {
    // var baseUrl = "file:///home/kamil/Documents/Studia/5/sbd/DB-train/frontend/"
    var baseUrl = "http://localhost:8080/"
    const url = new URL("index.html", baseUrl)
    for(var i=0; i < params.length; i++) {
        url.searchParams.append(params[i].key, params[i].value)
    }
    return url.href
}

function loadMenu() {
    var txt="<ul>"
    for(var i=0; i < menu.length; i++) {
        txt+="<li>"
        params = [{key: "action", value: "list"}, {key: "name", value: menu[i]}]
        txt+="<a href='" + createUrl(params) + "'>"
        txt+=menu[i] + "s"
        txt+="</a>"
        txt+="</li>"
    }
    txt+="</ul>"
    $("#sideMenu").html(txt)
}

function errorPage() {
    var txt = "<h1>Sorry, this page does not exist. Use menu to find the appropriate page.</h1>"
    $("#mainContent").html(txt)
}

function loadContent() {
    loadMenu()
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    action = urlParams.get("action")
    name = urlParams.get("name")
    if(action == "edit") {
        id = urlParams.get("id")
        loadEdit(name, id)
    } else if(action == "list") {
        loadList(name)
    } else {
        errorPage()
    }
}

function getIcon(icon) {
    return '<i class="material-icons">' + icon + '</i>'
}

function postJson(url, data, success, error) {
    $.ajax({
        type: "POST",
        contentType:"application/json; charset=utf-8",
        url: url,
        data: JSON.stringify(data),
        success: success,
        error: error
    })
}