var request = require('sync-request')
var fs = require("fs")
var stations = JSON.parse(fs.readFileSync('stations.json'))
var keys = Object.keys(stations)
for(var i=0; i < keys.length; i++) {
    s = stations[keys[i]]
    res = {}
    res.id = 0
    res.latitude = s.lat
    res.longitude = s.lon
    res.name = s.name
    console.log(res)
    var resp = request('POST', 'http://localhost:8080/api/station/upsert', {
        json: res
    })
    // console.log(resp.body.toString('utf-8'))
}