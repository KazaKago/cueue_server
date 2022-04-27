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

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.shadowJar {
    archiveClassifier.set("")
    archiveVersion.set("")
    mergeServiceFiles()
}

tasks.register("stage") {
    group = "heroku"
    dependsOn(tasks.clean)
    dependsOn(tasks.shadowJar)
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:2.0.0"))
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-default-headers")
    implementation("io.ktor:ktor-server-forwarded-header")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation(platform("org.jetbrains.exposed:exposed-bom:0.38.2"))
    implementation("org.jetbrains.exposed:exposed-core")
    implementation("org.jetbrains.exposed:exposed-dao")
    implementation("org.jetbrains.exposed:exposed-jdbc")
    implementation("org.jetbrains.exposed:exposed-java-time")
    implementation("org.postgresql:postgresql:42.3.4")
    implementation("org.flywaydb:flyway-core:8.5.9")
    implementation("io.insert-koin:koin-core:3.1.6")
    implementation("com.google.firebase:firebase-admin:8.1.0")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("org.sejda.imageio:webp-imageio:0.1.6")
    implementation("com.drewnoakes:metadata-extractor:2.17.0")
    implementation("net.swiftzer.semver:semver:1.2.0")

    testImplementation("io.ktor:ktor-server-tests")
}
