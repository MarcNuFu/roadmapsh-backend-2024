package com.marcnf

enum class Commands(val cliValue: String) {
    ADD("add"),
    UPDATE("update"),
    DELETE("delete"),
    MARK_IN_PROGRESS("mark-in-progress"),
    MARK_DONE("mark-done"),
    LIST("list"),
}
