plugins {
    id("java-conventions")
}

dependencies {
    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)
    testImplementation(libs.bundles.jgrapht)
}