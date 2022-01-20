plugins {
    `java-platform`
    jacoco
    id("net.researchgate.release")
    id("boogie-parent.reporting-conventions")
    id("boogie-parent.javadoc-conventions")
}

/** Explicitly declare referenced/used repositories which host the artifact necessary to build the software */
allprojects {
    repositories {
        // mavenLocal() // only uncomment to test a build that depends on locally installed maven artifacts
        maven {
            name = "clojars"
            url = uri("https://repo.clojars.org/")
        }
        maven {
            name = "repo1"
            url = uri("https://repo1.maven.org/maven2/")
        }
        maven {
            name = "codev-artifactory"
            url = uri("https://repo.codev.mitre.org/artifactory/idaass-maven")
            mavenContent {
                releasesOnly()
            }
            credentials {
                val (mavenUser, mavenPassword) = mavenCredentials
                username = mavenUser
                password = mavenPassword
            }
        }
    }
}

release {
    preTagCommitMessage = "[Gradle] Bump to stable version "
    newVersionCommitMessage = "[Gradle] Bump to version "

    gitConfig.requireBranch = "main"
}

val mockitoVersion by extra("3.2.4")
subprojects {
    apply(plugin = "java-library")
    apply(plugin = "jacoco")
    apply(plugin = "boogie-module.testing-conventions")
    apply(plugin = "boogie-module.checkstyle-conventions")
    apply(plugin = "boogie-module.release-conventions")

    // declare dependencies used in every child module
    dependencies {
        val slf4jVersion = "1.7.25" // "implementation"("org.slf4j:slf4j-simple:1.7.7")
        "implementation"("org.slf4j:slf4j-api:$slf4jVersion")
        "implementation"("org.slf4j:slf4j-log4j12:$slf4jVersion")

        val junitVersion = "5.5.2"
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

        "testImplementation"("org.mockito:mockito-core:$mockitoVersion")
    }

    /**
     * configure javadoc
     */
    tasks.named<Javadoc>("javadoc") {
        (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:none", true)
    }

    /** create/publish source and javadoc jars */
    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }

        withSourcesJar()
        withJavadocJar()
    }
}
