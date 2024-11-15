plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)
    implementation(libs.bundles.commons)
    implementation(libs.bundles.jgrapht)

    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)
}

tasks.test {
    useJUnitPlatform() // Use JUnit 5 platform
    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
}

configure<JavaPluginExtension> {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}
