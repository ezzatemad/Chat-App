package com.example.chats_app.database

data class Users(
    val id:String ?=null,
    val fullName:String ?=null,
    val email:String ?=null,
){
    companion object{
        const val COLLECTION_NAME = "Users"
    }
}
