function testConnection() {
    data = {
        stations:[1000, 2]
    }
    postJson('http://localhost:8080/api/connection/generate', data, function(resp){
        console.log(resp)
    }, function(err) {
        console.log(err)
    })
}

function loadConnection() {
    stationsconnections = []
    if(id == 0) {
        obj = undefined
        stations = {"stations":[]}
        showConnectionForm()
    } else {
        var url = api+name+"/get/" + id
        $.get(url, function(data) {
            obj = data
            showConnectionForm()
        }).fail(function(){
            errorPage()
        })
    }
}

function prepareStationsConnections(sc) {
    sc.departure_time = arrToTime(sc.departure)
    sc.arrival_time = arrToTime(sc.arrival)
    console.log(sc)
    return sc
}

function showConnectionArray(htmlId, value, params) {
    console.log("before: ", stationsconnections)
    if(value.length <= stationsconnections.length) {
        for(var i=0; i < stationsconnections.length; i++) {
            obj.stations[i] = (stationsconnections[i].id)
        }
        recurrentCallback(function(res){
            obj.firstStation = res
        }, stationsconnections[0].station, ["station", "name"])
        recurrentCallback(function(res){
            obj.lastStation = res
        }, stationsconnections[stationsconnections.length-1].station, ["station", "name"])
    }
    console.log("after: ", stationsconnections)
    var txt=`<div><p>${params.name}</p>`
    txt+=`<div id="${htmlId}-departure-container"></div>`
    for(var i=0; i < value.length; i++) {
        var curId = htmlId+"-"+i
        txt+='<div>'
        txt+=`<div class="form-row" id='${curId}-container'></div>`
        txt+='</div>'
    }
    txt+='</div>'
    insertHtml(htmlId, txt);
    for(var i=0; i < value.length; i++) {
        show(i)
    }
    function show(id) {
        if(stationsconnections[id] == undefined) {
            $.get(api+'stationsconnections/get/'+value[id], function(data) {
                console.log("id:", id)
                stationsconnections[id] = data
                showStationConnection(id)
            })
        } else {
            stationsconnections[id] = prepareStationsConnections(stationsconnections[id])
            showStationConnection(id)
        }
    }
    function showStationConnection(id) {
        stationsconnections[id] = prepareStationsConnections(stationsconnections[id])
        showObject(htmlId+"-"+id, stationsconnections[id], params.arr)
    }
}

function showConnectionForm() {
    var txt = `<h1>${id == 0 ? "Add" : "Edit"} ${name}</h1>`
    if(id == 0) {
        txt+=`<div id="new-connection-container"></div>`
        txt+=`<button class="btn btn-primary" onclick="generateConnection()">Generate</button>`
    }
    txt+=`<div id="connection-container"></div>`
    if(obj != undefined) {
        txt+=`<button class="btn btn-primary" onclick="submitEditForm()">Submit</button>`
    }
    txt+=`<div id="general-error"></div>`
    $("#mainContent").html(txt)
    if(id == 0) {
        showObject("new-connection", stations, edit["connection_new"])
    }
    if(obj != undefined) {
        showObject("connection", obj, edit["connection"])
    }
}

function generateConnection() {
    err = {}
    getObject("new-connection", stations, edit["connection_new"])
    if(stations.stations.length < 2) {
        $("#general-error").text("You have to specify at least 2 stations")
    }
    console.log(stations)
    if($.isEmptyObject(err)) {
        console.log(obj)
        postJson(api+"connection/generate", stations, function(data) {
            console.log(stationsconnections, data)
            stationsconnections = data
            $.get(api+"connection/fields", function(data) {
                obj = getDefaultObject(data)
                showConnectionForm()
            })
        }, function(xhr) {
            console.log(xhr)
            err = JSON.parse(xhr.responseText)
            showConnectionForm()
            $("#general-error").text("There were errors in the form.")
        })
    } else {
        showConnectionForm()
        $("#general-error").text("There were errors in the form. It couldn't have been submitted.")
    }
}

function arrToTime(arr) {
    return parseInt(arr[0])*1000*3600+parseInt(arr[1])*1000*60
}

function timeToArr(t) {
    var mod = 60*24
    t = (t%mod+mod)%mod
    console.log(t)
    return [Math.floor(t/60), t%60]
}

function isAfter(a, b) {
    var mod = 60*24
    return ((b-a)%mod+mod)%mod > mod/2
}

function getConnectionArray(htmlId, value, params) {
    for(var i=0; i < stationsconnections.length; i++) {
        var sc = stationsconnections[i]
        var curId = htmlId+"-"+i
        getObject(curId, sc, params.arr)
        if(i > 0 && true == isAfter(stationsconnections[i-1].departure_time, sc.arrival_time)) {
            err[curId+"-arrival_time"] = "Train cannot come to the next station before leaving the previous one."
        }
        if(isAfter(sc.arrival_time, sc.departure_time)) {
            err[curId+"-departure_time"] = "Arrival cannot be after departure"
        }
    }
    for(var i=0; i < stationsconnections.length; i++) {
        var sc = stationsconnections[i]
        sc.arrival = timeToArr(sc.arrival_time)
        sc.departure = timeToArr(sc.departure_time)
        delete sc.arrival_time
        delete sc.departure_time
    }
}

async function saveConnectionArray() {
    var p = []
    for(var i=0; i < stationsconnections.length; i++) {
        p[i] = await saveSingleStop(i)
    }
    function saveSingleStop(sid) {
        stationsconnections[sid].connection = id
        console.log(stationsconnections[sid])
        return postJson(api+"stationsconnections/upsert", stationsconnections[sid], function(data) {
            stationsconnections[sid] = data
            console.log(data)
            return true
        })
    }
    for(var i=0; i < stationsconnections.length; i++) {
        if(p[i] == true) {
            continue
        }
    }
    var url = new URL(location.href)
    url.searchParams.set('id', id)
    if(location.href != url.href) {
        location.href = url.href
    } else {
        location.reload()
    }
}

function showEdges(htmlId, value, params) {
    var txt="<p>Edges</p>"
    txt+="<table>";
    for(var i=0; i < value.length; i++) {
        var p = [
            {key: "action", value: "edit"},
            {key: "name", value: "edge"},
            {key: "id", value: id}
        ]
        txt+=`<tr><td id="edge-${i}-container"></td><td><a href='${createUrl(p)}'>${getIcon("edit")}</a></td></tr>`
    }
    txt+='</table>'
    insertHtml(htmlId, txt)
    for(var i=0; i < value.length; i++) {
        showRecurrentValue(`edge-${i}`, value[i], ["edge", "station2", "station", "name"])
    }
}