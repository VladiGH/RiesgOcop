package com.sovize.riesgocop.models

data class Report(
    val id: String,
    val title: String = "N/A",
    val danger: Long = 0,
    val description: String = "N/A",
    val location: String = "N/A",
    val pictures: Array<String> = arrayOf("N/A")
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Report

        if (id != other.id) return false
        if (title != other.title) return false
        if (danger != other.danger) return false
        if (description != other.description) return false
        if (location != other.location) return false
        if (!pictures.contentEquals(other.pictures)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + danger.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + pictures.contentHashCode()
        return result
    }
}