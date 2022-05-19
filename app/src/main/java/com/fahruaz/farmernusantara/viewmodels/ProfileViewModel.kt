package com.fahruaz.farmernusantara.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahruaz.farmernusantara.preferences.UserPreferences
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref: UserPreferences) : ViewModel()
{
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}