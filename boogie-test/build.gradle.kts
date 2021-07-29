dependencies {
    api(project(":boogie-core"))
    api(project(":boogie-routes"))
    api(project(":boogie-conformance"))
    api(project(":boogie-arinc"))

    val mockitoVersion: String by rootProject.extra
    implementation("org.mockito:mockito-core:$mockitoVersion")
    implementation("org.mockito:mockito-inline:$mockitoVersion")
}
