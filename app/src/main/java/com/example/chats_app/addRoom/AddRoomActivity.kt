package com.example.chats_app.addRoom

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

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
import androidx.compose.ui.window.Dialog
import com.example.chats_app.R
import com.example.chats_app.addRoom.ui.theme.Chats_appTheme
import com.example.chats_app.chats.chatActivity
import com.example.chats_app.database.Room
import com.example.chats_app.home.HomeActivity
import com.example.chats_app.home.homeViewModel


import com.example.chats_app.register.myTextField
import com.example.chats_app.register.signInViewModel


class AddRoomActivity : ComponentActivity(), navigator_room{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Chats_appTheme {
                AddRoomContent(navigator = this)
            }
        }
    }

    override fun navigate() {
        finish()
    }


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoomContent(
    viewModel: addRoomViewModel = viewModel(),
    navigator: navigator_room
) {
    viewModel.navigator = navigator
    Scaffold(

        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = {
                    viewModel.returnToHome()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Icon Back"
                    )
                }
                Text(
                    text = "Chat App",
                    style = TextStyle(color = colorResource(id = R.color.white), fontSize = 20.sp)
                )
                Spacer(modifier = Modifier)
            }

        },
        contentColor = colorResource(id = R.color.white)
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
            Spacer(modifier = Modifier.fillMaxHeight(0.10F))
            myCard(navigator = navigator)
        }

    }

}

@Composable
fun myAlertDialog(
    viewModel: addRoomViewModel = viewModel(),
    navigator: navigator_room
){
    viewModel.navigator = navigator
    if(viewModel.message.value.isNotEmpty()) {
            AlertDialog(
                modifier = Modifier.fillMaxHeight(.50F),
                onDismissRequest = {
                    viewModel.message.value = "" },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.message.value = ""
                            viewModel.returnToHome()
                        }
                    ) {
                        Text(
                            text = "OK",
                            style = TextStyle(fontSize = 18.sp),
                            color = Color.Black
                        )
                    }
                },
                title = {
                    Text(
                        text = "Room Added Successfully",
                        style = TextStyle(fontSize = 20.sp),
                        color = Color.Black
                    )
                },
                text = { Text(text = viewModel.message.value) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.sign_success),
                        contentDescription = "sign success",
                        modifier = Modifier
                            .fillMaxWidth(.40F)
                            .fillMaxHeight(.40F)
                    )
                }
            )
        }
}



@Composable
fun LoadingDialog(viewModel: addRoomViewModel = viewModel()) {
    if (viewModel.showLoading.value)
        Dialog(onDismissRequest = { }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .background(
                        color = colorResource(id = R.color.white),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp),
                    color = colorResource(id = R.color.blue)
                )
            }
        }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myMenu(viewModel: addRoomViewModel = viewModel()) {

    ExposedDropdownMenuBox(
        expanded = viewModel.isExpand.value,
        onExpandedChange = {
            viewModel.isExpand.value = !viewModel.isExpand.value
        },
        modifier = Modifier.padding(horizontal = 15.dp)
    ) {
        OutlinedTextField(
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Black,
                cursorColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Gray
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            value = viewModel.selectedItem.value.name ?: "",
            onValueChange = {},
            label = { Text("Room Category") },
            leadingIcon = {
                Image(
                    painter = painterResource(id = viewModel.selectedItem.value.image!!),
                    contentDescription = "",
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.isExpand.value) },
        )
        ExposedDropdownMenu(
            expanded = viewModel.isExpand.value,
            onDismissRequest = { viewModel.isExpand.value = false }
            )
        {
            viewModel.categoryList.forEach { category ->
                DropdownMenuItem(text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                        ) {
                        Image(
                            painter = painterResource(id = category.image!!),
                            contentDescription = "Room Categeory Image",
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = category.name ?: "")
                    }
                }, onClick = {
                    viewModel.selectedItem.value = category
                    viewModel.isExpand.value = false
                })
            }
        }
    }
}






@Composable
fun myCard(
    viewModel: addRoomViewModel = viewModel(),
    navigator: navigator_room,
    homeViewModel: homeViewModel = viewModel()
           ) {
    Card(
        modifier = Modifier
            .fillMaxWidth(.85F),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(12.dp),
        shape = RoundedCornerShape(18.dp)

    ) {
        Text(text = "Create New Room",
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            style = TextStyle(
                color = colorResource(id = R.color.text_bold),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
                )
            )
        Image(painter = painterResource(id = R.drawable.iv_room),
            contentDescription = "Room image",
            modifier = Modifier
                .width(160.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.FillWidth
            )
        myTextField(label ="Room Name" ,
            state = viewModel.title,
            errorState = viewModel.title_error)
        myMenu()
        myTextField(label ="Room Description" ,
            state = viewModel.desc,
            errorState = viewModel.desc_error)
        myButtonRoom(btn_text = "Create") {
            viewModel.addRoomToFireStore()

        }
    }
    LoadingDialog()
    myAlertDialog(navigator = navigator)
}


@Composable
fun myButtonRoom(btn_text:String,
             onButtonClickListener:() -> Unit
) {
    Button(
        onClick = { onButtonClickListener() },
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.blue)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 50.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        Text(
            text = btn_text,
            style = TextStyle(fontSize = 20.sp)
        )
    }
}






@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview5() {
    Chats_appTheme {
        AddRoomContent(navigator = object :navigator_room {
            override fun navigate() {
            }
        })
    }
}