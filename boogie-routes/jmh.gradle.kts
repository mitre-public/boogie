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
    // Reuse the checked-in navigation fixtures without the test resources' debug logging configuration.
    compileClasspath += sourceSets.getByName("main").output + sourceSets.getByName("test").output.classesDirs
    runtimeClasspath += output + compileClasspath
}

configurations[jmh.implementationConfigurationName].extendsFrom(configurations["testImplementation"])
configurations[jmh.runtimeOnlyConfigurationName].extendsFrom(configurations["testRuntimeOnly"])

dependencies {
    add(jmh.implementationConfigurationName, "org.openjdk.jmh:jmh-core:$jmhVersion")
    add(jmh.annotationProcessorConfigurationName, "org.openjdk.jmh:jmh-generator-annprocess:$jmhVersion")
}

fun registerJmhTask(
    name: String,
    smoke: Boolean,
    benchmark: String = "org.mitre.tdp.boogie.alg.facade.FluentRouteExpanderBenchmark",
    resultFileName: String = if (smoke) "smoke.json" else "results.json"
) = tasks.register<JavaExec>(name) {
    group = "performance"
    description = when {
        smoke -> "Checks that the route expansion benchmarks run."
        name == "jmhLinker" -> "Compares separate and combined radius/nearest linking scans."
        name == "jmhShortestPath" -> "Compares repeated and reused Dijkstra searches across route endpoints."
        else -> "Benchmarks route expansion using existing test fixtures."
    }
    dependsOn(jmh.classesTaskName)
    mainClass.set("org.openjdk.jmh.Main")
    classpath = jmh.runtimeClasspath
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(17))
    })

    val resultFile = layout.buildDirectory.file("reports/jmh/$resultFileName")
    args(
        benchmark,
        "-rf", "json",
        "-rff", resultFile.get().asFile.absolutePath,
        "-prof", "gc",
        "-foe", "true"
    )
    if (smoke) {
        args("-f", "1", "-wi", "1", "-i", "1", "-w", "200ms", "-r", "200ms")
    }

    doFirst {
        resultFile.get().asFile.parentFile.mkdirs()
    }
}

registerJmhTask("jmh", false)
// JMH uses a shared benchmark lock; keep these tasks sequential when requested together.
registerJmhTask("jmhSmoke", true).configure {
    mustRunAfter("jmh")
}
registerJmhTask("jmhLinker", false, "org.mitre.tdp.boogie.alg.chooser.graph.RangeOrClosestBenchmark", "linker.json").configure {
    mustRunAfter("jmh", "jmhSmoke")
}
registerJmhTask("jmhShortestPath", false, "org.mitre.tdp.boogie.alg.chooser.ShortestPathBenchmark", "shortest-path.json").configure {
    mustRunAfter("jmh", "jmhSmoke", "jmhLinker")
}
