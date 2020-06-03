dependencies {
    api(project(":boogie-core"))
//    implementation("org.junit.jupiter:junit-jupiter")
    val mockitoVersion: String by rootProject.extra
    implementation("org.mockito:mockito-core:$mockitoVersion")
}
