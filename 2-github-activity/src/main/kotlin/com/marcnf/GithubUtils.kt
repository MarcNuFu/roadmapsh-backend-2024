package com.marcnf

import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object GithubUtils {
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

        return json.decodeFromString(response.body())
    }
}
