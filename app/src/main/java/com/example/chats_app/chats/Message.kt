package com.example.chats_app.chats

data class Message(
    var id: String? = null,
    val content: String? = null,
    val data: Long? = null,
    val senderId: String? =null,
    val senderName: String? = null,
    val roomId: String? = null

){
    companion object{
        val COLLECTION_NAME = "Message"
    }
}
