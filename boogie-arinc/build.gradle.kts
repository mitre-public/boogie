plugins {
    id("project.lib-conventions")
}

dependencies {
    api(project(":boogie-core"))
    implementation("com.ko-sys.av:airac:1.0.0")

    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)
}
