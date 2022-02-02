rootProject.name = "boogie-parent"
include(":boogie-core")
include(":boogie-arinc")
include(":boogie-routes")
include(":boogie-conformance")
include(":boogie-test")
include(":boogie-rest")

pluginManagement {
    repositories {
        /* Other plugin portals you're using, e.g. gradlePluginPortal() */
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }

        maven {
            name = "codev-gradle-artifactory"
            url = uri("https://repo.codev.mitre.org/artifactory/idaass-gradle")

            val mavenUser: String? by settings // NOTE: "by project" won't work here
            val mavenPassword: String? by settings
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
    }
}