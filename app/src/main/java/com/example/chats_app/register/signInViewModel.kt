package com.example.chats_app.register

import android.text.TextUtils
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chats_app.database.Users
import com.example.chats_app.database.addUserToDataBase
import com.example.chats_app.login.navigator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class signInViewModel:ViewModel() {

    val fullName = mutableStateOf("")
    val fullNameError = mutableStateOf("")
    val email = mutableStateOf("")
    val emailError = mutableStateOf("")
    val password = mutableStateOf("")
    val passwordError = mutableStateOf("")
    val showLoading = mutableStateOf(false)
    val message = mutableStateOf("")
    val auth = Firebase.auth
    val isSuccess = mutableStateOf(false)
    var navigator :navigator_register ?= null

    fun validateField():Boolean{
        if(fullName.value.isNullOrEmpty() ||fullName.value.isNullOrBlank())
        {
            fullNameError.value = "Enter Valid Name"
            return false
        }
        else
            fullNameError.value = ""

        if(!isEmail(email.value)) {
            emailError.value = "Enter Valid Email !"
        }
        else
            emailError.value = ""
        if(password.value.isNullOrEmpty() || password.value.isNullOrBlank() ||
            password.value.length < 6)
        {
            passwordError.value = "Enter Valid Password"
            return false
        }
        else
            passwordError.value = ""

        return true
    }

    fun isEmail(text: String): Boolean {
        val email: CharSequence = text
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }



    fun sendAuthDataToFireBase():Boolean{
        if(validateField()){
            showLoading.value = true
            signUpNewUsers()
            return true
        }
        return false
    }

    fun signUpNewUsers(){
        auth
            .createUserWithEmailAndPassword(email.value,password.value)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    //Sign in users
                    showLoading.value = false
                    isSuccess.value = true
                    addToFireStore(it.result.user?.uid)
                    navigator?.openHomeScreen()
                }else{
                    //show error
                    showLoading.value = false
                    isSuccess.value = false
                    message.value = it.exception?.localizedMessage ?: ""
                }
            }

    }
    fun addToFireStore(uid:String?)
    {
        showLoading.value = true
        addUserToDataBase(users = Users(id = uid, fullName = fullName.value, email = email.value),
            onSuccessListener = {
                showLoading.value=false
                message.value = "Sign in successful"
            },
            onFailureListener ={
                showLoading.value = false
                message.value = it.localizedMessage
            }
            )
    }
}