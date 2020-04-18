package com.example.firebasechat.Model

class ChatModelRetriever(val uid : String, val date : Long, val message : String) {
    constructor() : this ("", 0,"")
}