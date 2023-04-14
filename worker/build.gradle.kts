plugins {
    application
    alias(libs.plugins.kotlin.jvm)
}

group = "com.kazakago.cueue.worker"
version = libs.versions.version.get()

application {
    mainClass.set("com.kazakago.cueue.worker.WorkerKt")
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(projects.server)
    implementation(libs.picocli)
    implementation(libs.typesafe.config)
    implementation(platform(libs.exposed.bom))
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.firebase.admin)
    implementation(libs.sentry)
}
