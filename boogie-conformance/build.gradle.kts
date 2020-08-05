dependencies {
    api(project(":boogie-routes"))
    api("org.jgrapht:jgrapht-core:1.3.0")
    api("org.jgrapht:jgrapht-io:1.3.0")
    implementation("org.jblas:jblas:1.2.4")
    implementation("ch.hsr:geohash:1.1.0")
    testImplementation(project(":boogie-test"))
}
