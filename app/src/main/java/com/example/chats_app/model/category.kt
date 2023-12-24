package com.example.chats_app.model

import com.example.chats_app.R

data class category(
    val id:String? = null,
    val name:String? = null,
    val image:Int? = null

){
    companion object{

        val MUSIC = "music"
        val SPORTS = "sports"
        val MOVIES = "movies"

        fun getCategoryById(id:String?):category{
            return when(id){
                MUSIC -> category(MUSIC,"Music", R.drawable.music)
                SPORTS -> category(SPORTS,"Sports", R.drawable.sports)
                else -> category(MOVIES,"Movies",R.drawable.movies)
            }
        }
        fun getList():List<category>{
            return listOf(getCategoryById(MUSIC),getCategoryById(SPORTS),getCategoryById(MOVIES))
        }
    }
}
