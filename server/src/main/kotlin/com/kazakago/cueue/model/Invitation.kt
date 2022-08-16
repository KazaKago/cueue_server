package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.LocalDateTime

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
    val expireAt: LocalDateTime = createdAt.plusDays(1)

    init {
        verify()
    }

    private fun verify() {
        if (expireAt < LocalDateTime.now()) {
            throw NoSuchElementException()
        }
    }
}
