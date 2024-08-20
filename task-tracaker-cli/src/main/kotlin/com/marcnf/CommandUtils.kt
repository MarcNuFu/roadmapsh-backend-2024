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

    fun updateTask(
        args: Array<String>,
        existingTasks: List<Task>,
    ) {
        if (args.size != 3) {
            println("Usage: update <task_id> <new_status>")
        } else {
            val taskToUpdate = existingTasks.find { it.id == args[1].toIntOrNull() }
            val newStatus = TaskStatus.fromValue(args[2])

            if (newStatus == null || taskToUpdate == null) {
                println("Task not found or invalid status.")
            } else {
                val updatedTask = TaskUtils.getUpdateTask(
                    task = taskToUpdate,
                    status = newStatus
                )
                TaskUtils.saveTasks(
                    tasks = existingTasks - taskToUpdate + updatedTask,
                )
            }
        }
    }

    fun deleteTask(
        args: Array<String>,
        existingTasks: List<Task>,
    ) {
        if (args.size != 2) {
            println("Usage: delete <task_id>")
        } else {
            val taskToDelete = existingTasks.find { it.id == args[1].toIntOrNull() }
            if (taskToDelete == null) {
                println("Task not found")
            } else {
                TaskUtils.saveTasks(
                    tasks = existingTasks - taskToDelete,
                )
            }
        }
    }
}
