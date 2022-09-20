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

val codevUserKey = "codevUser"
val codevPasswordKey = "codevPassword"

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

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    tdpConventions {
        `module-checkstyle-conventions`
        `module-testing-conventions`
    }

    val guavaVersion = "23.0"
    dependencies {

        val commonsVersion = "0.0.49"
        api("org.mitre.caasd:commons:$commonsVersion")

        val gsonVersion = "2.9.1"
        "implementation"("com.google.guava:guava:$guavaVersion")
        "implementation"("com.google.code.gson:gson:$gsonVersion")

        val math3Version = "3.6.1"
        "implementation"("org.apache.commons:commons-math3:$math3Version")

        val jgraphtVersion = "1.5.1"
        api("org.jgrapht:jgrapht-core:$jgraphtVersion")
        api("org.jgrapht:jgrapht-io:$jgraphtVersion")

        val slf4jVersion = "2.0.0" // "implementation"("org.slf4j:slf4j-simple:1.7.7")
        "implementation"("org.slf4j:slf4j-api:$slf4jVersion")
        "implementation"("org.slf4j:slf4j-log4j12:$slf4jVersion")

        val junitVersion = "5.5.2"
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        "testImplementation"("org.junit.jupiter:junit-jupiter-params:$junitVersion")

        val mockitoVersion = "3.2.4"
        "testImplementation"("org.mockito:mockito-core:$mockitoVersion")
        "testImplementation"("org.mockito:mockito-inline:$mockitoVersion")

        "testImplementation"("nl.jqno.equalsverifier:equalsverifier:3.7")

        configurations.all {
            exclude("junit", "junit") //because we use only JUnit 5 (jupiter)
            resolutionStrategy.force("com.google.guava:guava:$guavaVersion")
        }
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
