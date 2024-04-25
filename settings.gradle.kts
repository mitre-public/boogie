rootProject.name = "boogie"

include(":boogie-core")
include(":boogie-arinc")
include("boogie-dafif")
include(":boogie-routes")
include(":boogie-conformance")

pluginManagement {
    repositories {
        maven { name = "gradle-plugin-portal" }
        maven { name = "caasd-plugin-portal" }
    }
    plugins {
        id("caasd.repositories") version "1.1.+"
        id("caasd.publish") version "1.0.+"
        id("caasd.auto-semver") version "1.0.+"
        id("me.champeau.jmh") version "0.7.2"
    }
    includeBuild("build-logic")
}