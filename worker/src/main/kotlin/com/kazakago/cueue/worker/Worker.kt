package com.kazakago.cueue.worker

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.ExportedUserRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.StorageClient
import com.kazakago.cueue.config.database.ConnectionInfo
import com.kazakago.cueue.config.firebase.FirebaseCredentials
import com.kazakago.cueue.database.entity.ContentEntity
import com.kazakago.cueue.database.entity.InvitationEntity
import com.kazakago.cueue.database.entity.UserEntity
import com.kazakago.cueue.database.entity.WorkspaceEntity
import com.kazakago.cueue.database.table.ContentsTable
import com.kazakago.cueue.database.table.InvitationsTable
import com.kazakago.cueue.database.table.UsersTable
import com.kazakago.cueue.database.table.WorkspacesTable
import com.typesafe.config.ConfigFactory
import io.sentry.Sentry
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import picocli.CommandLine
import picocli.CommandLine.Option
import java.io.File
import java.time.LocalDateTime
import java.util.concurrent.Callable
import kotlin.system.exitProcess

fun main(args: Array<String>): Unit = exitProcess(
    CommandLine(Worker())
        .setExecutionExceptionHandler(ExceptionHandler())
        .execute(*args)
)

class Worker : Callable<Int> {

    @Option(names = ["-dry-run"])
    var dryRun = false

    @Option(names = ["-config"], defaultValue = "server/src/main/resources/application.conf")
    lateinit var config: String

    override fun call(): Int {
        val conf = ConfigFactory.load(ConfigFactory.parseFile(File(config)))

        val sentryDsn = conf.getString("app.sentry.dsn")
        if (sentryDsn.isNotBlank()) {
            Sentry.init { it.dsn = sentryDsn }
        }
        val credentials = FirebaseCredentials(conf.getString("app.firebase.credentials"))
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(credentials.inputStream))
            .setStorageBucket(conf.getString("app.firebase.storage_bucket_name"))
            .build()
        FirebaseApp.initializeApp(options)
        val connectionInfo = ConnectionInfo(conf.getString("app.database.url"))
        Database.connect(
            url = connectionInfo.jdbcUrl,
            driver = "org.postgresql.Driver",
            user = connectionInfo.user,
            password = connectionInfo.password,
        )

        if (dryRun) println("\n--- RUN WITH \"DRY RUN\" MODE! ---\n")

        transaction {
            // Delete users not attached to Firebase Authentication.
            val firebaseUserUids = listFirebaseUsers().map { it.uid }
            val usersQuery = UsersTable
                .selectAll()
                .where { UsersTable.uid notInList firebaseUserUids }
            val users = UserEntity.wrapRows(usersQuery)
            users.forEach {
                println("[Deleting User] id = ${it.id}, uid = ${it.uid}, displayName = ${it.displayName}")
                if (!dryRun) it.delete()
            }
            println()

            // Delete Workspace not attached to any user.
            val workspacesQuery = WorkspacesTable
                .leftJoin(UsersTable)
                .selectAll()
                .where { UsersTable.id.isNull() }
            val workspaces = WorkspaceEntity.wrapRows(workspacesQuery)
            workspaces.forEach {
                println("[Deleting Workspace] id = ${it.id}, name = ${it.name}")
                if (!dryRun) it.delete()
            }
            println()

            // Delete content that has been created for more than 24 hours and is not attached anywhere
            val contentsQuery = ContentsTable
                .leftJoin(UsersTable)
                .selectAll()
                .where { ContentsTable.recipeId.isNull() and UsersTable.uid.isNull() }
            val contents = ContentEntity.wrapRows(contentsQuery)
            val bucket = StorageClient.getInstance().bucket()
            contents.forEach {
                val blob = bucket.get(it.key)
                println("[Deleting Blob] name = ${blob.name}")
                if (!dryRun) blob.delete()
                println("[Deleting Content] id = ${it.id}, key = ${it.key}, created_at = ${it.createdAt}")
                if (!dryRun) it.delete()
            }
            println()

            // Delete invitations that have been created for more than 24 hours
            val invitationQuery = InvitationsTable
                .selectAll()
                .where { InvitationsTable.createdAt less LocalDateTime.now().minusDays(1) }
            val invitations = InvitationEntity.wrapRows(invitationQuery)
            invitations.forEach {
                println("[Deleting Invitation] id = ${it.id}, code = ${it.code}, created_at = ${it.createdAt}")
                if (!dryRun) it.delete()
            }
            println()
        }
        return 0
    }

    private fun listFirebaseUsers(): List<ExportedUserRecord> {
        val firebaseUsers = mutableListOf<ExportedUserRecord>()
        var firebaseUserPages = FirebaseAuth.getInstance().listUsers(null)
        while (firebaseUserPages != null) {
            firebaseUsers.addAll(firebaseUserPages.values)
            firebaseUserPages = firebaseUserPages.nextPage
        }
        return firebaseUsers
    }
}

private class ExceptionHandler : CommandLine.IExecutionExceptionHandler {
    override fun handleExecutionException(ex: Exception, cmd: CommandLine, parseResult: CommandLine.ParseResult): Int {
        Sentry.captureException(ex)
        throw ex
    }
}
