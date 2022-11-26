import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797
plugins {
    application
    alias(libs.plugins.kotlin.jvm)
}

group = "com.kazakago.cueue.suspender"
version = libs.versions.version.get()

application {
    mainClass.set("com.kazakago.cueue.suspender.SuspenderKt")
}

java {
    setSourceCompatibility(libs.versions.java.get())
    setTargetCompatibility(libs.versions.java.get())
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = libs.versions.java.get()
}

dependencies {
    implementation(libs.picocli)
    implementation(libs.typesafe.config)
    implementation(libs.okHttp)
}
