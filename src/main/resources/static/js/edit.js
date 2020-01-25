function loadEdit() {
    var url = api+name+"/get/" + id
    $.get(url, function(data) {
        obj = data
        showEditForm()
    }).fail(function(){
        errorPage()
    })
}

function showInfo(htmlId, value, params) {
    var txt=""
    txt+=`<p>${params.name}: ${value}</p>`
    insertHtml(htmlId, txt)
}

function showInput(htmlId, value, params) {
    var txt=""
    txt+= `<p>${params.name}</p>`
    keys = Object.keys(params)
    txt+="<input"
    for(var i=0; i < keys.length; i++) {
        var key = keys[i]
        if(key == "name") {
            txt+=` name='${htmlId}'`
        } else {
            txt+=` ${key}='${params[key]}'`
        }
    }
    if(params.type == "checkbox") {
        if(value) {
            txt+=" checked"
        }
    } else {
        txt+=`value='${value}'`
    }
    txt+="/>"
    insertHtml(htmlId, txt)
}

function updateSearch(htmlId, params) {
    var query = $(`[name='${htmlId}']`).val()
    var url = api+params.object+"/"
    if(query != "") {
        url+="search/page/"+query
    } else {
        url+="page"
    }
    var sortBy = "id"
    if(lists[params.object][0].length == 1) {
        sortBy = lists[params.object][0][0]
    }
    url+="?page=0&size=10&sort=" + sortBy
    $.get(url, function(data){
        var options = data.content
        var txt = ""
        var resultSet = false;
        for(var i=0; i < options.length; i++) {
            var val = options[i][params.searchBy]
            txt+=`<option value="${val}"/>`
            console.log(val, query)
            if(val == query) {
                $(`[name='${htmlId}']`).attr('return', options[i][params.return])
                resultSet = true
            }
        }
        // if(!resultSet) {
        //     $(`[name='${htmlId}']`).attr('return', undefined)
        // }
        $(`#${htmlId}-datalist`).html(txt)
    })
}

function showSearch(htmlId, value, params) {
    var txt = ""
    txt+= `<p>${params.name}</p>`
    txt+= `<input name='${htmlId}' list='${htmlId}-datalist'/>`
    txt+= `<datalist id='${htmlId}-datalist'></datalist>`
    insertHtml(htmlId, txt)
    $(`[name='${htmlId}']`).bind("keyup", function() {
        updateSearch(htmlId, params)
    })
    $.get(api+params.object+'/get/'+value, function(data){
        $(`[name='${htmlId}']`).val(data[params.searchBy])
        updateSearch(htmlId, params)
    })
}

function showObject(htmlId, value, params) {
    var txt=""
    for(var i=0; i < params.length; i++) {
        txt+=`<div id='${htmlId}-${params[i].name}-container'></div>`
    }
    insertHtml(htmlId, txt)
    for(var i=0; i < params.length; i++) {
        showSth[params[i].type](htmlId + "-" + params[i].name, value[params[i].name], params[i])
    }
}

function showArray(htmlId, value, params) {

}

showSth = {
    __info__: showInfo,
    text: showInput,
    number: showInput,
    checkbox: showInput,
    date: showInput,
    __search__: showSearch,
    __list__: showArray
}

function insertHtml(htmlId, txt) {
    $(`#${htmlId}-container`).html(txt)
}

function getGlobalParams() {
    var e = edit[name]
    //special case for tickets - TODO?
    if(name == "ticket") {
        if(obj.startDate != undefined) {
            e = edit.commutationticket
        } else {
            e = edit.pathticket
        }
    }
    return e
}

function showEditForm() {
    var txt = `<h1>${id == 0 ? "Add" : "Edit"} ${name}</h1>`
    txt+=`<div id='${name}-container'></div>`
    txt+=`<button onclick="submitEditForm()">Submit</button>`
    $("#mainContent").html(txt)
    
    showObject(name, obj, getGlobalParams())
}


function getInput(htmlId, params) {
    var id = `[name=${htmlId}]`
    if(params["type"] == "checkbox") {
        return $(id).is(":checked")
    } else {
        return $(id).val()
    }
}

function getSearch(htmlId, params) {
    updateSearch(htmlId, params)
    var value = $(`[name='${htmlId}']`).attr('return')
    console.log(value)
    return value
}

function getArray(htmlId, value, params) {

}

getSth = {
    __info__: undefined,
    text: getInput,
    number: getInput,
    checkbox: getInput,
    date: getInput,
    __search__: getSearch,
    __list__: getArray
}

function getObject(htmlId, value, params) {
    for(var i=0; i < params.length; i++) {
        var newHtmlId = htmlId + "-" + params[i].name
        if(params[i].type == "__info__") {
            continue;
        }
        if(params[i].type == "__list__") {
            getArray(newHtmlId, value[params[i].name], params[i])
        } else {
            console.log(params[i])
            value[params[i].name] = getSth[params[i].type](newHtmlId, params[i])
        }
    }
    console.log(value)
}

function saveObject() {
    getObject(name, obj, getGlobalParams())
    return true
}

function submitEditForm() {
    if(saveObject()) {
        console.log(obj)
        postJson(api+name+"/upsert/", obj, function(data) {
            obj = data
            console.log(obj)
            id = data.id
            var url = new URL(location.href)
            url.searchParams.set('id', id)
            localStorage.href = url.href
            // location.reload()
        }, function(xhr, err) {
            console.log(xhr, err)
        })
    } else {
        //there are errors in your form
    }
}