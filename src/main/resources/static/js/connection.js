function testConnection() {
    data = {
        stations:[{id:1000}, {id:2}]
    }
    postJson('http://localhost:8080/api/connection/generate', data, function(resp){
        console.log(resp)
    }, function(err) {
        console.log(err)
    })
}