plugins {
    checkstyle // https://docs.gradle.org/current/userguide/checkstyle_plugin.html
}

/**
 * configure checkstyle extension:
 * https://docs.gradle.org/current/dsl/org.gradle.api.plugins.quality.CheckstyleExtension.html
 */
tasks.withType<Checkstyle>().configureEach {
    configFile = rootDir.resolve("config/checkstyle/checkstyle.xml")
    reports {
        xml.required.set(false)
        html.required.set(true)
        html.stylesheet = resources.text.fromFile(rootDir.resolve("config/xsl/checkstyle-report-stylesheet.xml"))
    }
}

configure<CheckstyleExtension> {
    doNotShowViolations() // don't want to clutter console; violations will be in html reports anyway
    toolVersion = "8.42" // lock down the version. checkstyle has frequent backwards-incompatible changes
}

tasks.getByName("checkstyleTest").enabled = false // for now, we won't inspect tests