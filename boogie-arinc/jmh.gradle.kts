import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register

val sourceSets = extensions.getByType<SourceSetContainer>()
val javaToolchains = extensions.getByType<JavaToolchainService>()
val jmhVersion = "1.37"

val jmh = sourceSets.create("jmh") {
    compileClasspath += sourceSets.getByName("main").output
    runtimeClasspath += output + compileClasspath
}

configurations[jmh.implementationConfigurationName].extendsFrom(configurations["implementation"])
configurations[jmh.runtimeOnlyConfigurationName].extendsFrom(configurations["runtimeOnly"])

dependencies {
    add(jmh.implementationConfigurationName, "org.openjdk.jmh:jmh-core:$jmhVersion")
    add(jmh.annotationProcessorConfigurationName, "org.openjdk.jmh:jmh-generator-annprocess:$jmhVersion")
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
