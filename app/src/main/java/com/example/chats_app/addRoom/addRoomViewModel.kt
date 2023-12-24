package com.example.chats_app.addRoom

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chats_app.database.Room
import com.example.chats_app.database.Users
import com.example.chats_app.database.addRoomToDataBase
import com.example.chats_app.database.addUserToDataBase
import com.example.chats_app.model.category

class addRoomViewModel: ViewModel() {
    val showLoading = mutableStateOf(false)
    val message = mutableStateOf("")
    val title = mutableStateOf("")
    val title_error = mutableStateOf("")
    val desc = mutableStateOf("")
    val desc_error = mutableStateOf("")
    val categoryList = category.getList()
    var selectedItem = mutableStateOf(categoryList[0])
    var isExpand = mutableStateOf(false)

    var navigator :navigator_room? =null

    fun validateField():Boolean{
        if(title.value.isNullOrEmpty() ||title.value.isNullOrBlank())
        {
            title_error.value = "Enter Valid Title"
            return false
        }
        else
            title_error.value = ""
        if(desc.value.isNullOrEmpty() || desc.value.isNullOrBlank())
        {
            desc_error.value = "Enter Valid Description"
            return false
        }
        else
            desc_error.value = ""
        return true
    }

    fun addRoomToFireStore(){
        if(validateField())
        {
            showLoading.value = true
            addRoomToDataBase(Room(id = null,name = title.value, desc = desc.value,
                categoryID = selectedItem.value.id?: category.MOVIES),
                onSuccessListener = {
                    showLoading.value=false
                    message.value = "Room Added successfully"
                },
                onFailureListener ={
                    showLoading.value = false
                    message.value = it.localizedMessage
                }
            )
        }
    }


    fun returnToHome()
    {
        navigator?.navigate()
    }

}