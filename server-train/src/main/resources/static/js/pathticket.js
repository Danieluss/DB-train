function testFindConnection() {
    data = {
        fromStation: 1919,
        toStation: 1908,
        time: '09:00:00',
        date: '2020-02-02'
    }
    postJson('http://localhost:8080/api/connection/findconnection', data, function(resp){
        console.log(resp)
    }, function(err) {
        console.log(err)
    })
}

function loadFindConnection() {
    obj = {}
    obj.fromStation = 0
    obj.toStation = 0
    obj.departure = new Date().getTime()
    obj.date = new Date().getTime()
    showFindConnectionForm()
}

function showFindConnectionForm() {
    var txt = `<h1>Add ${name}</h1>`
    txt+=`<div id="new-connection-container"></div>`
    txt+=`<button class="btn btn-primary" onclick="findConnection()">Find</button>`
    txt+=`<div id="connections-container"></div>`
    txt+=`<div id="general-error" class="err"></div>`
    $("#mainContent").html(txt)
    showObject("new-connection", obj, edit["pathticket_new"], true)
}

function findConnection() {
    err = {}
    getObject("new-connection", obj, edit["pathticket_new"])
    obj.time = timeToArr(obj.departure)
    console.log(obj)
    alert("BLOCK")
    submit(api+"connection/findconnection", obj, function(data) {
        var connections = data
        showFindConnectionForm()
        showConnections(connections)
    }, showFindConnectionForm)
}

function showConnections(connections) {
    var txt="";
    if(connections.length == 0) {
        txt+= "Sorry, there are no connections between chosen stations"
    } else {
        txt+="<table><tr><th>Departure</th><th>Arrival</th><th>Get ticket</th>"
        for(var i=0; i < connections.length; i++) {
            txt+="<tr>"
            txt+=`<td>${connections[i][0]}</td>`
            txt+=`<td>${connections[i][1]}</td>`
            var p = [
                {key: "action", value: "edit"},
                {key: "name", value: "pathticket"},
                {key: "id", value: 0},
                {key: "stationConnection1", value: connections[i][2]},
                {key: "stationConnection2", value: connections[i][3]}
            ]
            txt+=`<td><a href='${createUrl(p)}'>${getIcon("add")}</a></td>`
            txt+="</tr>"
        }
        txt+="<table>"
    }
    insertHtml("connections", txt)
}