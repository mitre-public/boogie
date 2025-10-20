plugins {
    id("java-conventions")
    id("com.vanniktech.maven.publish.base") version "0.34.0"
}

dependencies {
    implementation(project(":boogie-core"))
    implementation(project(":boogie-util"))
    implementation(libs.bundles.commons)

    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)
    testImplementation(libs.bundles.jgrapht)
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()

    val releaseVersion = System.getenv("BOOGIE_RELEASE_VERSION")
    version = if (releaseVersion.isNullOrBlank()) project.version.toString() else releaseVersion

    coordinates("org.mitre.boogie", project.name, version.toString())

    pom {
        name.set(project.name)
        description.set("parser implementations for various versions and record types within ARINC-424")
        inceptionYear.set(rootProject.property("inceptionYear").toString())
        url.set(rootProject.property("url").toString())
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
            url.set(rootProject.property("url").toString())
            connection.set(rootProject.property("connection").toString())
            developerConnection.set(rootProject.property("developerConnection").toString())
        }
    }
}
