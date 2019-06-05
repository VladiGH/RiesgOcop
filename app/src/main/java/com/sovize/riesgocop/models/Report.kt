package com.sovize.riesgocop.models

data class Report(
    val id: String,
    val title: String = "N/A",
    val danger: Long = 0,
    val description: String = "N/A",
    val location: String = "N/A",
    val pictures: List<String> = listOf("N/A")
)