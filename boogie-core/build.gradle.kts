dependencies {
    api("org.mitre.caasd:commons:0.0.41") {
        exclude("com.github.davidmoten", "rtree")
        exclude("com.github.davidmoten", "guava-mini")
        exclude("com.github.davidmoten", "rxjava-extras")
    }

    api("org.jgrapht:jgrapht-core:1.3.0")
    api("org.jgrapht:jgrapht-io:1.3.0")

    implementation("com.google.guava:guava:23.0")

    testImplementation("org.apache.commons:commons-lang3:3.9")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.3")

    val mockitoVersion: String by rootProject.extra
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
}
