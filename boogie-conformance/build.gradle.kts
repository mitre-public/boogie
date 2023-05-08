plugins {
    id("project.lib-conventions")
}

dependencies {
    api(project(":boogie-routes"))

    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)
}