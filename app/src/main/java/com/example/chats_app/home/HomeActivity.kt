package com.example.chats_app.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.chats_app.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chats_app.addRoom.AddRoomActivity
import com.example.chats_app.chats.chatActivity
import com.example.chats_app.database.Room

import com.example.chats_app.home.ui.theme.Chats_appTheme
import com.example.chats_app.model.category


class HomeActivity : ComponentActivity(), navigator{
    lateinit var room: Room
    private val viewModel: homeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            room = intent.getParcelableExtra("roomExtra",Room::class.java)!!
        }
        setContent {
            Chats_appTheme {
                homeContent(navigator= this)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        viewModel.getRoomFromFireStore()
    }
    override fun goToAddRoom() {
        val intent = Intent(this@HomeActivity, AddRoomActivity::class.java)
        startActivity(intent)
    }

    override fun goToChatScreen(room: Room) {
        val intent = Intent(this@HomeActivity,chatActivity::class.java)
        intent.putExtra("room_name",room)
        startActivity(intent)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun homeContent(viewModel: homeViewModel = viewModel(), navigator: navigator) {
    val intent = Intent()
    viewModel.navigator = navigator
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chat App",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(Color.Transparent),
                modifier = Modifier

                    .fillMaxWidth()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                          viewModel.navigateToAddRoom()
                },
                containerColor = colorResource(id = R.color.blue),
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription ="icon add"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.defualt_bg),
                    contentScale = ContentScale.FillBounds
                )
                .padding(top = it.calculateTopPadding())
        ) {
            roomList(navigator = navigator)

        }
    }
}

@Composable
fun roomList(viewModel: homeViewModel = viewModel() , navigator: navigator) {
    viewModel.getRoomFromFireStore()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)){
            items(viewModel.roomList.value.size){
                roomChatCard(room = viewModel.roomList.value[it], navigator = navigator)
            }
        }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun roomChatCard(
    viewModel: homeViewModel = viewModel(),
    room: Room,
    navigator: navigator
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White ),
        onClick = {
                 viewModel.navigateToChatScreen(room = room)
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(210.dp)
            .width(160.dp)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        ) {
            Image(
                painter = painterResource(
                    id = category.getCategoryById( room.categoryID ?: category.SPORTS).image ?: R.drawable.sports),
                contentDescription = "category image",
                modifier = Modifier
                    .padding(top = 18.dp)
                    .align(Alignment.CenterHorizontally)
                    .width(120.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = room.name ?:"",
                style = TextStyle(
                    color = colorResource(id = R.color.text_card),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview4() {
    Chats_appTheme {
        homeContent(navigator = object :navigator{
            override fun goToAddRoom() {
            }

            override fun goToChatScreen(room: Room) {

            }

        })
    }
}