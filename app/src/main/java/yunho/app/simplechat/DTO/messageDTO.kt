package yunho.app.simplechat.DTO

data class messageDTO(
    val senderID: String,
    val message: String
){
    constructor() : this("","")
}
