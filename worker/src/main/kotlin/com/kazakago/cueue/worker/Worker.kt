package com.kazakago.cueue.worker

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.ExportedUserRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.StorageClient
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
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import picocli.CommandLine
import picocli.CommandLine.Option
import java.io.File
import java.io.FileInputStream
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

    @Option(names = ["-config"], defaultValue = "app/src/main/resources/application.conf")
    lateinit var config: String

    override fun call(): Int {
        val conf = ConfigFactory.load(ConfigFactory.parseFile(File(config)))

        val sentryDsn = conf.getString("app.sentry.dsn")
        if (sentryDsn.isNotBlank()) {
            Sentry.init { it.dsn = sentryDsn }
        }
        val file = File(conf.getString("app.firebase.credentials"))
        FileInputStream(file).use {
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(it))
                .setStorageBucket(conf.getString("app.firebase.storage_bucket_name"))
                .build()
            FirebaseApp.initializeApp(options)
        }
        Database.connect(
            url = conf.getString("app.database.url"),
            driver = "org.postgresql.Driver",
            user = conf.getString("app.database.user"),
            password = conf.getString("app.database.password"),
        )

        if (dryRun) println("\n--- RUN WITH \"DRY RUN\" MODE! ---\n")

        transaction {
            // Delete User if firebase user is not available.
            val firebaseUserUids = listFirebaseUsers().map { it.uid }
            val usersQuery = UsersTable
                .select { UsersTable.uid notInList firebaseUserUids }
            val users = UserEntity.wrapRows(usersQuery)
            users.forEach {
                if (!dryRun) it.delete()
                println("[Deleted User] id = ${it.id}, uid = ${it.uid}, displayName = ${it.displayName}")
            }
            println()

            // Delete workspace if user is not connected.
            val workspacesQuery = WorkspacesTable
                .leftJoin(UsersTable)
                .select { UsersTable.id.isNull() }
            val workspaces = WorkspaceEntity.wrapRows(workspacesQuery)
            workspaces.forEach {
                if (!dryRun) it.delete()
                println("[Deleted Workspace] id = ${it.id}, name = ${it.name}")
            }
            println()

            // Delete content if no relation.
            val contentsQuery = ContentsTable
                .leftJoin(UsersTable)
                .select { ContentsTable.recipeId.isNull() and UsersTable.uid.isNull() }
            val contents = ContentEntity.wrapRows(contentsQuery)
            val bucket = StorageClient.getInstance().bucket()
            contents.forEach {
                val blob = bucket.get(it.key)
                if (!dryRun) blob.delete()
                println("[Deleted Blob] name = ${blob.name}")
                if (!dryRun) it.delete()
                println("[Deleted Content] id = ${it.id}, key = ${it.key}")
            }
            println()

            // Delete invitation more than 24 hours spends.
            val invitationQuery = InvitationsTable
                .select { InvitationsTable.createdAt less LocalDateTime.now().minusDays(1) }
            val invitations = InvitationEntity.wrapRows(invitationQuery)
            invitations.forEach {
                if (!dryRun) it.delete()
                println("[Deleted Invitation] id = ${it.id}, code = ${it.code}, created_at = ${it.createdAt}")
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
