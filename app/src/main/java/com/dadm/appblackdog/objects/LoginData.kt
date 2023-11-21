package com.dadm.appblackdog.objects

data class LoginData(
    var email: String = "",
    var pass: String = "",
    var remember: Boolean = false
) {
    fun isNotEmpty(): Boolean {
        return email.isNotEmpty() && pass.isNotEmpty()
    }
}