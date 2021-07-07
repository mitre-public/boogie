dependencies {
    api(project(":boogie-core"))

    val mockitoVersion: String by rootProject.extra
    implementation("org.mockito:mockito-core:$mockitoVersion")
    implementation("org.mockito:mockito-inline:$mockitoVersion")
}
