import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
}

group = "com.kazakago.cueue.worker"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("com.kazakago.cueue.worker.WorkerKt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.register("stage") {
    group = "heroku"
    dependsOn(tasks.installDist)
}

dependencies {
    implementation(project(":app"))
    implementation("info.picocli:picocli:4.6.3")
    implementation("com.typesafe:config:1.4.2")
    implementation(platform("org.jetbrains.exposed:exposed-bom:0.39.2"))
    implementation("org.jetbrains.exposed:exposed-core")
    implementation("org.jetbrains.exposed:exposed-dao")
    implementation("org.jetbrains.exposed:exposed-jdbc")
    implementation("org.jetbrains.exposed:exposed-java-time")
    implementation("org.postgresql:postgresql:42.4.1")
    implementation("com.google.firebase:firebase-admin:9.0.0")
    implementation("io.sentry:sentry:6.3.1")
}
