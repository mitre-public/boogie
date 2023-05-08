rootProject.name = "boogie"

include(":boogie-core")
include(":boogie-arinc")
include(":boogie-routes")
include(":boogie-conformance")
include(":boogie-test")

pluginManagement {
    repositories {
        maven { name = "gradle-plugin-portal" }
        maven { name = "caasd-plugin-portal" }
    }
    plugins {
        id("caasd.repositories") version "1.1.+"
        id("caasd.publish") version "1.0.+"
        id("caasd.auto-semver") version "1.0.+"
    }
    includeBuild("build-logic")
}