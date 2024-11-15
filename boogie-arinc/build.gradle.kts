plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":boogie-core"))
    implementation(libs.bundles.commons)

    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)
}

tasks.test {
    useJUnitPlatform {
        excludeTags("INTEGRATION")
    }
    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
}

tasks.register<Test>("integrationTest") {
    useJUnitPlatform {
        includeTags("INTEGRATION")
    }
    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
    maxHeapSize = "2G"
}

configure<JavaPluginExtension> {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    withSourcesJar()
    withJavadocJar()
}
