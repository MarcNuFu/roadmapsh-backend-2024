package com.marcnf

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDateTime

object TaskUtils {
    private const val FIRST_ID = 1
    private const val FILE_PATH = "tasks.json"

    private val json = Json { prettyPrint = true }

    fun generateTask(
        existingTasks: List<Task>,
        description: String
    ): Task {
        val id = existingTasks.maxOfOrNull(Task::id)?.plus(1) ?: FIRST_ID
        val now = LocalDateTime.now().toString()

        return Task(
            id = id,
            description = description,
            TaskStatus.TODO.value,
            createdAt = now,
            updatedAt = now,
        )
    }

    fun saveTasks(
        tasks: List<Task>,
    ) {
        val jsonString = json.encodeToString(tasks)
        File(FILE_PATH).writeText(jsonString)
    }

    fun loadTasks(): List<Task> = with(File(FILE_PATH)) {
        if (!this.exists()) {
            emptyList()
        } else {
            val jsonString = this.readText()
            if (jsonString.isEmpty()) {
                emptyList()
            } else {
                Json.decodeFromString(jsonString)
            }
        }
    }

    fun getUpdateTask(
        task: Task,
        description: String
    ): Task = task.copy(
        description = description,
        updatedAt = LocalDateTime.now().toString(),
    )
}
