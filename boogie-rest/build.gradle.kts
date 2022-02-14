plugins {
    application
    // No shadowJar plugins needed - Spring auto-shades the jar as part of the standard jar task - which is a bit weird
    // but lets them hijack it to set up all the things they need for their auto-wiring to work
    id("org.springframework.boot") version "2.6.3"
}

dependencies {
    // instead of using the dependency plugin import the BOM
    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.6.3"))

    api(project(":boogie-arinc"))
    api(project(":boogie-routes"))

    implementation("org.mitre.tdp:tentacular-core:0.0.6")

    /** Spring dependency to host a REST API */
    implementation("org.springframework.boot:spring-boot-starter-data-rest") {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }

    /* Monitoring dependencies */
    implementation("org.springframework.boot:spring-boot-starter-actuator") {
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    /* Swagger dependencies */
    implementation("org.springdoc:springdoc-openapi-ui:1.6.5")

    /** Logging dependencies */
    implementation("org.springframework.boot:spring-boot-starter-log4j2")

    /** Testing dependencies */
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
        exclude("org.springframework.boot", "spring-boot-starter-logging")
    }
}

// we don't need zip or tar archives
tasks.getByName<Zip>("distZip").enabled = false
tasks.getByName<Tar>("distTar").enabled = false
tasks.getByName<Zip>("bootDistZip").enabled = false
tasks.getByName<Tar>("bootDistTar").enabled = false

/** Set the main-class entry point */
application {
    mainClass.set("org.mitre.tdp.boogie.BoogieApplication")
}

configure<PublishingExtension> {
    publications {
        // append the bootJar to the existing publication
        getByName<MavenPublication>(project.name) {
            artifact(tasks.getByName("bootJar"))
        }
    }
}