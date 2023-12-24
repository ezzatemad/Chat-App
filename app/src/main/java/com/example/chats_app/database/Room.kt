package com.example.chats_app.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Room(
    var id:String? =null,
    val name:String? =null,
    val desc:String? =null,
    val categoryID:String? =null,
):Parcelable{
    companion object{
        const val COLLECTION_NAME_ROOM = "Room"
    }
}
