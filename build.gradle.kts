plugins {
    `java-platform`
    jacoco
    id("com.adarshr.test-logger") version "2.0.0" apply false
    id("net.researchgate.release") version "2.8.1" // used to emulate mvn release: https://github.com/researchgate/gradle-release
}

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
            name = "dali-mirror"
            url = uri("https://dali.mitre.org/nexus/content/groups/mirror")
            content {
                excludeGroup("org.jacoco")// dali's: mirror/org/jacoco doesn't have all the agent jar's, just runtime which breaks the build
            }
        }
        maven {
            name = "dali-proxied-repositories"
            url = uri("https://dali.mitre.org/nexus/content/groups/proxied-repositories")
        }
        maven {
            name = "dali-external"
            url = uri("https://dali.mitre.org/nexus/content/groups/external")
        }
        maven {
            name = "mitre-caasd-releases"
            url = uri("https://dali.mitre.org/nexus/content/repositories/mitre-caasd-releases/")
        }
        maven {
            name = "dali-mitre-caasd-releases"
            url = uri("https://dali.mitre.org/nexus/content/groups/mitre-caasd")
        }
    }
}

/** Add gradle tasks for releasing a new version of the software to MITRE internal DALI */
apply(from = "./scripts/gradle/dali-releasing.gradle.kts")

val mavenUser: String? by project
val mavenPassword: String? by project

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

        failFast = true

        useJUnitPlatform {
            excludeTags("INTEGRATION")
            includeEngines("junit-jupiter")
            excludeEngines("junit-vintage")
        }

        filter {
            excludeTestsMatching("IT*")
            isFailOnNoMatchingTests = false//allow modules to not have any tests
        }
    }

    tasks.register<Test>("testIntegration") {
        group = "verification"
        description = "Runs integration tests (tests named 'IT*')"
        useJUnitPlatform {
            includeTags("INTEGRATION")
            includeEngines("junit-jupiter")
            excludeEngines("junit-vintage")
        }
        failFast = true
        maxHeapSize = "4096m"

        jvmArgs = "-Xss256k -Dfile.encoding=UTF-8".split(" ")
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
            html.stylesheet = resources.text.fromFile("${rootProject.rootDir}/config/xsl/checkstyle-report-stylesheet.xml")
        }
    }
}
