package com.marcnf

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int,
    val description: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
)

enum class TaskStatus(val value: String) {
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    DONE("done")
}
