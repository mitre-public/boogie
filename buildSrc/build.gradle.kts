plugins {
    `kotlin-dsl`
}

repositories {
    maven {
        name = "Gradle Plugin Portal"
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    val releasePluginVersion = "2.8.1"
    val testLoggerPluginVersion = "2.0.0"

    implementation("net.researchgate:gradle-release:$releasePluginVersion")
    implementation("com.adarshr:gradle-test-logger-plugin:$testLoggerPluginVersion")
}