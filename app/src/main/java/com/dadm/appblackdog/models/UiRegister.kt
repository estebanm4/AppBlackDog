package com.dadm.appblackdog.models

data class UiRegister(
    var email: String = "",
    var password: String = "",
    var validatedPassword: String = "",
    var name: String = "",
    var lastname: String = "",
    var photoUrl: String = "",
    var hasPets: Boolean = false,
    /** loader*/
    var isLoaderEnable: Boolean = false,
    /** error fields */
    var nameError: Boolean = false,
    var lastnameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var validatePasswordError: Boolean = false,
    var pets: MutableList<Pet> = mutableListOf(),
) {

    fun validateForm(): Boolean {
        val isNotEmpty = formIsNotEmpty()
        val validEmail = validateEmail()
        val validPassword = password.length >= 6 && password == validatedPassword
        return isNotEmpty && validEmail && validPassword
    }

    fun validateEmail(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(Regex(emailPattern))
    }

    private fun formIsNotEmpty(): Boolean {
        return name.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && validatedPassword.isNotEmpty()
    }

    fun toOwnerMap(): HashMap<String,Any> {
        return hashMapOf(
            "name" to name,
            "lastname" to lastname,
            "email" to email,
            "hasPets" to pets.isNotEmpty(),
            "photoUrl" to photoUrl
        )
    }
}