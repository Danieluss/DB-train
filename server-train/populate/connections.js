const koleo = require('koleo')
var fs = require("fs")
var dateFormat = require('dateformat');
var stations = JSON.parse(fs.readFileSync('stations.json'))
keys = Object.keys(stations)
edges = new Set()
connections = new Set()
n = keys.length
m = 100
p = 0


function ri(a, b) {
    return Math.floor(Math.random()*(b-a)+a);
}

function stationToPoint(station) {
    p = {}
    // console.log(station)
    p.slug = station.slug
    p.id = station.id
    p.type = "station"
    return p
}

function dateToTime(date) {
    return dateFormat(new Date(date), 'HH:MM:ss')
}

function saveSet(obj, filename) {
    arr = Array.from(obj)
    res = []
    for(var i=0; i < arr.length; i++) {
        res.push(JSON.parse(arr[i]))
    }
    fs.writeFileSync(filename, JSON.stringify(res));
}

function saveObjects() {
    saveSet(connections, 'connections.json')
    saveSet(edges, 'edges.json')
}

count = 0
for(q=0; q < m; q++) {
    a = ri(0, n)
    b = ri(0, n)
    koleo.journeys(stationToPoint(stations[keys[a]]), stationToPoint(stations[keys[b]]), new Date(new Date()+ri(0, 1000*3600*24))).then(function(data){
        console.log(data.length)
        for(var i=0; i < data.length; i++) {
            for(var j=0; j < data[i].legs.length; j++) {
                c = data[i].legs[j]
                connection = {}
                connection.name = c.line.name
                connection.stations = []
                pref = 0
                for(var k=0; k < c.stopovers.length; k++) {
                    stop = c.stopovers[k]
                    e = {}
                    e.station = stop.station
                    e.arrival = dateToTime(stop.arrival)
                    e.departure = dateToTime(stop.departure)
                    e.dist = stop.distanceFromLast - pref
                    pref = stop.distanceFromLast
                    // console.log(c.stopovers[k])
                    // console.log(e)
                    // console.log(stations[e.station_id])
                    if(k > 0) {
                        f = {}
                        f.station1 = c.stopovers[k-1].station
                        f.station2 = stop.station
                        f.distance = e.dist
                        edges.add(JSON.stringify(f))
                        [f.station1, f.station2] = [f.station2, f.station1]
                        edges.add(JSON.stringify(f))
                    }
                    connection.stations.push(e)
                }
                connections.add(JSON.stringify(connection))
            }
        }
        count++
        if(count == m) {
            saveObjects()
        }
    })
}