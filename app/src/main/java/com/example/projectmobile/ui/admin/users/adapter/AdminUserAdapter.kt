package com.example.projectmobile.ui.admin.users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.api.types.User
import com.example.projectmobile.databinding.RowAdminItemBinding
import com.example.projectmobile.ui.admin.users.viewholder.AdminUserViewHolder

class AdminUserAdapter : RecyclerView.Adapter<AdminUserViewHolder>() {
    private var usersList: List<User> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUserViewHolder {
        val item = RowAdminItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminUserViewHolder(item)
    }

    override fun getItemCount(): Int {
        return usersList.count()
    }

    override fun onBindViewHolder(holder: AdminUserViewHolder, position: Int) {
        holder.bind(usersList[position])
    }

    fun updateUsers(list: List<User>) {
        usersList = list
        notifyDataSetChanged()
    }
}
