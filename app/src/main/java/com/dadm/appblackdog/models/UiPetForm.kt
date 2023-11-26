package com.dadm.appblackdog.models

data class UiPetForm(
    var name: String = "",
    var description: String = "",
    var breed: String = "",
    var ageRange: String = "",
    var weight: String = "",
    var measureUnit: String = "",
    var birthdate: String = "",
    var birthdateTimeStamp: Long = 0,
    var photoUrl: String = "",
    var ageRangeId: String = "",
    var measureUnitId: String = "",
    var breedId: String = "",
    var measureUnitList: MutableList<String> = mutableListOf(""),
    var breedList: MutableList<String> = mutableListOf(""),
    var ageRangeList: MutableList<String> = mutableListOf(""),

    var isLoader: Boolean = false,
    var nameError: Boolean = false,
    var weightError: Boolean = false,
    var measureUnitError: Boolean = false,
    var birthdateError: Boolean = false,
    var ageRangeError: Boolean = false,
    var breedError: Boolean = false,
) {

    fun validateForm(): Boolean {
        return name.isNotEmpty() && breedId.isNotEmpty() && ageRangeId.isNotEmpty() &&
                weight.isNotEmpty() && measureUnitId.isNotEmpty() && birthdateTimeStamp != 0L && birthdate.isNotEmpty()
    }
}