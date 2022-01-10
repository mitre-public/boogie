subprojects {
    apply(plugin = "java-library")
    apply(plugin = "jacoco")
}

/**
 * https://docs.gradle.org/6.4-rc-1/samples/sample_jvm_multi_project_with_code_coverage.html
 * task to gather code coverage from multiple subprojects
 * NOTE: the `JacocoReport` tasks do *not* depend on the `test` task by default. Meaning you have to ensure
 * that `test` (or other tasks generating code coverage) run before generating the report.
 */
tasks.register<JacocoReport>("codeCoverageReport") {

    // If a subproject applies the 'jacoco' plugin, add its result into the aggregate report
    subprojects {
        val subproject = this
        dependsOn(subproject.tasks.named("testUnit"))
        subproject.plugins.withType<JacocoPlugin>().configureEach {
            subproject.tasks.matching { it.extensions.findByType<JacocoTaskExtension>() != null }.configureEach {

                var javaPlugin = subproject.convention.getPlugin(JavaPluginConvention::class.java)
                var mainSourceSet = javaPlugin.sourceSets["main"]

                sourceSets(mainSourceSet)

                val testTask = this
                var testSourceSet = javaPlugin.sourceSets["test"]
                var execFileName = "${subproject.buildDir}/jacoco/${testTask.name}.exec"

                if (!testSourceSet.allJava.isEmpty && File(execFileName).exists()) {
                    logger.lifecycle("including ${subproject.name}: ${testTask.name} in full report")
                    executionData(testTask)
                } else {
                    //logger.lifecycle("excluding ${subproject.name}: ${testTask.name} in full report")
                }
            }
        }
    }
    reports {
        // xml is usually used to integrate code coverage with other tools like SonarQube, Coveralls or Codecov
        xml.isEnabled = true
        // HTML reports can be used to see code coverage without any external tools
        html.isEnabled = true
    }
}
val jacocoAggregateXmlReportFile = "$buildDir/reports/jacoco/codeCoverageReport/codeCoverageReport.xml"