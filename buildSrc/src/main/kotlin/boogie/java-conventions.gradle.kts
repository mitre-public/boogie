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



