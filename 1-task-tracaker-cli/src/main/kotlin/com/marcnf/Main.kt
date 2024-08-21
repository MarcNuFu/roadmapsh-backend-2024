package com.marcnf

fun main(args: Array<String>) {
    val tasks = TaskUtils.loadTasks()

    when (args.getOrNull(0)) {
        Commands.ADD.cliValue -> CommandUtils.addTask(
            args = args,
            existingTasks = tasks,
        )

        Commands.UPDATE.cliValue -> CommandUtils.updateTask(
            args = args,
            existingTasks = tasks,
        )

        Commands.DELETE.cliValue -> CommandUtils.deleteTask(
            args = args,
            existingTasks = tasks,
        )

        Commands.MARK_IN_PROGRESS.cliValue -> CommandUtils.markTaskInProgress(
            args = args,
            existingTasks = tasks,
        )

        Commands.MARK_DONE.cliValue -> CommandUtils.markTaskDone(
            args = args,
            existingTasks = tasks,
        )

        Commands.LIST.cliValue -> CommandUtils.listTasks(
            args = args,
            existingTasks = tasks,
        )

        else -> showUsage()
    }
}

private fun showUsage() {
    println("Usage: <command> [<args>]")
    println("Commands: add, update, delete, mark-in-progress ...")
}
