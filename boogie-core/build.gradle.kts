plugins {
    id("project.lib-conventions")
}

dependencies {
    api(libs.bundles.commons)
    api(libs.bundles.jgrapht)

    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)
}
