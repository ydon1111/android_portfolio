package fastcampus.aop.part3.chattingapp

data class Message(
    var message: String?,
    var sendId: String?
){
    constructor():this("","")
}
