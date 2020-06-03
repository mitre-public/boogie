plugins {
    id("com.adarshr.test-logger") version "2.0.0" apply false
    id("org.sonarqube") version "2.7.1"
    id("com.bmuschko.clover") version "2.2.5"
    // checkstyle
}


///**
// * code coverage and reporting configuration
// */
//if (project.hasProperty("clover")) {
//    allprojects {
//        apply(plugin = "com.bmuschko.clover")
//        dependencies {
//            clover("org.openclover:clover:4.3.1")
//        }
//    }
//}

//if (project.hasProperty("sonar")) {
//    val sonarReportPath = "$buildDir/reports/clover/clover.xml"
//    allprojects {
//        sonarqube {
//            properties {
//                property("sonar.clover.reportPath", sonarReportPath)
//            }
//        }
//    }
//}

/**
 * import repo configs
 */
apply(from = "build-repos.gradle.kts")

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

        val mockitoVersion = "2.21.0"
        "testImplementation"("org.mockito:mockito-core:$mockitoVersion")
    }
    /* TESTING  */
    val testLargeJvmargs: String by project
    val testLargeHeapMax: String by project

    /** configure main test task to ignore desired tags and integration tests by default */
    tasks.named<Test>("test") {
        group = "verification"
        description = "Runs all unit tests"
        useJUnitPlatform { excludeTags("CLUSTER", "IGNORE") }
        filter {
            excludeTestsMatching("IT*")
            isFailOnNoMatchingTests = false//allow modules to not have any tests
        }
        failFast = true
        jvmArgs = testLargeJvmargs.split(" ")
        maxHeapSize = testLargeHeapMax

    }

    /** test task to run ONLY small unit tests */
    tasks.register<Test>("testSmall") {
        group = "verification"
        description = "Runs SMALL (or untagged) tests only."
        useJUnitPlatform { excludeTags("CLUSTER", "LARGE") }
        filter {
            excludeTestsMatching("IT*")
            isFailOnNoMatchingTests = false//allow modules to not have any tests
        }
        failFast = true
    }
    /** test task to run ONLY medium and large unit tests  */
    tasks.register<Test>("testLarge") {
        group = "verification"
        description = "Runs LARGE (and MEDIUM) tagged tests only."
        useJUnitPlatform {
            includeTags("LARGE")
        }
        failFast = true
        jvmArgs = testLargeJvmargs.split(" ")
        maxHeapSize = testLargeHeapMax
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
