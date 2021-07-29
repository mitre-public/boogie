dependencies {
    api(project(":boogie-core"))

    implementation("com.google.guava:guava:23.0")
    implementation("org.apache.commons:commons-lang3:3.10")
    implementation("com.ko-sys.av:airac:1.0.0")

    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.3")

    val mockitoVersion: String by rootProject.extra
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
}
