plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    id("caasd.repositories")
}

caasdRepositories {
    mavenCentral
    gradlePluginPortal
    caasdPluginPortal
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("caasd-gradle-plugins:caasd-auto-semver-plugin:1.3.2")
    implementation("caasd-gradle-plugins:caasd-repositories-plugin:3.1.1")
    implementation("caasd-gradle-plugins:caasd-repositories:3.1.1")
    implementation("caasd-gradle-plugins:caasd-publishing-plugin:3.1.1")
    implementation("caasd-gradle-plugins:caasd-test-conventions-plugin:1.0.0")
    implementation("caasd-gradle-plugins:caasd-test-logging-conventions-plugin:1.0.0")
    implementation("me.champeau.jmh:jmh-gradle-plugin:0.7.2")
}