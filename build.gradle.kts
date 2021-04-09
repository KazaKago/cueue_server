plugins {
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}
