package com.dadm.appblackdog.objects

data class UiLogin(
    /** campo de texto para el email*/
    var email: String = "",
    /** campo de texto para la contraseña*/
    var password: String = "",
    /** checkbox recordarme*/
    var remember: Boolean = false,
    /** checkbox recordarme*/
    var isLoaderEnable: Boolean = false
) {
    fun isNotEmpty(): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }
}