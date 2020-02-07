package com.example.apcs_practice.models

data class History(
    var id: String = "",
    var date: String = "",
    var answer: String = "",
    var title: String = "",
    var session: Int = 0,
    var correctRate: String = "0/25"
)