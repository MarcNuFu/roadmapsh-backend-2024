package com.marcnf

fun main(args: Array<String>) {
    if (args.size > 1) {
        showUsage()
    } else {
        val username = args[0]
        val activity = GithubUtils.getUserActivity(username)
        println(activity)
    }
}

private fun showUsage() {
    println("Usage: <username>")
}
