package com.kazakago.cueue.model

import kotlinx.datetime.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.time.Duration.Companion.days

@Serializable
data class Invitation(
    @SerialName("code")
    val code: InvitationCode,
    @SerialName("workspace")
    val workspace: Workspace,
    @Transient
    @SerialName("created_at")
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = throw NullPointerException(),
) {
    @SerialName("expire_at")
    @Serializable(with = LocalDateTimeSerializer::class)
    val expireAt: LocalDateTime = (createdAt.toInstant(TimeZone.UTC) + 1.days).toLocalDateTime(TimeZone.UTC)

    init {
        verify()
    }

    private fun verify() {
        if (expireAt.toInstant(TimeZone.UTC) < Clock.System.now()) {
            throw NoSuchElementException()
        }
    }
}
