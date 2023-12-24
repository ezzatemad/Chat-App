package com.example.chats_app.home

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chats_app.database.Room
import com.example.chats_app.database.getRoomFromDataBase


class homeViewModel: ViewModel() {
    var navigator: navigator ?= null
    val roomList = mutableStateOf<List<Room>>(listOf())
    val message = mutableStateOf("")
    var room : Room? = null




    @SuppressLint("SuspiciousIndentation")
    fun getRoomFromFireStore(){
        getRoomFromDataBase(onSuccessListener = {
            val list = mutableListOf<Room>()
            it.documents.forEach {
                list.add(it.toObject(Room::class.java) ?: return@forEach)
            }
            roomList.value = list
        }, onFailureListener = {
            message.value = it.localizedMessage
        })

    }


    fun navigateToAddRoom()
    {
        navigator?.goToAddRoom()
    }

    fun navigateToChatScreen(room: Room)
    {
        navigator?.goToChatScreen(room = room)
    }
}