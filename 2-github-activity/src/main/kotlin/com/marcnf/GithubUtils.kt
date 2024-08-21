package com.marcnf

import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object GithubUtils {
    private const val NOT_FOUND_CODE = 404
    private const val OK_CODE = 200

    private val client = HttpClient.newBuilder().build()
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    fun getUserActivity(
        username: String,
    ): List<GithubActivity> {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/users/$username/events"))
            .GET()
            .build()

        val response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString(),
        )

        when (response.statusCode()) {
            NOT_FOUND_CODE -> throw NotFoundException()
            OK_CODE -> {}
            else -> throw GithubUnexpectedException()
        }

        return json.decodeFromString(response.body())
    }

    fun printActivity(
        activity: GithubActivity
    ) {
        val repoName = activity.repo.name

        val textToPrint = when (activity.type) {
            "PushEvent" -> "Pushed ${activity.payload.commits?.size ?: 0} commit(s) to $repoName"

            "IssuesEvent" -> "${activity.payload.action?.replaceFirstChar(Char::titlecase)} an issue in $repoName"

            "WatchEvent" -> "Starred $repoName"

            "ForkEvent" -> "Forked $repoName"

            "CreateEvent" -> "Created ${activity.payload.refType} in  $repoName"

            else -> "${activity.type?.replace("Event", "")} in $repoName"
        }

        println(textToPrint)
    }
}
