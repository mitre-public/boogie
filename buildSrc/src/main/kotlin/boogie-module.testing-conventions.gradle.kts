import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    id("com.adarshr.test-logger")
}

testlogger {
    theme = ThemeType.STANDARD_PARALLEL
    showFullStackTraces = true
}

val integrationMaxHeap = "4096m"
val integrationJvmArgs = "-Xss256k -Dfile.encoding=UTF-8".split(" ")

/** Main test task should run all tests regardless of whether they're "INTEGRATION" or "UNIT" level. */
tasks.named<Test>("test") {
    group = "verification"
    description = "Runs ALL tests regardless of tagged category within the project."

    failFast = false

    useJUnitPlatform {
        includeEngines("junit-jupiter")
        excludeEngines("junit-vintage")
    }

    // need to inherit JVM
    maxHeapSize = integrationMaxHeap
    jvmArgs = integrationJvmArgs
}

/** Specific task for "unit-level" tests within the Boogie project - these tests should be short, sweet, small and mostly
 * comprehensive. */
tasks.register<Test>("testUnit") {
    group = "verification"
    description = "Runs all UNIT (or untagged) tests"

    failFast = true

    useJUnitPlatform {
        excludeTags("INTEGRATION")
        includeEngines("junit-jupiter")
        excludeEngines("junit-vintage")
    }

    testlogger {
        slowThreshold = 1000 // log in red if exceeds 1 sec
        showSummary = false
    }

    filter {
        excludeTestsMatching("IT*")
        passIfNoTestsMatch()
    }
}

/** Specific task for "integration-level" tests within the Boogie project - these tests can take longer, be larger, but should
 * be utilized sparingly. */
tasks.register<Test>("testIntegration") {
    group = "verification"
    description = "Runs integration tests (tests named 'IT*') or tagged as 'INTEGRATION'"

    failFast = false

    useJUnitPlatform {
        includeTags("INTEGRATION")
        includeEngines("junit-jupiter")
        excludeEngines("junit-vintage")
    }

    maxHeapSize = integrationMaxHeap
    jvmArgs = integrationJvmArgs
}
