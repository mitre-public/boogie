/*********************************
 * Configure artifact publishing *
 *********************************/

plugins {
    `java-library`
    `maven-publish`
}

configure<PublishingExtension> {
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

            val (mavenUser, mavenPassword) = mavenCredentials
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
    }
}