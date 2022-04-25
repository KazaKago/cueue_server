import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.flywaydb.flyway") version "8.5.9"
    id("com.github.johnrengelman.shadow") version "7.1.2"
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
    implementation("io.ktor:ktor-server-netty:1.6.8")
    implementation("io.ktor:ktor-serialization:1.6.8")
    implementation("io.ktor:ktor-auth:1.6.8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.exposed:exposed-core:0.38.2")
    implementation("org.jetbrains.exposed:exposed-dao:0.38.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.38.2")
    implementation("org.jetbrains.exposed:exposed-java-time:0.38.2")
    implementation("org.postgresql:postgresql:42.3.4")
    implementation("org.flywaydb:flyway-core:8.5.9")
    implementation("io.insert-koin:koin-ktor:3.1.6")
    implementation("com.google.firebase:firebase-admin:8.1.0")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.sejda.imageio:webp-imageio:0.1.6")
    implementation("com.drewnoakes:metadata-extractor:2.17.0")
    implementation("net.swiftzer.semver:semver:1.2.0")

    testImplementation("io.ktor:ktor-server-tests:1.6.8")
}
