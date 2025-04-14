plugins {
    id("java-conventions")
}

dependencies {
    implementation(project(":boogie-core"))
    implementation(libs.bundles.commons)
    implementation(libs.bundles.jgrapht)

    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)
}

tasks.jar {
    val version = if (project.hasProperty("version")) {
        project.property("version") as String
    } else {
        "unspecified"
    }
//    archiveBaseName.set("boogie-core")
    archiveVersion.set(version)
}
