package com.marcnf

fun main(args2: Array<String>) {
    val args = listOf("kamranahmedse")
    if (args.size > 1) {
        showUsage()
    } else {
        val username = args[0]
        try {
            val activities = GithubUtils.getUserActivity(username)
            activities.forEach(GithubUtils::printActivity)
        } catch (_: NotFoundException) {
            println("Username not found")
        } catch (_: GithubUnexpectedException) {
            println("Unexpected error has occurred")
        }
    }
}

private fun showUsage() {
    println("Usage: <username>")
}
