plugins {
    id("project.lib-conventions")
}

dependencies {
    api(project(":boogie-core"))

    testImplementation(platform(rootProject.libs.junit.bom))
    testImplementation(rootProject.libs.bundles.test.tools)

    jmh(rootProject.libs.bundles.jmh)
}

jmh {
    warmupIterations.set(5)
}
