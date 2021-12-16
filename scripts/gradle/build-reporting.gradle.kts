subprojects {
    apply(plugin = "java-library")
    apply(plugin = "jacoco")
}

buildscript {
    repositories {
        //use our proxied Gradle Plugin Portal (https://plugins.gradle.org/) to find this plugin
        maven(url = "https://dali.mitre.org/nexus/content/repositories/proxy-gradle-plugin-portal/")
    }
    dependencies {
        val sonarqubePluginVersion = "3.0" // matches sonarqube.mitre.org version
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:$sonarqubePluginVersion")
    }
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

/**
 * configure the sonarqube extension to allow us to publish code reports to caasd-sonar
 */
//since not in main build script, need to apply plugin via fully qualified classname, rather than plugin-id
apply<org.sonarqube.gradle.SonarQubePlugin>()    //apply("org.sonarqube")
configure<org.sonarqube.gradle.SonarQubeExtension> {
    properties {
        properties(mapOf(
            "sonar.coverage.jacoco.xmlReportPaths" to jacocoAggregateXmlReportFile,
            "sonar.projectKey" to "boogie",
            "sonar.host.url" to "https://sonarqube.mitre.org",
            "sonar.login" to "679804ed9a3029d0f5c800f432581eb4dac9be4b"
        ))
    }
}
