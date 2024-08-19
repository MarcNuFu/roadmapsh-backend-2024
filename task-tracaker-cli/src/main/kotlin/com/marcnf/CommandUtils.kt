package com.marcnf

object CommandUtils {
    fun addTask(
        args: Array<String>,
        existingTasks: List<Task>,
    ) {
        if (args.size < 2) {
            println("Usage: add <task_description>")
        } else {
            val description = args.drop(1).joinToString(" ")
            val newTask = TaskUtils.generateTask(
                existingTasks = existingTasks,
                description = description
            )
            TaskUtils.saveTasks(
                tasks = existingTasks + newTask,
            )
            println("Task added successfully (ID: ${newTask.id})")
        }
    }
}
