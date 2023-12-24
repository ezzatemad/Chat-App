package com.example.chats_app.database

import android.app.DownloadManager.Query
import com.example.chats_app.chats.Message
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


fun getRef(collectionName:String):CollectionReference{
    val db = Firebase.firestore
    return db.collection(collectionName)
}



fun addUserToDataBase(users: Users,onSuccessListener: OnSuccessListener<Void>,onFailureListener: OnFailureListener){

        getRef(Users.COLLECTION_NAME).document(users.id !!)
        .set(users)
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)

}

fun getUserFromDataBase(
    uid: String,
    onSuccessListener: OnSuccessListener<DocumentSnapshot>,
    onFailureListener: OnFailureListener){
    getRef(Users.COLLECTION_NAME)
        .document(uid)
        .get()
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)

}


fun addRoomToDataBase(room: Room,onSuccessListener: OnSuccessListener<Void>,onFailureListener: OnFailureListener){
    val reference = getRef(Room.COLLECTION_NAME_ROOM).document()
    room.id = reference.id
    reference.set(room)
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)
}


fun getRoomFromDataBase(
    onSuccessListener: OnSuccessListener<QuerySnapshot>,
    onFailureListener: OnFailureListener){
        getRef(Room.COLLECTION_NAME_ROOM)
        .get()
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)

}

fun addMessageToDataBase(roomId: String, message: Message,
                         addOnSuccessListener: OnSuccessListener<Void>,
                         addOnFailureListener: OnFailureListener
                         )
{
    //get room collection
    val roomCollection = getRef(Room.COLLECTION_NAME_ROOM)
    //get room document
    val roomDocument = roomCollection.document(roomId)
    // collection from room document
    val messageCollection = roomDocument.collection(Message.COLLECTION_NAME)
    //make document from message collection
    val messageDoc = messageCollection.document()
    message.id = messageDoc.id
    messageDoc
        .set(message)
        .addOnSuccessListener(addOnSuccessListener)
        .addOnFailureListener(addOnFailureListener)
}

fun getMessageFromDataBase(roomId: String, listener: EventListener<QuerySnapshot>)
{
    //get room collection
    val roomCollection = getRef(Room.COLLECTION_NAME_ROOM)
    //get room document
    val roomDocument = roomCollection.document(roomId)
    // collection from room document
    val messageCollection = roomDocument.collection(Message.COLLECTION_NAME)

    messageCollection
        .orderBy("data", com.google.firebase.firestore.Query.Direction.DESCENDING)
        .addSnapshotListener(listener)
}
