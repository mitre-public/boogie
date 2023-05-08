plugins {
    id("project.lib-conventions")
}

dependencies {
    api(project(":boogie-core"))
    api(project(":boogie-routes"))
    api(project(":boogie-conformance"))
    api(project(":boogie-arinc"))

    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)
}
