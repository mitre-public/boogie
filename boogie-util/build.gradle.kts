import org.gradle.kotlin.dsl.assign

plugins {
    id("java-conventions")
    id("com.vanniktech.maven.publish.base") version "0.34.0"
}

dependencies {
    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)
    testImplementation(libs.bundles.jgrapht)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    val releaseVersion = System.getenv("BOOGIE_RELEASE_VERSION")
    version = if (releaseVersion.isNullOrBlank()) project.version.toString() else releaseVersion

    coordinates("org.mitre.boogie", project.name, version.toString())

    pom {
        name.set(rootProject.name)
        description.set(rootProject.description)
        inceptionYear.set("2024")
        url.set("https://github.com/mitre-public/boogie")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("dbaker-mitre")
                name.set("David Baker")
                url.set("https://github.com/dbaker-mitre")
            }
            developer {
                id.set("mattpollock")
                name.set("Matt Pollock")
                url.set("https://github.com/mattpollock")
            }
        }
        scm {
            url.set("https://github.com/mitre-public/boogie")
            connection.set("scm:git:git://github.com/mitre-public/boogie.git")
            developerConnection.set("scm:git:ssh://git@github.com/mitre-public/boogie.git")
        }
    }
}