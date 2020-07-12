dependencies {
    api(project(":boogie-routes"))
    implementation("org.jblas:jblas:1.2.4")
    implementation("ch.hsr:geohash:1.1.0")
    testImplementation(project(":boogie-test"))
}
