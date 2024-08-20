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

        else -> showUsage()
    }
}

private fun showUsage() {
    println("Usage: <command> [<args>]")
    println("Commands: add, update, ...")
}
