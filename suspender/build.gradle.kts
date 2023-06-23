plugins {
    application
    alias(libs.plugins.kotlin.jvm)
}

group = "com.kazakago.cueue.suspender"
version = libs.versions.version.get()

application {
    mainClass.set("com.kazakago.cueue.suspender.SuspenderKt")
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.picocli)
    implementation(libs.typesafe.config)
    implementation(libs.okHttp)
}
