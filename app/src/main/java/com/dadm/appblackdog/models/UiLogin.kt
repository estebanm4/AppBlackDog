package com.dadm.appblackdog.models

import com.dadm.appblackdog.utils.Constants

data class UiLogin(
    /** campo de texto para el email*/
    var email: String = Constants.EMAIL,
    /** campo de texto para la contraseña*/
    var password: String = Constants.PASS,
    /** checkbox recordarme*/
    var isLoaderEnable: Boolean = false
) {
    fun isNotEmpty(): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }
}