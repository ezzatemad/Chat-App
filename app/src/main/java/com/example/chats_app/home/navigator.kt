package com.example.chats_app.home

import com.example.chats_app.database.Room

interface navigator {

    fun goToAddRoom()

    fun goToChatScreen(room: Room)
}