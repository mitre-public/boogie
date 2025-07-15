plugins {
    id("java-library")
    id("maven-publish")
}


repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            group = "org.mitre.boogie"
            artifactId = project.name

            from(components["java"])

            pom {
                name.set(project.name)
                description.set("Boogie - A dependency-light software project for navigation data parsing, route expansion, and conformance.")
                url.set("https://github.com/mitre-public/boogie")
                organization {
                    name.set("The MITRE Corporation TDP")
                    url.set("https://github.com/mitre-public")
                }
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("dbaker")
                        name.set("David Baker")
                        email.set("dbaker@mitre.org")
                    }
                    developer {
                        id.set("mpollock")
                        name.set("Matthew Pollock")
                        email.set("mpollock@mitre.org")
                    }
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

    val ghprToken: String? by project
    repositories {
        maven {
            name = "boogie-public"
            url = uri("https://maven.pkg.github.com/mitre-public/boogie")
            credentials(HttpHeaderCredentials::class) {
                name = "Authorization"
                value = "Bearer ${ghprToken}"
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
        maven {
            name = "mavenCentral"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.findProperty("mavenCentralUsername") as String? ?: System.getenv("MAVEN_CENTRAL_USERNAME")
                password = project.findProperty("mavenCentralPassword") as String? ?: System.getenv("MAVEN_CENTRAL_PASSWORD")
            }
        }
    }
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
        excludeTags("LIDO")
        excludeTags("DAFIF")
        includeTags("INTEGRATION")
    }

    maxHeapSize = "8G"

    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
}

tasks.register<Test>("all-local-data-is-there") {
    group = "verification"
    useJUnitPlatform {
        includeTags("LIDO")
        includeTags("INTEGRATION")
        includeTags("DAFIF")
        includeTags("CIFP")
    }

    maxHeapSize = "8G"

    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
}

tasks.register<Test>("unit") {
    group = "verification"
    useJUnitPlatform {
        excludeTags("INTEGRATION")
        excludeTags("LIDO")
        excludeTags("DAFIF")
        excludeTags("CIFP")
    }

    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
}

tasks.register<Test>("cifp-integration") {
    group = "verification"
    useJUnitPlatform {
        includeTags("INTEGRATION")
        includeTags("CIFP")
        excludeTags("LIDO")
        excludeTags("DAFIF")
    }

    maxHeapSize = "2G"

    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
}

tasks.register<Test>("lido-integration") {
    group = "verification"
    useJUnitPlatform {
        includeTags("LIDO")
        includeTags("INTEGRATION")
        excludeTags("DAFIF")
        excludeTags("CIFP")
    }

    maxHeapSize = "8G"

    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
}

tasks.register<Test>("dafif-integration") {
    group = "verification"
    useJUnitPlatform {
        includeTags("DAFIF")
        includeTags("INTEGRATION")
        excludeTags("LIDO")
        excludeTags("CIFP")
    }

    maxHeapSize = "4G"

    testLogging {
        events("passed", "skipped", "failed") // Log these events
    }
}

