plugins {
    `java-platform`
    jacoco
    id("com.adarshr.test-logger") version "2.0.0"// https://github.com/radarsh/gradle-test-logger-plugin
    id("net.researchgate.release") version "2.8.1" // used to emulate mvn release: https://github.com/researchgate/gradle-release
}

val mavenUser: String? by project
val mavenPassword: String? by project

/** Explicitly declare referenced/used repositories which host the artifact necessary to build the software */
allprojects {
    repositories {
        // only uncomment to test a build that depends on locally installed maven artifacts
        // mavenLocal()
        maven {
            name = "clojars"
            url = uri("https://repo.clojars.org/")
        }
        maven {
            name = "repo1"
            url = uri("https://repo1.maven.org/maven2/")
        }
        maven {
            name = "codev-artifactory-releases"
            url = uri("https://repo.codev.mitre.org/artifactory/idaass-maven")
            mavenContent {
                releasesOnly()
            }
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
    }
}

/** Add gradle tasks for releasing a new version of the software to MITRE internal DALI */
apply(from = "./scripts/gradle/dali-releasing.gradle.kts")

release {
    preTagCommitMessage = "[Gradle] Bump to stable version "
    newVersionCommitMessage = "[Gradle] Bump to version "

    val git: net.researchgate.release.GitAdapter.GitConfig = getProperty("git") as net.researchgate.release.GitAdapter.GitConfig
    git.requireBranch = "main"
}

/** Add gradle tasks for deploying updated code quality and coverage reports to MITRE SONAR */
apply(from = "./scripts/gradle/build-reporting.gradle.kts")

/** Add gradle tasks for packaging javadocs */
apply(from = "./scripts/gradle/build-docs.gradle.kts")

val mockitoVersion by extra("3.2.4")


subprojects {
    apply(plugin = "java-library")
    apply(plugin = "checkstyle") //https://docs.gradle.org/current/userguide/checkstyle_plugin.html
    apply(plugin = "maven-publish")
    apply(plugin = "com.adarshr.test-logger")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // declare dependencies used in every child module
    dependencies {

        val slf4jVersion = "1.7.25"//"implementation"("org.slf4j:slf4j-simple:1.7.7")
        "implementation"("org.slf4j:slf4j-api:$slf4jVersion")
        "implementation"("org.slf4j:slf4j-log4j12:$slf4jVersion")

        val junitVersion = "5.5.2"
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

        "testImplementation"("org.mockito:mockito-core:$mockitoVersion")
    }

    testlogger {
        theme = com.adarshr.gradle.testlogger.theme.ThemeType.fromName("standard-parallel")
        showFullStackTraces = true
    }

    /**
     * configure checkstyle extension:
     * https://docs.gradle.org/current/dsl/org.gradle.api.plugins.quality.CheckstyleExtension.html
     */
    tasks.withType<Checkstyle>().configureEach {
        //  isIgnoreFailures = true
        configFile = file("${rootProject.rootDir}/config/checkstyle/checkstyle.xml")

        reports {
            xml.isEnabled = false
            html.isEnabled = true
            html.stylesheet = resources.text.fromFile("${rootProject.rootDir}/config/xsl/checkstyle-report-stylesheet.xml")
        }
    }
    configure<CheckstyleExtension> {
        setShowViolations(false) // don't want to clutter console; violations will be in html reports anyway
        toolVersion = "8.42" // lock down the version. checkstyle has frequent backwards-incompatible changes
    }
    tasks.get("checkstyleTest").enabled = false // for now, we won't inspect tests

    val integrationMaxHeap = "4096m"
    val integrationJvmArgs = "-Xss256k -Dfile.encoding=UTF-8".split(" ")

    /** Main test task should run all tests regardless of whether they're "INTEGRATION" or "UNIT" level. */
    tasks.named<Test>("test") {
        group = "verification"
        description = "Runs ALL tests regardless of tagged category within the project."
        useJUnitPlatform {
            includeEngines("junit-jupiter")
            excludeEngines("junit-vintage")
        }
        failFast = false

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
            isFailOnNoMatchingTests = false//allow modules to not have any tests
        }
    }

    /** Specific task for "integration-level" tests within the Boogie project - these tests can take longer, be larger, but should
     * be utilized sparingly. */
    tasks.register<Test>("testIntegration") {
        group = "verification"
        description = "Runs integration tests (tests named 'IT*') or tagged as 'INTEGRATION'"
        useJUnitPlatform {
            includeTags("INTEGRATION")
            includeEngines("junit-jupiter")
            excludeEngines("junit-vintage")
        }
        failFast = false
        maxHeapSize = integrationMaxHeap
        jvmArgs = integrationJvmArgs
    }

    /**
     * configure javadoc
     */
    tasks.named<Javadoc>("javadoc") {
        (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:none", true)
    }

    /** create/publish source and javadoc jars */
    configure<JavaPluginExtension> {
        withSourcesJar()
        withJavadocJar()
    }
}
