plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.flyway)
}

group = "com.kazakago.cueue"
version = libs.versions.version.get()

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(platform(libs.ktor.bom))
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.default.header)
    implementation(libs.ktor.server.forwarded.header)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.content.negotitation)
    implementation(libs.ktor.serialization.json)
    implementation(platform(libs.exposed.bom))
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.flyway)
    implementation(libs.flyway.postgres)
    implementation(libs.koin)
    implementation(libs.firebase.admin)
    implementation(libs.logback)
    implementation(libs.webp.imageio)
    implementation(libs.metadata.extractor)
    implementation(libs.lucene.kuromoji)
    implementation(libs.icu4j)
    implementation(libs.sentry)
    implementation(libs.apache.common.codec)

    testImplementation(libs.ktor.server.tests)
}
