package com.example.chats_app.splash

import androidx.lifecycle.ViewModel
import com.example.chats_app.database.Users
import com.example.chats_app.database.getUserFromDataBase
import com.example.chats_app.model.utilesObject
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class splashViewModel: ViewModel() {
    var navigator: navigator ?=null
    val auth = Firebase.auth

    fun isCurrentUserLogIn():Boolean{
        return auth.currentUser !=null
    }

    fun returnUserLogIn(){
        if(isCurrentUserLogIn())
        {

            getUserFromDataBase(auth.currentUser?.uid!!,
                onSuccessListener = {
                    utilesObject.user = it.toObject(Users::class.java)
                    utilesObject.firbaseUser= auth.currentUser
                    navigator?.navigateToHome()
                },
                onFailureListener = {
                    navigator?.navigateToLogIn()
                }
            )
        }else{
            navigator?.navigateToLogIn()
        }
    }



}