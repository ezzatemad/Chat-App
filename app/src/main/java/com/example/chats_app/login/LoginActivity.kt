package com.example.chats_app.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.chats_app.R
import com.example.chats_app.login.ui.theme.Chats_appTheme
import com.example.chats_app.register.dialog
import com.example.chats_app.register.myButton
import com.example.chats_app.register.myTextField
import com.example.chats_app.home.HomeActivity
import com.example.chats_app.register.SignInActivity
import com.example.chats_app.register.signInViewModel

class LoginActivity : ComponentActivity(), navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Chats_appTheme {
                login(navigator = this)
            }
        }
    }

    override fun openSignInScreen() {
        val intent =Intent(this@LoginActivity, SignInActivity::class.java)
        startActivity(intent)

    }

    override fun openHomeScreen() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun login(viewModel: loginViewModel = viewModel(),navigator: navigator) {
    viewModel.navigator = navigator
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Login",
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
            Text(
                text = "Welcome Back!",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black),
                modifier = Modifier.padding(horizontal = 15.dp)
                )
            mySpacer(number = .03F)
            myTextField(label = "Email", state = viewModel.email,viewModel.emailError)
            myTextField(label = "Password", state = viewModel.password,viewModel.passwordError, isPassword = true)
            mySpacer(number = .02F)
            Text(
                text = "Forgot Password?",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Thin,
                    color = colorResource(id = R.color.text_light)
                ),
                modifier = Modifier.padding(horizontal = 15.dp)
            )
            mySpacer(number = .1F)
            myButton(btn_text = "Login") {
                viewModel.isSuccess.value = true
                // make here click on button
                viewModel.sendAuthDataToFireBase()

            }
            TextButton(
                onClick = {
                    viewModel.navigateToSignInScreen()
                },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 8.dp)
            )
            {
                Text(
                    text = "Or Create Account",
                    color = colorResource(id = R.color.text_light),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

            }
            dialogLogin()
            myAlertDialogLogin()
        }
    }
}

@Composable
fun dialogLogin(viewModel : loginViewModel = viewModel()) {
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
fun myAlertDialogLogin(viewModel: loginViewModel = viewModel()) {

    if(viewModel.message.value.isNotEmpty())
    {
        if (viewModel.isSuccess.value)
        {
            AlertDialog(
                modifier =Modifier.fillMaxHeight(.50F),
                onDismissRequest = { viewModel.message.value = "" },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.message.value = "" }
                    ) {
                        Text(text = "OK",
                            style = TextStyle(fontSize = 18.sp),
                            color = Color.Black
                        )}
                },
                title = {
                    Text(text = "Login Success ",
                        style = TextStyle(fontSize = 20.sp ),
                        color = Color.Black) },
                text = { Text(text = viewModel.message.value)},
                icon = { Icon(painter = painterResource(id = R.drawable.sign_success),
                    contentDescription = "sign_success",
                    modifier = Modifier
                        .fillMaxWidth(.40F)
                        .fillMaxHeight(.40F)
                )
                }
            )
        }else{
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
                title = { Text(text ="Login Failed ",
                    style = TextStyle(fontSize = 20.sp),
                    color = Color.Black
                )},
                text = { Text(text = viewModel.message.value)},
                icon = { Icon(painter = painterResource(id = R.drawable.sign_failed),
                    contentDescription = "sign_failed",
                    modifier = Modifier
                        .fillMaxWidth(.40F)
                        .fillMaxHeight(.40F)
                )
                }
            )
        }
    }
}




@Composable
fun mySpacer(number: Float) {
    Spacer(modifier = Modifier
        .fillMaxHeight(number)
    )

}




@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Chats_appTheme {
        login(navigator = object :navigator{
            override fun openSignInScreen() {

            }

            override fun openHomeScreen() {
            }

        })
    }
}