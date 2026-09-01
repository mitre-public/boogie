plugins {
    id("java-conventions")
    id("com.vanniktech.maven.publish.base") version "0.34.0"
}

val jmh = sourceSets.create("jmh") {
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += output + compileClasspath
}

configurations[jmh.implementationConfigurationName].extendsFrom(configurations.implementation.get())
configurations[jmh.runtimeOnlyConfigurationName].extendsFrom(configurations.runtimeOnly.get())

dependencies {
    implementation(project(":boogie-core"))
    implementation(project(":boogie-util"))
    implementation(libs.bundles.commons)

    add(jmh.implementationConfigurationName, libs.jmh.core)
    add(jmh.annotationProcessorConfigurationName, libs.jmh.generator)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.bundles.test.tools)
    testImplementation(libs.bundles.jgrapht)
}

fun registerJmhTask(name: String, dataset: String?, specSet: String?, resultFileName: String) = tasks.register<JavaExec>(name) {
    group = "performance"
    val fixtures = dataset?.let { "the $it fixture" } ?: "the CIFP and LIDO fixtures"
    val specs = when (specSet) {
        "FULL" -> "the full record specs"
        "NAV" -> "the navigation-only record specs"
        else -> "both the full and navigation-only record specs"
    }
    description = "Benchmarks the OneShot parser with $fixtures using $specs."

    dependsOn(jmh.classesTaskName)
    mainClass.set("org.openjdk.jmh.Main")
    classpath = jmh.runtimeClasspath
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(17))
    })
    workingDir = rootProject.projectDir

    val resultFile = layout.buildDirectory.file("reports/jmh/$resultFileName")
    args(
        "org.mitre.tdp.boogie.arinc.OneshotRecordParserBenchmark",
        "-rf", "json",
        "-rff", resultFile.get().asFile.absolutePath,
        "-prof", "gc",
        "-foe", "true"
    )
    dataset?.let { args("-p", "dataset=$it") }
    specSet?.let { args("-p", "specSet=$it") }

    doFirst {
        resultFile.get().asFile.parentFile.mkdirs()
    }
}

registerJmhTask("jmh", null, null, "results.json")
registerJmhTask("jmhCifp", "CIFP", "FULL", "cifp.json")
registerJmhTask("jmhCifpNav", "CIFP", "NAV", "cifp-nav.json")
registerJmhTask("jmhLido", "LIDO", "FULL", "lido.json")
registerJmhTask("jmhLidoNav", "LIDO", "NAV", "lido-nav.json")

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
            developer {
                id.set("stellarsunset")
                name.set("Alex Cramer")
                url.set("https://github.com/stellarsunset")
            }
        }
        scm {
            url.set(rootProject.property("url").toString())
            connection.set(rootProject.property("connection").toString())
            developerConnection.set(rootProject.property("developerConnection").toString())
        }
    }
}
