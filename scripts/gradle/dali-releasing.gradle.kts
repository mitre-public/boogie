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