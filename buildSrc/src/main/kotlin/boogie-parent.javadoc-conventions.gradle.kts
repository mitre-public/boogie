val javadocBuild by tasks.registering(Javadoc::class) {
    description = "Generate uber Javadocs containing sources from all modules"
    title = "${project.name} $version API"
    group = "javadoc"
    setDestinationDir(buildDir.resolve("docs/java-doc"))

    val customOptions = options as StandardJavadocDocletOptions
    customOptions.addBooleanOption("Xdoclint:none", true)
    customOptions.addBooleanOption("quiet", true)
    customOptions.addBooleanOption("linksource", true)
    customOptions.addStringOption("sourcepath", "") // hacky workaround for bug: https://github.com/gradle/gradle/issues/5630

    val subTasks = subprojects.map { it.tasks.getByName<Javadoc>("javadoc") }
    subTasks.forEach {
        source += it.source
        classpath += it.classpath
    }
}

val javadocPackage by tasks.registering(Zip::class) {
    description = "Zip the javadocs into a single file"
    group = "javadoc"
    dependsOn(javadocBuild)

    from(buildDir.resolve("docs/java-doc"))
    destinationDirectory.set(buildDir.resolve("dist"))

    val javadocZipName = "${now("yyyy-MM-dd")}-boogie-${project.version}.zip"
    archiveFileName.set(javadocZipName)
}