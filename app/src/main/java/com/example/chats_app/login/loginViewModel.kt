package com.example.chats_app.login

import android.text.TextUtils
import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chats_app.database.Users
import com.example.chats_app.database.addUserToDataBase
import com.example.chats_app.database.getUserFromDataBase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class loginViewModel: ViewModel() {

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
    var navigator : navigator ?= null

    fun validateField():Boolean{
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



    fun sendAuthDataToFireBase(){
        if(validateField()){
            showLoading.value = true
            signUpNewUsers()
        }
    }

    fun signUpNewUsers(){
        auth
            .signInWithEmailAndPassword(email.value,password.value)
            .addOnCompleteListener {
                showLoading.value = false
                if (it.isSuccessful){
                    //Sign in users
                    isSuccess.value = true
                    getUserFromFireStore(it.result.user?.uid)
                }else{
                    //show error
                    isSuccess.value = false
                    message.value = it.exception?.localizedMessage ?: ""
                }
            }

    }
    fun navigateToSignInScreen()
    {
        navigator?.openSignInScreen()
    }
    fun getUserFromFireStore(uid:String?)
    {
        showLoading.value = true
        getUserFromDataBase(
            uid = uid !!,
            onSuccessListener = {
                val users = it.toObject(Users::class.java)
                // callback
                navigator?.openHomeScreen()
                showLoading.value = false
            },
            onFailureListener ={
                showLoading.value = false
                message.value = it.localizedMessage
            }
        )
    }
}