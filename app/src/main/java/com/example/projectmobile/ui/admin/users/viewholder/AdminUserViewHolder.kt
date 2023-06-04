package com.example.projectmobile.ui.admin.users.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.projectmobile.api.types.User
import com.example.projectmobile.databinding.RowAdminItemBinding
import com.example.projectmobile.ui.admin.users.clicklistener.UserClickListener

class AdminUserViewHolder(private val bind: RowAdminItemBinding, private val userClickListener: UserClickListener): RecyclerView.ViewHolder(bind.root) {
    fun bind(user: User) {
        bind.itemContent.text = user.name

        bind.arrowRight.setOnClickListener {
            userClickListener.onEditCarClick(user)
        }

        bind.trash.setOnClickListener {
            userClickListener.onDeleteCarClick(user)
        }
    }
}
