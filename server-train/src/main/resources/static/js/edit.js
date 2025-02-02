function loadEdit() {
    err = {}
    if(name == "connection") {
        loadConnection()
        return
    }
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
            if(name == "pathticket" || name == "commutationticket") {
                if(name == "pathticket") {
                    let urlParams = new URLSearchParams(window.location.search);
                    if(urlParams.get("stationConnection1") != undefined && urlParams.get("stationConnection1") != undefined) {
                        obj.stationConnection1 = urlParams.get("stationConnection1")
                        obj.stationConnection2 = urlParams.get("stationConnection2")
                    } else {
                        loadFindConnection()
                        return
                    }
                }
                url = api+"ticket/fields"
                $.get(url, function(data) {
                    obj = Object.assign(obj, getDefaultObject(data))
                    showEditForm()
                })
            } else {
                showEditForm()
            }
        })
    }
}

function getDefaultObject(data) {
    var res = {}
    var keys = Object.keys(data)
    for(var i=0; i < keys.length; i++) {
        if(keys[i] == "TOOLTIPS") {
            continue
        }
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
        } else if(s == "UUID") {
            t = ""
        } else if(s == "Train") {
            t = null  
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

function showRecurrentInfo(htmlId, value, params) {
    var txt=`<p>${params.comment}: <span id='${htmlId}-rec-container'></span></p>`
    insertHtml(htmlId, txt)
    showRecurrentValue(htmlId+"-rec", value, params.arr)
}

function formatDate(value) {
    var d = new Date(value)
    return d.getFullYear().toString().padStart(4, '0') +
        '-'  + (d.getMonth()+1).toString().padStart(2, '0') +
        '-' + d.getDate().toString().padStart(2, '0')
}

function formatTime(value) {
    var t = new Date(value)
    return t.getHours().toString().padStart(2, '0') +
        ':' + t.getMinutes().toString().padStart(2, '0')
}

function showInput(htmlId, value, params) {
    var txt=""
    txt+= `<label for='${htmlId}'>${params.name}</label>`
    keys = Object.keys(params)
    txt+=`<input class='form-control' id='${htmlId}' autocomplete='off'`
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
        txt+= `value='${formatDate(value)}'`
    } else if(params.type == "time") {
        txt+= `value='${formatTime(value)}'`
    } else if(params.type == "password") {
        txt+= `value=""`
    } else {
        txt+=`value='${value}'`
    }
    txt+="/>"
    insertHtml(htmlId, txt)
}

function showSelect(htmlId, value, params) {
    var txt=""
    txt+= `<label for='${htmlId}'>${params.name}</label>`
    txt+= `<select class='form-control' name='${htmlId}'>`
    for(var i=0; i < params.options.length; i++) {
        txt+= `<option value='${params.options[i]}' ${value == params.options[i] ? "selected" : ""}>${params.options[i]}</option>`
    }
    txt+= `</select>`
    insertHtml(htmlId, txt)
}

function showSearch(htmlId, value, params) {
    var previousQuery = undefined
    var txt = ""
    txt+= `<label for='${htmlId}'>${params.name}</label>`
    txt+= `<input class="form-control" title='${params.title}' id='${htmlId}' type="text" name='${htmlId}' list='${htmlId}-datalist'/>`
    txt+= `<datalist id='${htmlId}-datalist' open='open'></datalist>`
    insertHtml(htmlId, txt)
    $(`[name='${htmlId}']`).bind("keyup input", function() {
        updateSearch()
    })
    if(value > 0) {
        $.get(api+params.object+'/get/'+value, function(data){
            $(`[name='${htmlId}']`).val(data[params.searchBy])
            updateSearch()
        })
    } else {
        updateSearch()
    }
    function updateSearch() {
        var query = $(`[name='${htmlId}']`).val()
        if(query == previousQuery) {
            return
        }
        previousQuery = query
        var url = api+params.object+"/"
        if(query != "" || params.null != undefined) {
            url+="filter/page"
        } else {
            url+="page"
        }
        var sortBy = "id"
        if(lists[params.object][0].length == 1) {
            sortBy = lists[params.object][0][0]
        }
        url+="?page=0&size=10&sort=" + sortBy
        if(query != "" || params.null != undefined) {
            url+="&query="
            if(query != "") {
                url+=`${params.searchBy}__like__${query},`
            }
            if(params.null != undefined) {
                url+=`${params.null}__null__`
            }
        }
        $.get(url, function(data){
            var options = data.content
            var txt = ""
            var resultSet = false;
            for(var i=0; i < options.length; i++) {
                var val = options[i][params.searchBy]
                txt+=`<option value="${val}"/>`
                if(val == query) {
                    $(`[name='${htmlId}']`).attr('return', options[i][params.return])
                    resultSet = true
                }
            }
            if(!resultSet) {
                $(`[name='${htmlId}']`).attr('return', null)
            }
            $(`#${htmlId}-datalist`).html(txt)
        })
    }
}

function showUsedSearch(htmlId, value, params) {
    $.get(api+params.object+'/get/'+value, function(data){
        var txt = `<p>${data[params.searchBy]}</p>`
        insertHtml(htmlId, txt)
    })
}

function getSubId(htmlId, params) {
    var res = htmlId+"-"+params.name
    if (params.comment != undefined) {
        res+=params.comment
    }
    return res
}

function showObject(htmlId, value, params, mainObject=false) {
    var txt=""
    for(var i=0; i < params.length; i++) {
        var curId = getSubId(htmlId, params[i])
        txt+=`<div id='${curId}-container'></div>`
        txt+=`<div id=${curId}-error class="err"></div>`
    }
    insertHtml(htmlId, txt)
    for(var i=0; i < params.length; i++) {
        var curId = getSubId(htmlId, params[i])
        showSth[params[i].type](curId, value[params[i].name], params[i])
        var errId = `#${curId}-error`
        if(mainObject) {
            $(errId).addClass("block")
        }
        if(err[curId] != undefined) {
            $(errId).text(err[curId])
        } else if(err[params[i].name] != undefined) {
            $(errId).text(err[params[i].name])
        }
    }
}

function showArray(htmlId, value, params) {
    var txt=`<p>${params.name}</p>`
    txt+=`<div class="form-row changingList" >`
    txt+=`<div id='${htmlId}-new-container'></div>`
    txt+=`<a id='${htmlId}_new' href="#" title="Add">${getIcon("add")}</a>`
    txt+="</div>"
    for(var i=0; i < value.length; i++) {
        var curId = htmlId+"-"+i
        txt+='<div class="form-row changingList">'
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
            value.push(id)
            showArray(htmlId, value, params)
        } else {
            alert("No such object!")
        }
    })
    for(var i=0; i < value.length; i++) {
        prep(i)
    }
    function prep(id) {
        var curId = htmlId+"-"+id
        if(params.swapping) {
            showSearch(curId, value[id], params.arr[0])
            if(id > 0) {
                $("#"+curId+"_up").on("click", function(){
                    getArray(htmlId, value, params)
                    var tmp = value[id-1]
                    value[id-1] = value[id]
                    value[id] = tmp
                    showArray(htmlId, value, params)
                })
            }
            if(id+1 < value.length) {
                $("#"+curId+"_down").on("click", function() {
                    getArray(htmlId, value, params)
                    var tmp = value[id+1]
                    value[id+1] = value[id]
                    value[id] = tmp
                    showArray(htmlId, value, params)
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
    __recurrentInfo__: showRecurrentInfo,
    text: showInput,
    password: showInput,
    number: showInput,
    checkbox: showInput,
    date: showInput,
    time: showInput,
    select: showSelect,
    __search__: showSearch,
    __usedSearch__: showUsedSearch,
    __list__: showArray,
    __connectionlist__: showConnectionArray,
    __showEdges__: showEdges
}

function insertHtml(htmlId, txt) {
    $(`#${htmlId}-container`).html(txt)
}

function showEditForm() {
    var txt = `<h1>${id == 0 ? "Add" : "Edit"} ${name}</h1>`
    txt+=`<div class="form-group" id='${name}-container'></div>`
    txt+='<div id="general-error" class="err"></div>'
    txt+=`<button class="btn btn-primary" onclick="submitEditForm()">Submit</button>`
    $("#mainContent").html(txt)
    
    showObject(name, obj, edit[name], true)
}

function isInvalid(htmlId) {
    return $(`[name='${htmlId}']`).is(":invalid")
}

function unformatTime(s) {
    var arr = s.split(":")
    return parseInt(arr[0])*60*60000+parseInt(arr[1])*60000
}

function getInput(htmlId, params) {
    var id = `[name=${htmlId}]`
    if(params["type"] == "checkbox") {
        return $(id).is(":checked")
    } else if(params["type"] == "time") {
        return unformatTime($(id).val())
    } else {
        if(isInvalid(htmlId)) {
            err[htmlId] = `${params.name} has to be a valid ${params.type}`
            if(params.min != undefined) {
                err[htmlId]+=`, not less than ${params.min}`
            }
            if(params.max != undefined) {
                err[htmlId]+=`, not greater than ${params.max}`
            }
        }
        return $(id).val()
    }
}

function getSelect(htmlId, params) {
    return $(`[name='${htmlId}'] option:selected`).text();
}

function getSearch(htmlId, params) {
    var value = $(`[name='${htmlId}']`).attr('return')
    if(!(value > 0)) {
        err[htmlId] = 'No object chosen'
    }
    return value
}

function getArray(htmlId, value, params) {
    for(var i=0; i < value.length; i++) {
        if(params.swapping) {
            value[i] = getSearch(htmlId+"-"+i, params.arr[0])
        }
    }
}

getSth = {
    __info__: undefined,
    text: getInput,
    password: getInput,
    number: getInput,
    checkbox: getInput,
    date: getInput,
    time: getInput,
    select: getSelect,
    __search__: getSearch,
    __usedSearch__: undefined,
    __list__: getArray,
    __connectionlist__: getConnectionArray,
}

function getObject(htmlId, value, params) {
    for(var i=0; i < params.length; i++) {
        var newHtmlId = getSubId(htmlId, params[i])
        if(getSth[params[i].type] == undefined) {
            continue;
        }
        if(params[i].type == "__list__" || params[i].type == "__connectionlist__") {
            getSth[params[i].type](newHtmlId, value[params[i].name], params[i])
        } else {
            value[params[i].name] = getSth[params[i].type](newHtmlId, params[i])
        }
    }
}

function saveObject() {
    getObject(name, obj, edit[name])
}

function submitEditForm() {
    err = {}
    saveObject()
    if(name == "connection" && id == 0) {
        obj.stations = []
    }
    submit(api+name+"/upsert/", obj, function(data) {
        obj = data
        console.log(obj)
        if(name == "pathticket" || name == "commutationticket") {
            id = data.uuid
        } else {
            id = data.id
        }
        if(name == "connection") {
            saveConnectionArray()
            return
        }
        var url = new URL(location.href)
        url.searchParams.set('id', id)
        if(location.href != url.href) {
            location.href = url.href
        } else {
            location.reload()
        }
    }, name == "connection" ? showConnectionForm : showEditForm)
}

function submit(url, obj, success, showIfError) {
    if($.isEmptyObject(err)) {
        postJson(url, obj, success, function(xhr) {
            console.log(xhr)
            err = JSON.parse(xhr.responseText)
            showIfError()
            var txt = "<p>There were errors in the form."
            if(err_comments[name] != undefined) {
                txt+=" " + err_comments[name]
            }
            txt+="</p>"
            if(err["crossValid"] != undefined) {
                txt+= `<p>${err["crossValid"]}</p>`
            }
            if(err["errors"] != undefined) {
                for(var i=0; i < err.errors.length; i++) {
                    txt+=`<p>${err.errors[i]}</p>`
                }
            }
            $("#general-error").html(txt)
        })
    } else {
        showIfError()
        // showEditForm()
        $("#general-error").text("There were errors in the form. It couldn't have been submitted.")
    }
}
