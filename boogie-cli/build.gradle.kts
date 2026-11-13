plugins {
    id("java-conventions")
    application
}

application {
    applicationName = "boogie"
    mainClass.set("org.mitre.boogie.cli.BoogieCli")
}

dependencies {
    implementation(project(":boogie-arinc"))
    // boogie-arinc logs through SLF4J; a command-line viewer keeps stderr for its own diagnostics.
    runtimeOnly(libs.slf4j.nop)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

// The CLI is distributed with installDist/distZip, not as a library: it deliberately omits the Maven Central
// (vanniktech) plugin, and the publication that java-conventions registers is never published.
tasks.withType<AbstractPublishToMaven>().configureEach {
    enabled = false
}
