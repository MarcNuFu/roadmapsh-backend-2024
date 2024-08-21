package com.marcnf

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubActivity(
    val type: String?,
    val payload: GithubActivityPayload,
    val repo: GithubActivityRepo,
)

@Serializable
data class GithubActivityPayload(
    val commits: List<GithubActivityPayloadCommit>?,
    val action: String?,
    @SerialName("ref_type")
    val refType: String?,
)

@Serializable
data class GithubActivityRepo(
    val name: String,
)

@Serializable
data class GithubActivityPayloadCommit(
    val sha: String,
)
