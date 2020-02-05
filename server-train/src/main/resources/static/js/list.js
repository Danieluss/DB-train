function loadList() {
    var txt="<h1>" + name + "s</h1>"
    txt+=`<a href='${getEditLink(0)}' title='add new'>${getIcon("add")}</a>`
    txt+="<div id='search'>"
    txt+="<p>Search:</p>"
    txt+="<input class='form-control' type='text' id='searchBar'/>"
    txt+="</div>"
    txt+="<div id='list'></div>"
    $("#mainContent").html(txt)
    $("#searchBar").on("keyup", function(){
        query = $("#searchBar").val()
        page = 0
        requestList()
    })
    query = ""
    page = 0
    requestList()
}

function requestList() {
    var url = api+name+"/"
    if(query != "") {
        url+="search/deep"
    } else {
        url+="page"
    }
    var sortBy = "id"
    if(lists[name][0].length == 1) {
        sortBy = lists[name][0][0]
    }
    url+="?depth=2&page=" + page + "&size=10&sort=" + sortBy
    if(query != "") {
        url+=`&query=${query}`
    }
    $.get(url, function(data){
        obj = data.content
        totalPages = data.totalPages
        showList()
    })
}

function getEditLink(id) {
    var params = [
        {key: "action", value: "edit"},
        {key: "name", value: name},
        {key: "id", value: id}
    ]
    return createUrl(params)
}

function deleteEntry(i) {
    var idkey = edit[name][0]["name"]
    var id = obj[i][idkey]
    if(confirm("Are you sure, that you want to delete this " + name + "?")) {
        $.ajax({
            url: api + name + "/delete/" + id,
            type: "DELETE",
            success: function() {
                requestList()
            },
            error: function() {
                alert("Could not delete object because it has connections to other objects")
            }
        })
    }
}

function showListEntry(htmlId, id, entry) {
    var txt='<td>'
    for(var i=0; i < lists[name].length; i++) {
        if(i > 0) {
            txt+= " "
        }
        var key = lists[name][i]
        console.log(key)
        if(Array.isArray(key)) {
            if(key.length > 1) {
                txt+=`<span id="${htmlId}-${i}-container"></span>`
            } else {
                txt+=entry[key[0]]
            }
        } else {
            txt+=key
        }
    }
    txt+='</td>'
    txt+='<td>'
    txt+='<a href="' + getEditLink(entry[edit[name][0]["name"]]) + '" title="Edit">' + getIcon("edit") + '</a>'
    txt+='</td>'
    txt+='<td>'
    txt+='<a href="#" onclick="deleteEntry(' + id + ')" title="Delete">' + getIcon("delete") + '</a>'
    txt+='</td>'
    $("#"+htmlId).html(txt)
    console.log(txt)
    for(var i=0; i < lists[name].length; i++) {
        var key = lists[name][i]
        if(Array.isArray(key) && key.length > 1) {
            showRecurrentValue(`${htmlId}-${i}`, entry[key[0]], key.slice(1))
        }
    }
}

function showList() {
    var txt=""
    if(obj.length > 0) {
        txt+="<table>"
        for(var i=0; i < obj.length; i++) {
            txt+=`<tr id="entry-${i}">`
            txt+='</tr>'
        }
        txt+='</table>'
    } else {
        txt+='Sorry, there are no more elements'
    }
    if(page > 0)
        txt+='<a href="#" id="previousPage" title="Previous page">' + getIcon("navigate_before") + '</a>'
    if(page < totalPages-1)
        txt+='<a href="#" id="nextPage" title="Next page">' + getIcon("navigate_next") + '</a>'
    $("#list").html(txt)
    for(var i=0; i < obj.length; i++) {
        showListEntry(`entry-${i}`, i, obj[i])
    }
    if(page > 0) {
        $("#previousPage").on("click", function(){
            page--
            requestList()
        })
    }
    if(page < totalPages-1) {
        $("#nextPage").on("click", function(){
            page++
            requestList()
        })
    }
}