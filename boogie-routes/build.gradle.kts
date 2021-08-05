dependencies {
    api(project(":boogie-core"))

    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.3")

    val mockitoVersion: String by rootProject.extra
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
}
