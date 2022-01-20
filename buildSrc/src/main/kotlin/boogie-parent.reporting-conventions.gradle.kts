plugins {
    jacoco
}

tasks.register<JacocoReport>("codeCoverageReport") {
    group = "verification"

    dependsOn(*subprojects.map { it.tasks.getByName("testUnit") }.toTypedArray())
    sourceSets(*subprojects.map { it.javaSourceSets["main"] }.toTypedArray())

    val allJacocoTasks = subprojects
        .filter { it.javaSourceSets["test"].allJava.isNotEmpty }
        .flatMap { subproject ->
            val validJacocoTasks = subproject.jacocoTasks
                .filter { task ->
                    subproject.buildDir.resolve("jacoco/${task.name}.exec").exists()
                }
            validJacocoTasks.forEach { task ->
                logger.lifecycle("including ${subproject.name}: ${task.name} in full report")
            }

            validJacocoTasks
        }

    executionData(*allJacocoTasks.toTypedArray())

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}