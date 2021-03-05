import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.flywaydb.flyway") version "7.5.4"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.kazakago.cueue"
version = "1.0.0-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
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
    implementation("io.ktor:ktor-server-netty:1.5.2")
    implementation("io.ktor:ktor-serialization:1.5.2")
    implementation("io.ktor:ktor-auth:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    implementation("org.jetbrains.exposed:exposed-core:0.29.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.29.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.29.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.29.1")
    implementation("org.postgresql:postgresql:42.2.19")
    implementation("org.flywaydb:flyway-core:7.5.4")
    implementation("org.koin:koin-ktor:2.2.2")
    implementation("com.google.firebase:firebase-admin:7.1.0")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.sejda.imageio:webp-imageio:0.1.6")
    implementation("com.drewnoakes:metadata-extractor:2.15.0")

    testImplementation("io.ktor:ktor-server-tests:1.4.3")
}
