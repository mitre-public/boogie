plugins {
    id("java-conventions")
}

dependencies {
    implementation(project(":boogie-core"))
    implementation(project(":boogie-util"))
    implementation(libs.bundles.commons)

    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)
    testImplementation(libs.bundles.jgrapht)
}

