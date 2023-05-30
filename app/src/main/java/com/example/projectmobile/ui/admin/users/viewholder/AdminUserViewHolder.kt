package com.example.projectmobile.ui.admin.users.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.R
import com.example.projectmobile.api.types.User
import com.example.projectmobile.databinding.RowAdminItemBinding

class AdminUserViewHolder(private val bind: RowAdminItemBinding): RecyclerView.ViewHolder(bind.root) {
    fun bind(user: User) {
        bind.itemContent.text = user.name
    }
}
