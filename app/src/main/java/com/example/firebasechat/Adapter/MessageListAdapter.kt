package com.example.firebasechat.Adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasechat.FirebaseHelper.FirebaseHelper
import com.example.firebasechat.Model.ChatModelRetriever
import com.example.firebasechat.Model.UserModel
import com.example.firebasechat.R
import com.example.firebasechat.Utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_list_message_received.view.*
import kotlinx.android.synthetic.main.item_list_message_sent.view.*

class MessageListAdapter
    (var context : Context, var model : MutableList<ChatModelRetriever>,
     val uid : String, userTarget : String) : RecyclerView.Adapter<MessageListAdapter.MViewHolder>(){

        val isMe = 1
        val isNotMe = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageListAdapter.MViewHolder {
        if(viewType == isMe) {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_message_sent, parent, false)
            return MessageListAdapter.MViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_message_received, parent, false)
            return MessageListAdapter.MViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    override fun onBindViewHolder(holder: MessageListAdapter.MViewHolder, position: Int) {
        var chatModel = model[position]
        if(chatModel.uid.equals(uid)){
            holder.bindSent(chatModel, context, uid, "")
        }else{
            holder.bindReceive(chatModel,context,uid, "")
        }
    }

    override fun getItemViewType(position: Int): Int {
        var chatModel = model[position]
        if(chatModel.uid.equals(uid)){
            return isMe
        }else{
            return isNotMe
        }
    }

    class MViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!){
        val userRef = FirebaseDatabase.getInstance().getReference(Constant.USERS)
        fun bindSent(message : ChatModelRetriever, ctx : Context, uid: String, userTarget: String){
            itemView.item_message_sent_message_body.text = message.message
            itemView.item_message_sent_tanggal_post.text = FirebaseHelper.getTimeDate(message.date)
            itemView.setOnLongClickListener{
                ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val myClipData = ClipData.newPlainText("captions", message.message)
                Toast.makeText(ctx,"Text copied", Toast.LENGTH_SHORT).show()
                return@setOnLongClickListener false
            }
        }

        fun bindReceive(message : ChatModelRetriever, ctx : Context, uid: String, userTarget: String){
            itemView.item_message_received_tanggal_post.text = FirebaseHelper.getTimeDate(message.date)
            itemView.item_message_received_message_body.text = message.message
            userRef.child(message.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.exists()){
                        val u = p0.getValue(UserModel::class.java)
                        if(u != null){
                            itemView.item_message_received_display_name.text = u.name
                            Glide.with(ctx).load(u.photoUrl).into(itemView.item_message_received_photo_profile)
                        }
                    }
                }

            })

            itemView.setOnLongClickListener{
                val clipBoardSecurityManager = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val myClipData = ClipData.newPlainText("captions", message.message)
                Toast.makeText(ctx,"Text copied", Toast.LENGTH_SHORT).show()
                return@setOnLongClickListener false
            }
        }
    }
}