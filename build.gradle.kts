import net.researchgate.release.GitAdapter.GitConfig
import org.mitre.tdp.repositories.codev
import org.mitre.tdp.repositories.from
import org.mitre.tdp.repositories.repo1

plugins {
    `java-platform`
    `maven-publish`
    jacoco
    id("org.mitre.tdp.gradle-cookbook") version "1.0.0"
    id("net.researchgate.release") version "2.8.1" // used to emulate mvn release (see https://github.com/researchgate/gradle-release)
}

tdpConventions {
    `multimodule-reporting-conventions`
    `multimodule-javadoc-conventions`
}

val codevUserKey = "mavenUser"
val codevPasswordKey = "mavenPassword"

allprojects {
    repositories {
        // only uncomment to test a build that depends on locally installed maven artifacts
        // mavenLocal()
        repo1()
        codev.mavenArtifactory {
            mavenContent {
                releasesOnly()
            }
            credentials.from(rootProject.properties) {
                usernameKey = codevUserKey
                passwordKey = codevPasswordKey
            }
        }
    }
}

release {
    // allow un-versioned files which may have been copied in for use by CI
    failOnUnversionedFiles = false

    preTagCommitMessage = "[Gradle] Bump to stable version "
    newVersionCommitMessage = "[Gradle] Bump to version "

    val git: GitConfig = getProperty("git") as GitConfig
    git.requireBranch = "main"
}

tdpJavadoc {
    javadocPackageName {
        "${date("yyyy-MM-dd")}-boogie-${project.version}.zip"
    }
}

jacoco {
    toolVersion = "0.8.5"
}

val mockitoVersion by extra("3.2.4")
subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    tdpConventions {
        `module-checkstyle-conventions`
        `module-testing-conventions`
    }

    // declare dependencies used in every child module
    dependencies {
        val slf4jVersion = "1.7.25" // "implementation"("org.slf4j:slf4j-simple:1.7.7")
        "implementation"("org.slf4j:slf4j-api:$slf4jVersion")
        "implementation"("org.slf4j:slf4j-log4j12:$slf4jVersion")

        val junitVersion = "5.5.2"
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

        "testImplementation"("org.mockito:mockito-core:$mockitoVersion")
    }

    tdpCheckstyle {
        configFile = rootProject.layout.projectDirectory.file("config/google_checks.xml")
    }

    tasks.named<Javadoc>("javadoc") {
        (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:none", true)
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }

        withSourcesJar()
        withJavadocJar()
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
                        connection.set("scm:git:ssh://git@github.com:mitre-tdp/boogie.git")
                        developerConnection.set("scm:git:ssh://git@github.com:mitre-tdp/boogie.git")
                        url.set("https://github.com/mitre-tdp/boogie")
                        tag.set("HEAD")
                    }
                }
            }
        }
        repositories {
            codev.mavenArtifactory {
                credentials.from(rootProject.properties) {
                    usernameKey = codevUserKey
                    passwordKey = codevPasswordKey
                }
            }
        }
    }
}
