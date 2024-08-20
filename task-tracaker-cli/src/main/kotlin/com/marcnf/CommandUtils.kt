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
        if (args.size < 3) {
            println("Usage: update <task_id> <new_description>")
        } else {
            val taskToUpdate = existingTasks.find { it.id == args[1].toIntOrNull() }
            val description = args.drop(2).joinToString(" ")

            if (taskToUpdate == null) {
                println("Task not found")
            } else {
                val updatedTask = TaskUtils.getUpdatedTask(
                    task = taskToUpdate,
                    description = description
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

    fun markTaskInProgress(
        args: Array<String>,
        existingTasks: List<Task>,
    ) {
        if (args.size != 2) {
            println("Usage: mark-in-progress <task_id>")
        } else {
            val taskToMark = existingTasks.find { it.id == args[1].toIntOrNull() }
            if (taskToMark == null) {
                println("Task not found")
            } else {
                val markedTask = TaskUtils.getMarkedTask(
                    task = taskToMark,
                    status = TaskStatus.IN_PROGRESS,
                )
                TaskUtils.saveTasks(
                    tasks = existingTasks - taskToMark + markedTask,
                )
            }
        }
    }

    fun markTaskDone(
        args: Array<String>,
        existingTasks: List<Task>,
    ) {
        if (args.size != 2) {
            println("Usage: mark-done <task_id>")
        } else {
            val taskToMark = existingTasks.find { it.id == args[1].toIntOrNull() }
            if (taskToMark == null) {
                println("Task not found")
            } else {
                val markedTask = TaskUtils.getMarkedTask(
                    task = taskToMark,
                    status = TaskStatus.DONE,
                )
                TaskUtils.saveTasks(
                    tasks = existingTasks - taskToMark + markedTask,
                )
            }
        }
    }
}
