package com.dadm.appblackdog.models

data class UiReminder(
    var items: List<Reminder> = listOf(),
    var calendarItems: List<DateItem> = listOf(
        DateItem(selected = false, day = "Vie", value = "24"),
        DateItem(selected = false, day = "Sab", value = "25"),
        DateItem(selected = false, day = "Dom", value = "26"),
        DateItem(selected = true, day = "Lun", value = "27"),
        DateItem(selected = false, day = "Mar", value = "28"),
        DateItem(selected = false, day = "Mie", value = "29"),
        DateItem(selected = false, day = "Jue", value = "30"),
    ),
    var loader: Boolean = false,
) {
    data class DateItem(
        var selected: Boolean = false,
        var day: String = "",
        var value: String = "",
    )
}