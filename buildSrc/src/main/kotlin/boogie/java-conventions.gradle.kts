plugins {
    id("java-library")
}

group = "org.mitre.boogie"
version = "4.6.0"

repositories {
    mavenCentral()
}

configure<JavaPluginExtension> {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    withSourcesJar()
    withJavadocJar()
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        excludeTags("INTEGRATION")
        excludeTags("LIDO")
        excludeTags("DAFIF")
    }

    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
}

tasks.register<Test>("integration") {
    group = "verification"
    useJUnitPlatform {
        includeTags("INTEGRATION")
        excludeTags("LIDO")
        excludeTags("DAFIF")
    }

    maxHeapSize = "2G"

    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
}

tasks.register<Test>("supplier-integration") {
    group = "verification"
    useJUnitPlatform {
        includeTags("LIDO")
        includeTags("DAFIF")
    }

    maxHeapSize = "4G"

    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
}



