allprojects {
    repositories {
        // only uncomment to test a build that depends on locally installed maven artifacts
//        mavenLocal()
        maven {
            name = "clojars"
            url = uri("https://repo.clojars.org/")
        }
        maven {
            name = "dali-mirror"
            url = uri("https://dali.mitre.org/nexus/content/groups/mirror")
            content {
                excludeGroup("org.jacoco")// dali's: mirror/org/jacoco doesn't have all the agent jar's, just runtime which breaks the build
            }
        }
        maven {
            name = "dali-proxied-repositories"
            url = uri("https://dali.mitre.org/nexus/content/groups/proxied-repositories")
        }
        maven {
            name = "dali-external"
            url = uri("https://dali.mitre.org/nexus/content/groups/external")
        }
        maven {
            name = "mitre-caasd-releases"
            url = uri("https://dali.mitre.org/nexus/content/repositories/mitre-caasd-releases/")
        }
        maven {
            name = "dali-mitre-caasd-releases"
            url = uri("https://dali.mitre.org/nexus/content/groups/mitre-caasd")
        }
        maven {
            name = "dali-hortonworks-releases"
            url = uri("https://repo.hortonworks.com/content/repositories/releases")
        }
        maven {
            name = "dali-jetty-hadoop"
            url = uri("https://repo.hortonworks.com/content/repositories/jetty-hadoop")
        }
        maven {
            name = "dali-jetty-hadoop"
            url = uri("https://dali.mitre.org/nexus/content/repositories/atlassian/")
        }
        maven {
            name = "dynamodb-local"
            url = uri("https://s3-us-west-2.amazonaws.com/dynamodb-local/release")
        }
    }
}

/* *******************************
 * configure artifact publishing *
 *********************************/

/* create a ~/.gradle/gradle.properties file which defines mavenUser and mavenPassword (copy from ~/.m2/settings.xml
 * @see: https://github.com/gradle/gradle/issues/1236
 * TODO investigate using https://github.com/etiennestuder/gradle-credentials-plugin */
val mavenUser: String? by project
val mavenPassword: String? by project

/** configure publishing */
subprojects {
    apply(plugin = "maven-publish")// https://docs.gradle.org/current/userguide/publishing_maven.html

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>(project.name) {
                artifactId = project.name

                apply(plugin = "java-library")// https://docs.gradle.org/current/userguide/java_library_plugin.html
                from(components["java"])

                pom {
                    name.set(project.name)
                    organization {
                        name.set("The MITRE Corporation")
                        url.set("https://www.mitre.org")
                    }
                    scm {
                        connection.set("scm:git:ssh://git@mustache.mitre.org:7999/ttfs/boogie.git")
                        developerConnection.set("scm:git:ssh://git@mustache.mitre.org:7999/ttfs/boogie.git")
                        url.set("https://mustache.mitre.org/projects/TTFS/repos/boogie")
                        tag.set("HEAD")
                    }
                }
            }
        }
        repositories {
            maven {
                //logger.lifecycle("using version [$version] to determine publish repo")
                if (version.toString().endsWith("SNAPSHOT")) {
                    name = "mitre-caasd-snapshots"
                    url = uri("https://dali.mitre.org/nexus/content/repositories/mitre-caasd-snapshots/")
                } else {
                    name = "mitre-caasd-releases"
                    url = uri("https://dali.mitre.org/nexus/content/repositories/mitre-caasd-releases/")
                }
                credentials {
                    username = mavenUser
                    password = mavenPassword
                }
            }
        }
    }
}