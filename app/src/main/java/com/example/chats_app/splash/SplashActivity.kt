package com.example.chats_app.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.chats_app.R
import com.example.chats_app.home.HomeActivity
import com.example.chats_app.login.LoginActivity
import com.example.chats_app.ui.theme.Chats_appTheme

class SplashActivity : ComponentActivity(), navigator{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Chats_appTheme {
                SplashScreen(navigator= this@SplashActivity)
            }

        }
    }

    override fun navigateToHome() {
        val intent = Intent(this@SplashActivity , HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun navigateToLogIn() {
        val intent = Intent(this@SplashActivity , LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
@Composable
fun SplashScreen(viewModel: splashViewModel= viewModel(), navigator: navigator)
{
    viewModel.navigator = navigator
    viewModel.returnUserLogIn()
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val(logo,description) =createRefs()
        Image(painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxSize(.4F)
                .constrainAs(logo) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Chats_appTheme {
        SplashScreen(navigator= object :navigator{
            override fun navigateToHome() {

            }

            override fun navigateToLogIn() {
            }

        })
    }
}