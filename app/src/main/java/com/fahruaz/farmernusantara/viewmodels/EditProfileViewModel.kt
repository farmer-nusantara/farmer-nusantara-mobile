package com.fahruaz.farmernusantara.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahruaz.farmernusantara.models.UserModel
import com.fahruaz.farmernusantara.preferences.UserPreferences
import kotlinx.coroutines.launch


class EditProfileViewModel(private val pref: UserPreferences): ViewModel() {

    fun setUser(user: UserModel){
        viewModelScope.launch {
            pref.editUser(user)
        }
    }
}