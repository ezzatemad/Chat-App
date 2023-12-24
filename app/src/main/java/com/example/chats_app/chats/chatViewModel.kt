package com.example.chats_app.chats

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chats_app.database.Room
import com.example.chats_app.database.addMessageToDataBase
import com.example.chats_app.database.getMessageFromDataBase
import com.example.chats_app.model.utilesObject
import com.google.firebase.firestore.DocumentChange
import java.util.Date

class chatViewModel: ViewModel() {
    var navigator: navigator? = null
    val messageList = mutableStateOf<List<Message>>(listOf())
    val messageSent = mutableStateOf("")
    var room: Room? =null

    fun addMessageToFireStore()
    {
        if (messageSent.value.isNullOrBlank() || messageSent.value.isNullOrEmpty())
            return
        val message = Message(
            content = messageSent.value,
            data = Date().time,
            senderId = utilesObject.user?.id,
            senderName = utilesObject.user?.fullName,
            roomId = room?.id
        )
        addMessageToDataBase(
            roomId = room?.id!!,
            message = message,
            addOnSuccessListener = {
                                   messageSent.value =""
        }, addOnFailureListener = {
                Log.e("TAG", "addMessageToFireStore: ${it.localizedMessage}")
        })
    }

    fun getMessageFromFireStore(){
        getMessageFromDataBase(roomId = room?.id!!, listener = {snapshots, e ->
            if (e != null) {
                Log.e("Tag", "${e.message}")
                return@getMessageFromDataBase
            }
            val mutableList = mutableListOf<Message>()
            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        mutableList.add(dc.document.toObject(Message::class.java))
                    }
                    else -> {}
                }
            }
            val newList = mutableListOf<Message>()
            newList.addAll(mutableList)
            newList.addAll(messageList.value)

            messageList.value = newList.toList()
        })
    }









    fun navigateToHomeScreen(){
        navigator?.goToHomeScreenFromChat()
    }
}