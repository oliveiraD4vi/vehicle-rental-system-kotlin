package com.example.projectmobile.ui.admin.users

import com.example.projectmobile.api.types.User

interface UserClickListener {
    fun onEditCarClick(user: User)
    fun onDeleteCarClick(user: User)
}
