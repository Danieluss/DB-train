menu = [
    ["station", "Stations"],
    ["edge", "Edges"],
    ["connection", "Connections"],
    ["train", "Trains"],
    ["carriage", "Carriages"],
    ["carriagetype", "Carriage types"],
    ["zone", "Zones"],
    ["commutationtickettype", "Ticket types"],
    ["discount", "Discounts"],
    ["trainuser", "Users"],
    ["pathticket", "Path tickets"],
    ["commutationticket", "Commutation tickets"]
]

lists = {}
lists["station"] = [["name"]]
lists["edge"] = [["id"], ["station1", "station", "name"], "-", ["station2", "station", "name"]]
lists["connection"] = [["id"], ["firstStation"], "-", ["lastStation"]]
// lists["connection"] = [["id"]]
lists["train"] = [["name"]]
lists["carriage"] = [["id"]]
lists["carriagetype"] = [["id"]]
lists["zone"] = [["name"]]
lists["discount"] = [["name"]]
lists["trainuser"] = [["name"], ["surname"]]
lists["ticket"] = [["uuid"]]
lists["pathticket"] = [["uuid"]]
lists["commutationticket"] = [["uuid"]]
lists["commutationtickettype"] = [["name"], ["zone"], ["zone", "zone", "name"]]

edit = {}
edit["station"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text"},
    {name: "latitude", type: "number", step: "0.0001", min: "-90", max: "90"},
    {name: "longitude", type: "number", step: "0.0001", min: "-180", max: "180"},
    {name: "edges", type: "__showEdges__"}
    // {name: "edges", type: "__list__", 
    //     arr: [
    //         {name: "station2", type: "__search__", object: "station", searchBy: "name", return: "id"},
    //         {name: "distance", type: "number", min: "0"},
    //         {name: "station1", type: ""}
    //     ],
    //     relation: "strong",
    // }
]
edit["edge"] = [
    {name: "id", type: "__info__"},
    {name: "station1", type: "__search__", object: "station", searchBy: "name", return: "id"},
    {name: "station2", type: "__search__", object: "station", searchBy: "name", return: "id"},
    {name: "distance", type: "number", step: "0.001", min: "0", max: "40000"},
]
edit["connection_new"] = [
    {name: "stations", type: "__list__",
        arr: [
            {name: "Add new", type: "__search__", object: "station", searchBy: "name", return: "id"},
        ],
        swapping: true,
    }
]
edit["connection"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text"},
    {name: "firstDay", type: "date"},
    {name: "lastDay", type: "date"},
    {name: "stations", type: "__connectionlist__",
        arr: [
            {name: "stop", type: "checkbox"},
            {name: "station", type: "__usedSearch__", object: "station", searchBy: "name", return: "id"},
            {name: "arrival_time", type: "time"},
            {name: "departure_time", type: "time"}
        ],
    },
    {name: "trains", type: "__list__", 
        arr: [
            {name: "Add new", type: "__search__", object: "train", searchBy: "name", return: "id"}
        ]
    }
]
edit["train"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text"},
    {name: "carriages", type: "__list__",
        arr: [
            {name: "Add new", type: "__search__", object: "carriage", searchBy: "id", return: "id", null: "train"}
        ],
    }
]
edit["carriage"] = [
    {name: "id", type: "__info__"},
    {name: "type", type: "__search__", object: "carriagetype", searchBy: "id", return: "id"}
]
edit["carriagetype"] = [
    {name: "id", type: "__info__"},
    {name: "cabin", type: "checkbox"},
    {name: "seats", type: "number", min: "0", max: "1000"},
]
edit["zone"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text"},
    {name: "description", type: "text"},
    {name: "connections", type: "__list__",
        arr: [
            {name: "Add new", type: "__search__", object: "connection", searchBy: "id", return: "id"}
        ]
    }
]
edit["commutationtickettype"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text"},
    {name: "price", type: "number", min:"0"},
    {name: "zone", type: "__search__", object: "zone", searchBy: "name", return: "id"}
]
edit["discount"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text"},
    {name: "percentOff", type: "number", min: "0", max: "100"}
]
edit["trainuser"] = [
    {name: "id", type: "__info__"},
    {name: "username", type: "text"},
    {name: "password", type: "password"},
    {name: "role", type: "select", options: ["ROLE_USER", "ROLE_ADMIN"]},
    {name: "email", type: "text"},
    {name: "name", type: "text"},
    {name: "surname", type: "text"}
]
edit["ticket"] = [
    {name: "uuid", type: "__info__"}
]
edit["pathticket_new"] = [
    {name: "fromStation", type: "__search__", object: "station", searchBy: "name", return: "id"},
    {name: "toStation", type: "__search__", object: "station", searchBy: "name", return: "id"},
    {name: "departure", type: "time"},
    {name: "date", type: "date"}
]
edit["pathticket"] = [
    {name: "uuid", type: "__info__"},
    {name: "trainUser", type: "__search__", object: "trainuser", searchBy: "email", return: "id"},
    {name: "discount", type: "__search__", object: "discount", searchBy: "name", return: "id"},
    {name: "price", type: "number", min: "0", step: "0.01"},
    {name: "date", type: "date", readonly: "readonly"},
    {name: "stationConnection1", comment: "from", type: "__recurrentInfo__", arr: ["stationsconnections", "station", "station", "name"]},
    {name: "stationConnection1", comment: "departure", type: "__recurrentInfo__", arr: ["stationsconnections", "departure", function(a){return a.join(':')}]},
    {name: "stationConnection2", comment: "to", type: "__recurrentInfo__", arr: ["stationsconnections", "station", "station", "name"]},
    {name: "stationConnection2", comment: "arrival", type: "__recurrentInfo__", arr: ["stationsconnections", "departure", function(a){return a.join(':')}]}
]
edit["commutationticket"] = [
    {name: "uuid", type: "__info__"},
    {name: "trainUser", type: "__search__", object: "trainuser", searchBy: "email", return: "id"},
    {name: "discount", type: "__search__", object: "discount", searchBy: "name", return: "id"},
    {name: "startDate", type: "date"},
    {name: "endDate", type: "date"},
    {name: "type", type: "__search__", object: "commutationtickettype", searchBy: "id", return: "id"},
    {name: "type", comment: "type", type: "__recurrentInfo__", arr: ["commutationtickettype", "name"]},
    {name: "type", comment: "zone", type: "__recurrentInfo__", arr: ["commutationtickettype", "zone", "zone", "name"]}
]

err_comments = []
err_comments["edge"] = "Make sure that these stations are not connected already."