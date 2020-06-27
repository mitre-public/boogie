val javadocDir = "$buildDir/docs/java-doc"
val javadocZipDir = "$buildDir/dist"
fun javadocZipName(): String {
    var dateString = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd").format(java.time.LocalDate.now())
    return "${dateString}-cda-$version.zip"
}
val javadocZipPath = "$javadocZipDir/${javadocZipName()}"


/** uber javadoc aggregation */
val javadocBuild by tasks.registering(Javadoc::class) {
    description = "Generate uber Javadocs containing sources from all modules"
    title = "${project.name} $version API"
    group = "javadoc"
    setDestinationDir(file(javadocDir))

    val customOptions = options as StandardJavadocDocletOptions
    customOptions.addBooleanOption("Xdoclint:none", true)
    customOptions.addBooleanOption("quiet", true)
    customOptions.addBooleanOption("linksource", true)
    customOptions.addStringOption("sourcepath", "")//hacky workaround for bug: https://github.com/gradle/gradle/issues/5630
    //add every module's sources and classpath to this uber configuration
    subprojects.forEach { child ->
        val childTask = child.tasks.getByName<Javadoc>("javadoc")
        source = source.plus(childTask.source)
        classpath = classpath.plus(childTask.classpath)
    }
}

val javadocPackage by tasks.registering(Zip::class) {
    dependsOn(javadocBuild)
    description = "Zip the javadocs into a single file"
    group = "javadoc"

    from(javadocDir)
    destinationDirectory.set(file(javadocZipDir))
    archiveFileName.set(javadocZipName())
}