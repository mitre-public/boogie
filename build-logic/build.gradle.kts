plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("caasd.repositories")
}

caasdRepositories {
    mavenCentral
    codev.gradlePlugins
}

repositories {
    gradlePluginPortal() // for test logging plugin dep
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("caasd-gradle-plugins:caasd-auto-semver-plugin:1.2.0")
    implementation("caasd-gradle-plugins:caasd-repositories-plugin:1.1.0")
    implementation("caasd-gradle-plugins:caasd-repositories:1.1.0")
    implementation("caasd-gradle-plugins:caasd-publishing-plugin:1.0.0")
    implementation("caasd-gradle-plugins:caasd-test-conventions-plugin:1.0.0")
    implementation("caasd-gradle-plugins:caasd-test-logging-conventions-plugin:1.0.0")
}