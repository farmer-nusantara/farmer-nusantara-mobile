package com.fahruaz.farmernusantara

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fahruaz.farmernusantara.preferences.UserPreferences
import com.fahruaz.farmernusantara.viewmodels.RegisterViewModel
import com.fahruaz.farmernusantara.viewmodels.VerificationViewModel

class ViewModelFactory(private val pref: UserPreferences, private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
//            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
//                MainViewModel(pref, Injection.provideRepository(context),
//                    Injection.provideDatabase(context), Injection.provideApiService()) as T
//            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(VerificationViewModel::class.java) -> {
                VerificationViewModel() as T
            }
//            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
//                AddStoryViewModel(pref) as T
//            }
//            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
//                LoginViewModel(pref) as T
//            }
//            modelClass.isAssignableFrom(MapStoryViewModel::class.java) -> {
//                MapStoryViewModel(pref) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}