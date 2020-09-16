import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("com.google.cloud.tools.appengine") version "2.3.0"
}

group = "com.kazakago.ktor_starter"
version = "1.0.0-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.shadowJar {
    archiveClassifier.set("")
    archiveVersion.set("")
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

appengine {
    stage {
        setAppEngineDirectory("./appengine")
    }
    deploy {
        projectId = "kazakago-playground"
        version = "1"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-netty:1.4.0")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    testImplementation("io.ktor:ktor-server-tests:1.4.0")
}
