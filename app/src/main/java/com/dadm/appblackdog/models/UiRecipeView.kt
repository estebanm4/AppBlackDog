package com.dadm.appblackdog.models

data class UiRecipeView(
    var name: String = "",
    var description: String = "",
    var items: List<String> = listOf(),
    var imageUrl: String = "",
    var loader: Boolean = false,
) {
}