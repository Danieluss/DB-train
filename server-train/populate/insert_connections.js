var request = require('sync-request');
var fs = require("fs")
var edges = JSON.parse(fs.readFileSync('edges.json'))
var connections = JSON.parse(fs.readFileSync('connections.json'))
var stations = JSON.parse(fs.readFileSync('stations.json'))
var inserted_stations = JSON.parse(request('GET', 'http://localhost:8080/api/station/list').getBody())

// console.log(inserted_stations)
dict = {}
for(var i=0; i < inserted_stations.length; i++) {
    dict[inserted_stations[i].name] = inserted_stations[i]
}

function mapStationId(id) {
    return dict[stations[id].name].id
}

function mapStationToName(id) {
    return stations[id].name
}

connection_id = 1000
for(var i=0; i < connections.length; i++) {
    try {
        c = connections[i]
        nc = {}
        nc.id = connection_id
        nc.name = c.name
        nc.firstStation = mapStationToName(c.stations[0].station)
        nc.lastStation = mapStationToName(c.stations[c.stations.length-1].station)
        nc.firstDay = '2020-01-01'
        nc.lastDay = '2020-06-30'
        console.log(nc)
        request('POST', 'http://localhost:8080/api/connection/upsert', {
            json: nc
        })
        for(var j=0; j < c.stations.length; j++) {
            s = c.stations[j]
            s.number = j
            s.stop = true
            s.connection = connection_id
            s.station = mapStationId(s.station)

            delete s.dist
            console.log(s)
            request('POST', 'http://localhost:8080/api/stationsconnections/upsert', {
                json: s
            })
        }
        connection_id++
    } catch(err) {}
}

for(var i=0; i < edges.length; i++) {
    try {
        e = edges[i]
        e.station1 = mapStationId(e.station1)
        e.station2 = mapStationId(e.station2)
        e.distance/=1000
        console.log(e)
        request('POST', 'http://localhost:8080/api/edge/upsert', {
            json: e
        })
    } catch(err) {}
}