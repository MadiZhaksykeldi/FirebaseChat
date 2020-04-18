package com.example.firebasechat.Model

import com.example.firebasechat.Utils.Constant

data class UserModel (val name : String, val email : String, val uid : String, val photoUrl : String){

    constructor() : this("undefined", "undefined", "undefined", Constant.PHOTO)
}