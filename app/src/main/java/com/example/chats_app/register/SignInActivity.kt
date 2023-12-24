package com.example.chats_app.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.chats_app.home.HomeActivity
import com.example.chats_app.R
import com.example.chats_app.login.mySpacer
import com.example.chats_app.ui.theme.Chats_appTheme
import androidx.lifecycle.viewmodel.compose.viewModel
class SignInActivity : ComponentActivity(), navigator_register {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Chats_appTheme {
                signIn(navigatorRegister = this)
            }
        }
    }

    override fun openHomeScreen() {
        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun signIn(viewModel: signInViewModel = viewModel(), navigatorRegister: navigator_register) {
    viewModel.navigator = navigatorRegister
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sign In",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                },
                colors = TopAppBarDefaults.smallTopAppBarColors(Color.Transparent),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth()

            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.defualt_bg),
                    contentScale = ContentScale.FillBounds
                )
        ) {
            Spacer(Modifier.fillMaxHeight(.33F))
            myTextField(label = "Full Name", state = viewModel.fullName,viewModel.fullNameError)
            myTextField(label = "Email", state = viewModel.email,viewModel.emailError)
            myTextField(label = "Password", state = viewModel.password,viewModel.passwordError, isPassword = true)
            mySpacer(number = .3F)
            myButton(btn_text = "Create Account") {
                viewModel.isSuccess.value = true
                // make here click on button
                viewModel.sendAuthDataToFireBase()

            }
            dialog()
            myAlertDialog()
        }
    }
}

@Composable
fun myAlertDialog(viewModel: signInViewModel = viewModel()) {

    if(viewModel.message.value.isNotEmpty())
    {
        if (viewModel.isSuccess.value)
        {

        }
        else{
            AlertDialog(
                modifier =Modifier.fillMaxHeight(.50F),
                onDismissRequest = { viewModel.message.value = "" },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.message.value = "" }
                    ) {
                        Text(text = "Try Again",
                            style = TextStyle(fontSize = 18.sp),
                            color = Color.Black
                        )
                    }
                },
                title = { Text(text ="Sign In Failed ",
                    style = TextStyle(fontSize = 20.sp),
                    color = Color.Black
                )},
                text = { Text(text = viewModel.message.value)},
                icon = { Icon(painter = painterResource(id = R.drawable.sign_failed),
                    contentDescription = "sign_failed",
                    modifier = Modifier
                        .fillMaxWidth(.40F)
                        .fillMaxHeight(.40F)
                )}
            )
        }
        }

    }























@Composable
fun dialog(viewModel :signInViewModel = viewModel()) {
    if(viewModel.showLoading.value)
    {
        Dialog(onDismissRequest = { }
        ) {

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(80.dp),
                    color = colorResource(id = R.color.blue)
                )
        }
    }
}



@Composable
fun myButton(btn_text:String,
             onButtonClickListener:() -> Unit
             ) {
    Button(
        onClick = { onButtonClickListener() },
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.blue)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        contentPadding = PaddingValues(20.dp)
    ) {
            Text(
                text = btn_text,
            )

            Image(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "ic_arrow_right",
                modifier = Modifier.padding(start = 100.dp)
            )
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myTextField(label:String,
                state: MutableState<String>,
                errorState:MutableState<String>,
                isPassword:Boolean = false
                ) {
    OutlinedTextField(
        keyboardOptions = if(isPassword) KeyboardOptions
            (keyboardType = KeyboardType.Password) else KeyboardOptions(),
        value =state.value ,
        onValueChange = {newValue ->
            state.value = newValue
        },
        label = { Text(text = label,
             style = TextStyle(fontSize = 15.sp))},
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Black,
            cursorColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Gray
        ),
        visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        isError = errorState.value.isNotEmpty(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp)
    )
    if(errorState.value.isNotEmpty())
    {
        Text(text = errorState.value,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 15.dp),
            style = TextStyle(Color.Red)
            )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    Chats_appTheme {
        myAlertDialog()
    }
}


//
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview2() {
//    Chats_appTheme {
//        signIn()
//    }
//}