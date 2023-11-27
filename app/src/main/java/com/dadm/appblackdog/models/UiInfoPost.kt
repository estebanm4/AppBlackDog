package com.dadm.appblackdog.models

data class UiInfoPost(
    var items: List<InfoPost> = listOf(),
    var loader: Boolean = false,
)