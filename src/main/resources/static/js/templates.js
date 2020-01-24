menu = ["station", "connection", "train", "carriage", "carriagetype", "zone", "discount", "trainuser", "ticket"]

lists = {}
lists["station"] = [["name"]]
// lists["connection"] = [["stations", 0, "station", "name"], "-", ["stations", -1, "station", "name"]]
lists["connection"] = [["id"]]
lists["train"] = [["name"]]
lists["carriage"] = [["id"]]
lists["carriagetype"] = [["id"]]
lists["zone"] = [["name"]]
lists["discount"] = [["name"]]
lists["trainuser"] = [["name"], ["surname"]]
lists["ticket"] = [["uuid"]]

edit = {}
edit["station"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text"},
    {name: "latitude", type: "number", step: "0.0001", min: "-90", max: "90"},
    {name: "longitude", type: "number", step: "0.0001", min: "-180", max: "180"},
    {name: "edges", type: "__list__", 
        arr: [
            {name: "station2", type: "__search__", object: "station", searchBy: "name", return: "id"},
            {name: "distance", type: "number", min: "0"},
            {name: "station1", type: ""}
        ],
        relation: "strong",
    }
]
edit["connection_new"] = [
    {name: "departure", type: "time"},
    {name: "stations", type: "__list__",
        arr: [
            {name: "", type: "__search__", object: "station", searchBy: "name", return: "id"},
        ],
        swapping: true,
    }
]
edit["connection"] = [
    {name: "id", type: "__info__"},
    {}
]
edit["train"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text"},
    {name: "carriages", type: "__list__",
        arr: [
            {name: "", type: "__search__", object: "carriage", searchBy: "id", return: "id"}
        ],
        relation: "weak"
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
            {name: "", type: "__search__", object: "connection", searchBy: "id", return: "id"}
        ]
    }
]
edit["discount"] = [
    {name: "id", type: "__info__"},
    {name: "name", type: "text"},
    {name: "percentOff", type: "number", min: "0", max: "100"}
]
edit["trainuser"] = [
    {name: "id", type: "__info__"},
    {name: "username", type: "text"},
    {name: "email", type: "text"},
    {name: "name", type: "text"},
    {name: "surname", type: "text"}
]
edit["ticket"] = [
    {name: "uuid", type: "__info__"}
]
edit["pathticket"] = [
    {name: "uuid", type: "__info__"},
    {name: ["trainUser", "username"], type: "__info__", comment: "owner"},
    {name: "discount", type: "__search__", object: "discount", searchBy: "name", return: "id"},
    {name: "price", type: "number", min: "0", step: "0.01"},
    {}
]
edit["commutationticket"] = [
    {name: "uuid", type: "__info__"},
    // {name: ["trainUser", "username"], type: "__info__", comment: "owner"},
    {name: "trainUser"},
    {name: "discount", type: "__search__", object: "discount", searchBy: "name", return: "id"},
    {name: "startDate", type: "date"},
    {name: "endDate", type: "date"},
    {name: "type", type: "__search__", object: "commutationtickettype", searchBy: "name", return: "id"}
]