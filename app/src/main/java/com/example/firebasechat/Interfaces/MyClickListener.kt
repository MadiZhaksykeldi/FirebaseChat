package com.example.firebasechat.Interfaces

import android.view.View

interface MyClickListener {
    abstract fun onClick(v : View, position : Int, isLongCLick : Boolean)
}