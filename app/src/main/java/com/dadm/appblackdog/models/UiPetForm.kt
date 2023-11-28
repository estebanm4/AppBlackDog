package com.dadm.appblackdog.models

data class UiPetForm(
    var name: String = "",
    var breedId: String = "",
    var ageRangeId: String = "",
    var weight: String = "",
    var measureUnitId: String = "",
    var birthdateTimeStamp: Long = 0,
    var description: String = "",
    var photoUrl: String = "",

    var breed: String = "",
    var ageRange: String = "",
    var measureUnit: String = "",
    var birthdate: String = "",
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

    fun toPetMap(ownerId: String): HashMap<String, Any> {
        return hashMapOf(
            "ownerId" to ownerId,
            "name" to name,
            "breedId" to breedId,
            "breed" to breed,
            "ageRangeId" to ageRangeId,
            "ageRange" to ageRange,
            "weight" to weight,
            "measureUnitId" to measureUnitId,
            "measureUnit" to measureUnit,
            "birthdate" to birthdateTimeStamp,
            "description" to description,
            "photoUrl" to photoUrl
        )
    }

    fun toPet(ownerId: String):Pet{
        return Pet(
            ownerId = ownerId,
            name = name,
            breedId = breedId,
            breed = breed,
            ageRangeId = ageRangeId,
            ageRange = ageRange,
            weight = weight,
            measureUnitId = measureUnitId,
            measureUnit = measureUnit,
            birthdate = birthdateTimeStamp,
            description = description,
            photoUrl = photoUrl,
        )
    }
}