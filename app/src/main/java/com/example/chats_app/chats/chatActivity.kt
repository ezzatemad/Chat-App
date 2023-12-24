package com.example.chats_app.chats

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chats_app.R
import com.example.chats_app.chats.ui.theme.Chats_appTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chats_app.database.Room
import com.example.chats_app.login.mySpacer
import com.example.chats_app.model.utilesObject
import java.text.SimpleDateFormat
import java.util.Date

class chatActivity : ComponentActivity(), navigator {
    private lateinit var roomIntent:Room
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra("room_name", Room::class.java)!!
        else
            intent.getParcelableExtra("room_name")!!

        setContent {
            Chats_appTheme {
                chatContent(room_intent = roomIntent,navigator = this)

            }
        }
    }

    override fun goToHomeScreenFromChat() {
//        val intent = Intent(this@chatActivity,HomeActivity::class.java)
//        startActivity(intent)
        finish()
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun chatContent(viewModel: chatViewModel = viewModel(), navigator: navigator, room_intent: Room) {
    viewModel.navigator = navigator
    viewModel.room = room_intent
    viewModel.getMessageFromFireStore()
    Scaffold(

        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = {
                    viewModel.navigateToHomeScreen() },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Icon Back"
                    )
                }
                Text(
                    text = room_intent.name?:"" ,
                    style = TextStyle(
                        color = colorResource(id = R.color.white),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                        )
                )
                Spacer(modifier = Modifier.width(30.dp))
            }
        },
        contentColor = colorResource(id = R.color.white),
        bottomBar = {
            myChatBottomBar()
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.defualt_bg),
                    contentScale = ContentScale.FillBounds
                )
                .padding(top = it.calculateTopPadding())
        ) {
            myChatCard()

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myChatBottomBar(viewModel: chatViewModel = viewModel()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = viewModel.messageSent.value,
            onValueChange = {
                viewModel.messageSent.value = it
            },
            label = {
                Text(text = "Type Message")
            },
            shape = RoundedCornerShape(
                topStart = 0.dp,
                bottomStart = 0.dp,
                topEnd = 14.dp,
                bottomEnd = 0.dp
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedLabelColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            modifier = Modifier.weight(1.20F)
        )
        mySpacerChat(number = .02F)
        Button(
            onClick = {
                viewModel.addMessageToFireStore()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.blue)
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.weight(.50F)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Send",
                    modifier = Modifier.padding(end= 3.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "icon send"
                )
            }
        }
    }
}

@Composable
fun mySpacerChat(number: Float) {
    Spacer(modifier = Modifier
        .fillMaxWidth(number)
    )

}

@Composable
fun myChatCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.88F)
            .padding(horizontal = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        chatLazyColumn()
    }

}

@Composable
fun chatLazyColumn(viewModel: chatViewModel = viewModel()) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        reverseLayout = true,
        content = {
        items(viewModel.messageList.value.size) {
            val item = viewModel.messageList.value[it]
            if(item.senderId == utilesObject.user?.id)
            {
                sentMessageRowCard(message = item)
            }else{
                receivedMessageRowCard(message = item)
            }
        }
    })

}

@SuppressLint("SuspiciousIndentation")
@Composable
fun sentMessageRowCard(message: Message) {
    val date = Date(message.data ?: 0)
    val simpleTimeFormat = SimpleDateFormat("hh:mm a")
    val dateString = simpleTimeFormat.format(date)
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Text(
                text = dateString,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.Bottom),
                style = TextStyle(color = colorResource(id = R.color.time_color))
            )
            Text(
                text = message.content ?: "",
                modifier = Modifier
                    .background(
                        color = colorResource(id = R.color.blue),
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            bottomStart = 14.dp,
                            bottomEnd = 0.dp,
                            topEnd = 14.dp
                        )
                    )
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                style = TextStyle(color = Color.White, fontSize = 17.sp)
            )
        }
}

@Composable
fun receivedMessageRowCard(message: Message) {
    val date = Date(message.data ?: 0)
    val simpleTimeFormat = SimpleDateFormat("hh:mm a")
    val dateString = simpleTimeFormat.format(date)
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {

        Text(
            text = message.senderName ?: "",
            style = TextStyle(color = colorResource(id = R.color.black))
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Text(
                text = message.content ?: "",
                modifier = Modifier
                    .background(
                        color = colorResource(id = R.color.received_card),
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 14.dp,
                            topEnd = 14.dp
                        )
                    )
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                style = TextStyle(color = Color.Black, fontSize = 17.sp)
            )
            Text(
                text = dateString,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .align(Alignment.Bottom),
                style = TextStyle(color = colorResource(id = R.color.time_color))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    Chats_appTheme {
        chatContent(room_intent = Room(name = ""), navigator = object :navigator{
            override fun goToHomeScreenFromChat() {

            }
        })
    }
}