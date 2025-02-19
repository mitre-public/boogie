plugins {
    `java-library`
    jacoco
    id("caasd.repositories")
    id("caasd.publish")
    id("caasd.auto-semver")
    id("caasd.test-conventions")
    id("caasd.test-logging-conventions")
    id("me.champeau.jmh")
}

group = "org.mitre.tdp"
version = semanticVersion

caasdRepositories {
    mavenCentral
    codev.javaArtifacts {
        mavenContent {
            releasesOnly()
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<Test>("testIntegration") {
    maxHeapSize = "8g"
}

configure<JavaPluginExtension> {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    withSourcesJar()
    withJavadocJar()
}

semanticVersioning {
    initialVersion.set("0.0.1")
    tagPrefix.set("")

    branches.main {
        useConventionalIncrementor()
        formatter { "$baseVersion${-snapshot}" }
    }
    defaultBranch {
        formatter { "$baseVersion${-branchId}${+shortCommit}" }
    }
}

caasdPublishing {
    publishTo {
        codev.javaArtifacts
        github.projectRepo
    }
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
}