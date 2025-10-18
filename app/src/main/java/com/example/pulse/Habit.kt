package com.example.pulse

data class Habit (
    val id : Int,
    var title : String,
    var description : String,
    var isDone : Boolean = false,
    var date : String,
    var type : String
)