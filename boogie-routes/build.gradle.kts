dependencies {
    api(project(":boogie-core"))

    val mockitoVersion: String by rootProject.extra
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
}
