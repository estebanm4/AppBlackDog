package com.dadm.appblackdog.models

data class UiPetProfile(
    var name: String = "",
    var description: String = "",
    var breed: String = "",
    var ageRange: String = "",
    var birthday: String = "",
    var weight: String = "",
    var measureUnit: String = "",
    var photoUrl: String = "",
    var loader: Boolean = false,
    var loadInfo: Boolean = true,
    var namesList: List<String> = arrayListOf(""),
) {
}