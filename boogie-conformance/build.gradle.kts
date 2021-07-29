dependencies {
    api(project(":boogie-routes"))
    api("org.jgrapht:jgrapht-core:1.3.0")
    api("org.jgrapht:jgrapht-io:1.3.0")
    implementation("org.jblas:jblas:1.2.4")
    implementation("ch.hsr:geohash:1.1.0")

    val mockitoVersion: String by rootProject.extra
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
}
