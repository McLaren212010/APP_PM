package com.example.app_pm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_pm.R
import com.example.app_pm.api.User

class UserAdapter(val users: List<User>): RecyclerView.Adapter<UsersViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent,)
        return UsersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        return holder.bind(users[position])
    }
}

class UsersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val name: TextView = itemView.findViewById(R.id.name)
    private val email: TextView = itemView.findViewById(R.id.email)
    private val city:TextView = itemView.findViewById(R.id.city)

    fun bind (user: User){
        name.text = user.name
        city.text = user.address.city
        email.text= email.text
    }



}
