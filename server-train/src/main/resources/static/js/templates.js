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
    {name: "name", type: "text", title: "Name of the station"},
    {name: "latitude", type: "number", step: "0.0001", min: "-90", max: "90", title: "Latitude of the stations center point"},
    {name: "longitude", type: "number", step: "0.0001", min: "-180", max: "180", title: "Longitude of the stations center point"},
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
    {name: "station1", type: "__search__", object: "station", searchBy: "name", return: "id", title: "Station from"},
    {name: "station2", type: "__search__", object: "station", searchBy: "name", return: "id", title: "Station to"},
    {name: "distance", type: "number", step: "0.001", min: "0", max: "40000", title: "Distance in kilometers between stations"},
]
edit["connection_new"] = [
    {name: "stations", type: "__list__",
        arr: [
            {name: "Add new", type: "__search__", object: "station", searchBy: "name", return: "id", title: "Next station to be connected"},
        ],
        swapping: true,
    }
]
edit["connection"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text", title: "Name of the connection"},
    {name: "firstDay", type: "date", title: "First day when connection will be available"},
    {name: "lastDay", type: "date", title: "Last day when connection will be available"},
    {name: "stations", type: "__connectionlist__",
        arr: [
            {name: "stop", type: "checkbox", title: "Do the trains stop there?"},
            {name: "station", type: "__usedSearch__", object: "station", searchBy: "name", return: "id"},
            {name: "arrival_time", type: "time", title: "Arrival time"},
            {name: "departure_time", type: "time", title: "Departure time"}
        ],
    },
    {name: "trains", type: "__list__", 
        arr: [
            {name: "Add new", type: "__search__", object: "train", searchBy: "name", return: "id", title: "Train to be assigned to this connection"}
        ]
    }
]
edit["train"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text", title: "Name of the train"},
    {name: "carriages", type: "__list__",
        arr: [
            {name: "Add new", type: "__search__", object: "carriage", searchBy: "id", return: "id", null: "train", title: "Carriage to be assigned to this train"}
        ],
    }
]
edit["carriage"] = [
    {name: "id", type: "__info__"},
    {name: "type", type: "__search__", object: "carriagetype", searchBy: "id", return: "id", title: "Type of the carriage"}
]
edit["carriagetype"] = [
    {name: "id", type: "__info__"},
    {name: "cabin", type: "checkbox", title: "Is it cabin?"},
    {name: "seats", type: "number", min: "0", max: "1000", title: "Number of seats"},
]
edit["zone"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text", title: "Name of the zone"},
    {name: "description", type: "text", title: "Optional description of the zone"},
    {name: "connections", type: "__list__",
        arr: [
            {name: "Add new", type: "__search__", object: "connection", searchBy: "id", return: "id", title: "Connections going through the zone"}
        ]
    }
]
edit["commutationtickettype"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text", title: "Name of the ticket type"},
    {name: "price", type: "number", min:"0", title: "Price"},
    {name: "zone", type: "__search__", object: "zone", searchBy: "name", return: "id", title: "Permitted zone"}
]
edit["discount"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text", title: "Name of the discount"},
    {name: "percentOff", type: "number", min: "0", max: "100", title: "Percent off"}
]
edit["trainuser"] = [
    {name: "id", type: "__info__"},
    {name: "username", type: "text", title: "Unique username"},
    {name: "password", type: "password", title: "Password"},
    {name: "role", type: "select", options: ["ROLE_USER", "ROLE_ADMIN"]},
    {name: "email", type: "text", title: "Unique email"},
    {name: "name", type: "text", title: "Name of the user"},
    {name: "surname", type: "text", title: "Surname of the user"}
]
edit["ticket"] = [
    {name: "uuid", type: "__info__"}
]
edit["pathticket_new"] = [
    {name: "fromStation", type: "__search__", object: "station", searchBy: "name", return: "id", title: "Find connection from this station"},
    {name: "toStation", type: "__search__", object: "station", searchBy: "name", return: "id", title: "Find connection to this station"},
    {name: "departure", type: "time", title: "Departure time"},
    {name: "date", type: "date", title: "Date"}
]
edit["pathticket"] = [
    {name: "uuid", type: "__info__"},
    {name: "trainUser", type: "__search__", object: "trainuser", searchBy: "email", return: "id", title: "Train User"},
    {name: "discount", type: "__search__", object: "discount", searchBy: "name", return: "id", title: "Discount"},
    {name: "price", type: "number", min: "0", step: "0.01", title: "Price"},
    {name: "date", type: "date", readonly: "readonly", title: "Date"},
    {name: "stationConnection1", comment: "from", type: "__recurrentInfo__", arr: ["stationsconnections", "station", "station", "name"]},
    {name: "stationConnection1", comment: "departure", type: "__recurrentInfo__", arr: ["stationsconnections", "departure", function(a){return a.join(':')}]},
    {name: "stationConnection2", comment: "to", type: "__recurrentInfo__", arr: ["stationsconnections", "station", "station", "name"]},
    {name: "stationConnection2", comment: "arrival", type: "__recurrentInfo__", arr: ["stationsconnections", "departure", function(a){return a.join(':')}]}
]
edit["commutationticket"] = [
    {name: "uuid", type: "__info__"},
    {name: "trainUser", type: "__search__", object: "trainuser", searchBy: "email", return: "id", title: "Owner of the ticket"},
    {name: "discount", type: "__search__", object: "discount", searchBy: "name", return: "id", title: "Discount"},
    {name: "startDate", type: "date", title: "Ticket will be valid from this date"},
    {name: "endDate", type: "date", title: "Expiration date"},
    {name: "type", type: "__search__", object: "commutationtickettype", searchBy: "id", return: "id"},
    {name: "type", comment: "type", type: "__recurrentInfo__", arr: ["commutationtickettype", "name"]},
    {name: "type", comment: "zone", type: "__recurrentInfo__", arr: ["commutationtickettype", "zone", "zone", "name"]}
]

err_comments = []
err_comments["edge"] = "Make sure that these stations are not connected already."
