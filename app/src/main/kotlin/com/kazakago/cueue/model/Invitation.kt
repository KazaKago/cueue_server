package com.kazakago.cueue.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Invitation(
    @SerialName("code")
    val code: InvitationCode,
    @SerialName("workspace")
    val workspace: Workspace,
    @SerialName("created_at")
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime
) {
    init {
        verify()
    }

    private fun verify() {
        if (createdAt < LocalDateTime.now().minusDays(1)) {
            throw NoSuchElementException()
        }
    }
}
