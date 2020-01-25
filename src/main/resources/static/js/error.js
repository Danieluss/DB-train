class Error {
    constructor(data) {
        this.data = data
    }
    get(id) {
        if(this.data[id] == undefined) {
            this.data[id] = new Error({})
        }
        return this.data[id]
    }
    
}