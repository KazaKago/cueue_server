import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("com.google.cloud.tools.appengine") version "2.4.1"
}

group = "com.kazakago.weekly_cook_plan"
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
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

appengine {
    stage {
        setAppEngineDirectory("./appengine")
    }
    deploy {
        projectId = "weekly-cook-plan"
        version = "1"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("io.ktor:ktor-server-netty:1.4.3")
    implementation("io.ktor:ktor-serialization:1.4.3")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    testImplementation("io.ktor:ktor-server-tests:1.4.3")
}
