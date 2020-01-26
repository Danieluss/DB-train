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

function showConnectionArray() {

}

function showConnectionForm() {
    var txt="";
    if(id == 0) {
        txt+=`<div id="new_connection-container"></div>`
        txt+=`<button onclick="generateConnection()">Generate</button>`
    }
    txt+=`<div id="connection-container"></div>`
    if(obj != undefined) {
        txt+=`<button onclick="submitEditForm()">Submit</button>`
    }
    txt+=`<div id="general-error"></div>`
    $("#mainContent").html(txt)
    if(id == 0) {
        if(stations == undefined) {
            stations = {"stations":[]}
        }
        showObject("connection_new", stations, edit["connection_new"])
    }
    if(obj != undefined) {
        showObject("connection", obj, edit["connection"])
    }
}

function generateConnection() {
    err = {}
    getObject("connection_new", stations, edit["connection_new"])
    if(stations.stations.length < 2) {
        $("#general-error").text("You have to specify at least 2 stations")
    }
    if($.isEmptyObject(err)) {
        console.log(obj)
        postJson(api+name+"/upsert/", stations, function(data) {
            obj = data
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

function getConnectionArray() {

}

function saveConnectionArray() {
    
}