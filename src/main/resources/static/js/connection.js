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

function getTime(t) {
    return new Date(t).toISOString().slice(11, 16)
}

function showInfoTime(htmlId, value, params) {
    var txt=""
    var t = value[0]*1000*3600+value[1]*1000*60
    txt+=`<p>${params.name}: ${getTime(t)}</p>`
    insertHtml(htmlId, txt)
}

function prepareStationsConnections(sc) {
    var dt = sc.departure[0]*60+sc.departure[1]-sc.arrival[0]*60-sc.arrival[1]
    sc.stopTime = dt
    console.log(sc.stop)
    return sc
}

function showConnectionArray(htmlId, value, params) {
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
    var txt=`<div><p>${params.name}</p>`
    txt+=`<div id="${htmlId}-departure-container"></div>`
    for(var i=0; i < value.length; i++) {
        var curId = htmlId+"-"+i
        txt+='<div>'
        txt+=`<div id='${curId}-container'></div>`
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
        if(id == 0) {
            showInput(htmlId+"-departure", getTime(arrToTime(stationsconnections[id].departure)*60*1000), params.departure)
        }
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
    return arr[0]*60+arr[1]
}

function timeToArr(t) {
    console.log(t)
    return [Math.floor(t/60), t%60]
}

function unformatTime(s) {
    var arr = s.split(":")
    return parseInt(arr[0])*60+parseInt(arr[1])
}

function getConnectionArray(htmlId, value, params) {
    var dt=0
    for(var i=0; i < stationsconnections.length; i++) {
        var sc = stationsconnections[i]
        var curId = htmlId+"-"+i
        sc.stop = getInput(curId+"-stop", params.arr[0])
        console.log("sc.stop:", sc.stop)
        var dep = arrToTime(sc.departure)
        var arr = arrToTime(sc.arrival)
        sc.stopTime = getInput(curId+"-stopTime", params.arr[3])
        if(i == 0) {
            var t = unformatTime(getInput(htmlId+"-departure", params.departure))
            dt = t-dep
            dt-=sc.stopTime-(dep-arr)
        }
        sc.arrival = timeToArr(dt+arr)
        dt+=sc.stopTime-(dep-arr)
        sc.departure = timeToArr(dep+dt)
        delete stationsconnections[i].stopTime
        console.log("sc:", stationsconnections[i])
    }
}

function saveConnectionArray() {
    for(var i=0; i < stationsconnections.length; i++) {
        saveSingleStop(i)
    }
    function saveSingleStop(sid) {
        stationsconnections[sid].connection = id
        console.log(stationsconnections[sid])
        postJson(api+"stationsconnections/upsert", stationsconnections[sid], function(data) {
            stationsconnections[sid] = data
            console.log(data)
            showConnectionForm()
        })
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