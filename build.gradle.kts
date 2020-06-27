plugins {
    `java-platform`
    jacoco
    id("com.adarshr.test-logger") version "2.0.0" apply false
    id("com.bmuschko.clover") version "2.2.5"
    id("net.researchgate.release") version "2.8.1" // used to emulate mvn release: https://github.com/researchgate/gradle-release
}

val gradleScriptsDir = "./gradle-scripts/"

/**
 * code coverage and reporting configuration
 */
if (project.hasProperty("clover")) {
    allprojects {
        apply(plugin = "com.bmuschko.clover")
        dependencies {
            clover("org.openclover:clover:4.3.1")
        }
    }
}

val mockitoVersion by extra("3.2.4")

/* import/apply our repo and dependencies declared in other scripts for modularity */
apply(from = "${gradleScriptsDir}build-repos.gradle.kts")

/* documentation tasks */
apply(from = "${gradleScriptsDir}build-docs.gradle.kts")

//only apply jacoco/sonar reporting settings if explicitly running
if (project.hasProperty("reporting")) {
    apply(from = "${gradleScriptsDir}build-reporting.gradle.kts")
}

release {
    preTagCommitMessage = "[Gradle] Bump to stable version "
    newVersionCommitMessage = "[Gradle] Bump to version "
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "checkstyle") //https://docs.gradle.org/current/userguide/checkstyle_plugin.html
    apply(plugin = "maven-publish")
    apply(plugin = "com.adarshr.test-logger")

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
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

    /** configure main test task to ignore desired tags and integration tests by default */
    tasks.named<Test>("test") {
        group = "verification"
        description = "Runs all unit tests"

        useJUnitPlatform { }
        filter {
            excludeTestsMatching("IT*")
            isFailOnNoMatchingTests = false//allow modules to not have any tests
        }
        failFast = true
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
            html.stylesheet = resources.text.fromFile("${rootProject.rootDir}/config/xsl/checksytle-report-sylesheet.xml")
        }
    }
}
