import net.researchgate.release.GitAdapter.GitConfig

plugins {
    `java-platform`
    `maven-publish`
    jacoco
    id("org.mitre.tdp.gradle-cookbook") version "1.0.0-SNAPSHOT"
    id("net.researchgate.release") version "2.8.1" // used to emulate mvn release (see https://github.com/researchgate/gradle-release)
}

tdpConventions {
    `multimodule-reporting-conventions`
    `multimodule-javadoc-conventions`
}

val mavenUser: String? by project
val mavenPassword: String? by project

/** Explicitly declare referenced/used repositories which host the artifact necessary to build the software */
allprojects {
    repositories {
        // mavenLocal() // only uncomment to test a build that depends on locally installed maven artifacts
        maven {
            name = "clojars"
            url = uri("https://repo.clojars.org/")
        }
        maven {
            name = "repo1"
            url = uri("https://repo1.maven.org/maven2/")
        }
        maven {
            name = "codev-artifactory"
            url = uri("https://repo.codev.mitre.org/artifactory/idaass-maven")
            mavenContent {
                releasesOnly()
            }
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
    }
}

release {
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
            maven {
                name = "codev-artifactory"
                url = uri("https://repo.codev.mitre.org/artifactory/idaass-maven")
                credentials {
                    username = mavenUser
                    password = mavenPassword
                }
            }
        }
    }
}
