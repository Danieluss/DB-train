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
    txt+=`<p>${params["name"]}: ${value}</p>`
    insertHtml(htmlId, txt)
}

function showInput(htmlId, value, params) {
    var txt=""
    txt+= `<p>${params[name]}</p>`
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
    if(params[type] == "checkbox") {
        if(value) {
            txt+=" checked"
        }
    } else {
        txt+=`value='${value}'`
    }
    txt+="/>"
    insertHtml(htmlId, txt)
}

function showSearch(htmlId, value, params) {
    var txt = ""
    txt+= `<p>${params[name]}</p>`
    txt+= `<input name='${htmlId}' list='${htmlId}-datalist'/>`
    insertHtml(htmlId, txt)
    $(`[name='${htmlId}']`).on("keyup", function() {
        updateSearch()
    })
    function updateSearch() {
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
                if(val == query) {
                    $(`[name=${htmlId}]`).attr('return', options[i][params.return])
                    resultSet = true
                }
            }
            if(!resultSet) {
                $(`[name=${htmlId}]`).attr('return', undefined)
            }
            $(`#${htmlId}-datalist`).html(txt)
        })
    }

}

function showObject(htmlId, value, params) {
    var txt=""
    for(var i=0; i < params.length; i++) {
        txt+=`<div id='${htmlId}-${params[i].name}-container'></div>`
    }
    insertHtml(htmlId, txt)
    for(var i=0; i < params.length; i++) {
        showObject[params.type](htmlId + "-" + params[i].name, value[params[i].name], params[i])
    }
}

function showArray(htmlId, value, params) {

}

showObject = {
    __info__: showInfo,
    text: showInput,
    number: showInput,
    checkbox: showInput,
    search: showSearch,
    __list__: showArray
}

function insertHtml(htmlId, txt) {
    $(`#${htmlId}-container`).html(txt)
}

function showEditForm() {
    $("#mainContent").html(`<div id='#${name}-container'></div>`)
    showArray(name, obj, edit[name])
}


function getInputValue(htmlId, params) {
    var id = `[name=${htmlId}]`
    if(params["type"] == "checkbox") {
        return $(id).is(":checked")
    } else {
        return $(id).val()
    }
}