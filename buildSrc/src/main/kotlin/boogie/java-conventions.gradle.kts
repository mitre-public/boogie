plugins {
    id("java-library")
    id("maven-publish")
}

group = "org.mitre.boogie"
version = "4.6.0"

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            artifactId = project.name

            from(components["java"])

            pom {
                name.set(project.name)
                organization {
                    name.set("The MITRE Corporation TDP")
                    url.set("https://github.com/mitre-tdp")
                }
                scm {
                    connection.set("scm:git:ssh://git@github.com:mitre-public/boogie.git")
                    developerConnection.set("scm:git:ssh://git@github.com:mitre-public/boogie.git")
                    url.set("https://github.com/mitre-public/boogie")
                    tag.set("HEAD")
                }
            }
        }
    }

    //repositories {
    //    maven {
    //        name = "myRepo"
    //        url = uri(layout.buildDirectory.dir("repo"))
    //    }
    //}
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



