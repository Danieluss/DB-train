function loadEdit() {
    if(id != 0) {
        var url = api+name+"/get/" + id
        $.get(url, function(data) {
            obj = data
            showEditForm()
        }).fail(function(){
            errorPage()
        })
    } else {
        var url = api+name+"/fields"
        $.get(url, function(data) {
            obj = getDefaultObject(data)
            showEditForm()
        })
    }
}

function getDefaultObject(data) {
    var res = {}
    var keys = Object.keys(data)
    for(var i=0; i < keys.length; i++) {
        var s = data[keys[i]]
        var t
        if(s == "String") {
            t = ""
        } else if(s == "Integer" || s == "Long" || s == "Double") {
            t = 0
        } else if(s == "Date") {
            t = new Date().getTime()
        } else if(s == "Set" || s == "List") {
            t = []
        } else {
            t = 0
        }
        res[keys[i]] = t
    }
    return res
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
    } else if(params.type == "date") {
        var d = new Date(value)
        txt+= `value='${d.toISOString().slice(0, 10)}'`
    } else {
        txt+=`value='${value}'`
    }
    txt+="/>"
    insertHtml(htmlId, txt)
}

function updateSearch(htmlId, params) {
    var  query = $(`[name='${htmlId}']`).val()
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
                console.log(`[name='${htmlId}']`)
                $(`[name='${htmlId}']`).attr('return', options[i][params.return])
                resultSet = true
            }
        }
        if(!resultSet) {
            $(`[name='${htmlId}']`).attr('return', undefined)
        }
        $(`#${htmlId}-datalist`).html(txt)
    })
}

function showSearch(htmlId, value, params) {
    console.log(htmlId)
    var txt = ""
    txt+= `<p>${params.name}</p>`
    txt+= `<input name='${htmlId}' list='${htmlId}-datalist'/>`
    txt+= `<datalist id='${htmlId}-datalist'></datalist>`
    insertHtml(htmlId, txt)
    $(`[name='${htmlId}']`).bind("keyup input", function() {
        updateSearch(htmlId, params)
    })
    $.get(api+params.object+'/get/'+value, function(data){
        $(`[name='${htmlId}']`).val(data[params.searchBy])
        updateSearch(htmlId, params)
    })
}

function showUsedSearch(htmlId, value, params) {
    console.log("usedsearch: ", htmlId)
    $.get(api+params.object+'/get/'+value, function(data){
        var txt = `<p>${data[params.searchBy]}</p>`
        insertHtml(htmlId, txt)
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
    var txt=`<div><p>${params.name}</p>`
    txt+=`<div id='${htmlId}-new-container'></div>`
    txt+=`<a id='${htmlId}_new' href="#" title="Add">${getIcon("add")}</a>`
    txt+="</div>"
    for(var i=0; i < value.length; i++) {
        var curId = htmlId+"-"+i
        txt+='<div>'
        txt+=`<div id='${curId}-container'></div>`
        if(params.swapping) {
            if(i > 0) {
                txt+=`<a id='${curId}_up' href="#" title="Move up">${getIcon("keyboard_arrow_up")}</a>`
            }
            if(i+1 < value.length) {
                txt+=`<a id='${curId}_down' href="#" title="Move down">${getIcon("keyboard_arrow_down")}</a>`
            }
        }
        txt+=`<a id='${curId}_delete' href="#" title="Delete">${getIcon("delete")}</a>`
        txt+='</div>'
    }
    insertHtml(htmlId, txt);
    showSearch(htmlId+"-new", 0, params.arr[0])
    $(`#${htmlId}_new`).on("click", function() {
        var id = getSearch(htmlId+"-new", params.arr[0])
        for(var i=0; i < value.length; i++) {
            if(value[i] == id) {
                alert("Object already on the list!")
                return
            }
        }
        if(id > 0) {
            getArray(htmlId, value, params)
            value.push()
            showArray(htmlId, value, params)
        } else {
            alert("No such object!")
        }
    })
    for(var i=0; i < value.length; i++) {
        var curId = htmlId+"-"+i
        var id = i
        if(params.swapping) {
            showSearch(curId, value[i], params.arr[0])
            if(i > 0) {
                $("#"+curId+"_up").on("click", function(){
                    var tmp = value[id-1]
                    value[id-1] = value
                    value[id] = tmp
                })
            }
            if(i+1 < value.length) {
                $("#"+curId+"_down").on("click", function() {
                    var tmp = value[id+1]
                    value[id+1] = value
                    value[id] = tmp
                })
            }
        } else {
            showUsedSearch(curId, value[i], params.arr[0])
        }
        $("#"+curId+"_delete").on("click", function() {
            getArray(htmlId, value, params)
            for(var j=id; j+1 < value.length; j++) {
                value[j] = value[j+1]
            }
            value.pop()
            showArray(htmlId, value, params)
        })
    }
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

function showEditForm() {
    var txt = `<h1>${id == 0 ? "Add" : "Edit"} ${name}</h1>`
    txt+=`<div id='${name}-container'></div>`
    txt+=`<button onclick="submitEditForm()">Submit</button>`
    $("#mainContent").html(txt)
    
    showObject(name, obj, edit[name])
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
    var value = $(`[name='${htmlId}']`).attr('return')
    return value
}

function getArray(htmlId, value, params) {
    for(var i=0; i < value.length; i++) {
        if(params.swapping) {
            value[i] = getArray(htmlId+"-"+i, params.arr[0])
        }
    }
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
            value[params[i].name] = getSth[params[i].type](newHtmlId, params[i])
        }
    }
}

function saveObject() {
    getObject(name, obj, edit[name])
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
            alert(url.href)
            location.href = url.href
        }, function(xhr, err) {
            console.log(xhr, err)
        })
    } else {
        //there are errors in your form
    }
}