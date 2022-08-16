package com.kazakago.cueue.repository

import com.google.cloud.storage.Acl
import com.google.cloud.storage.Bucket
import com.kazakago.cueue.database.entity.ContentEntity
import com.kazakago.cueue.model.Content
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ContentRepository(private val bucket: Bucket) {

    suspend fun create(content: Content): String {
        return newSuspendedTransaction {
            val blob = bucket.create(content.filePath, content.imageByte, content.mimeType)
            blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))
            ContentEntity.new {
                this.key = blob.name
            }
            blob.name
        }
    }
}
