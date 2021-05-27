import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.flywaydb.flyway") version "7.9.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.kazakago.cueue"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "11"
}

tasks.shadowJar {
    archiveClassifier.set("")
    mergeServiceFiles()
}

tasks.register("stage") {
    group = "heroku"
    dependsOn(tasks.clean)
    dependsOn(tasks.shadowJar)
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-netty:1.5.4")
    implementation("io.ktor:ktor-serialization:1.5.4")
    implementation("io.ktor:ktor-auth:1.5.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    implementation("org.jetbrains.exposed:exposed-core:0.31.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.31.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.31.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.31.1")
    implementation("org.postgresql:postgresql:42.2.20")
    implementation("org.flywaydb:flyway-core:7.9.1")
    implementation("io.insert-koin:koin-ktor:3.0.2")
    implementation("com.google.firebase:firebase-admin:7.3.0")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.sejda.imageio:webp-imageio:0.1.6")
    implementation("com.drewnoakes:metadata-extractor:2.16.0")

    testImplementation("io.ktor:ktor-server-tests:1.5.4")
}
