const koleo = require('koleo')
var fs = require("fs")
arr = koleo.stations().then(function(arr) {
    stations = {};
    for(var i=0; i < arr.length; i++) {
        s = {};
        if(arr[i]["location"] == undefined) {
            continue
        }
        s["id"] = arr[i].id
        s["lat"] = arr[i]["location"]["latitude"];
        s["lon"] = arr[i]["location"]["longitude"];
        s["name"] = arr[i]["name"];
        s["region"] = arr[i]["location"]["region"];
        s["country"] = arr[i]["location"]["country"]
        s["slug"] = arr[i]["slug"]
        stations[arr[i].id] = s;
    }
    fs.writeFileSync("stations.json", JSON.stringify(stations));
})
