buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", "1.3.72"))
        classpath("com.google.cloud.tools:appengine-gradle-plugin:2.3.0")
        classpath("com.github.jengelman.gradle.plugins:shadow:6.0.0")
    }
}

allprojects {
    repositories {
        jcenter()
    }
}