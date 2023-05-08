pluginManagement {
    repositories {
        maven { name = "gradle-plugin-portal" }
        maven { name = "caasd-plugin-portal" }
    }
    plugins {
        id("caasd.repositories") version "1.1.+"
    }
}