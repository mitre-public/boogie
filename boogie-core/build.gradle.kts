dependencies {
    api("org.mitre.caasd:commons:0.0.27")
    implementation("com.google.guava:guava:23.0")

    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
}

tasks.withType<Checkstyle>().configureEach {
    exclude("**/Geomagnetics.java")
}