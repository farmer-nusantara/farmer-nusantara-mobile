package com.fahruaz.farmernusantara.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    var email: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var password: String? = null,
    var passwordConfirm: String? = null,
    var token: String? = null
): Parcelable