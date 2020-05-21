import org.mitre.cda.core.LevArtifact

/**
 * registers a new task which creates a 'caasd.properties' file in
 * each project's resources/META-INF folder, which will by default get included in all jars
 */


buildscript {
    dependencies {
        val cdaVersion: String by project.extra//grab version set in our dependencies script
        classpath("org.mitre.cda:cda-core:$cdaVersion")
        classpath("ru.concerteza.buildnumber:maven-jgit-buildnumber-plugin:1.2.10")
    }
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        maven {
            name = "dali-mitre-caasd-releases"
            url = uri("https://dali.mitre.org/nexus/content/groups/mitre-caasd")
        }
        maven {
            name = "mitre-caasd-releases"//match repo name in ~/.m2/settings.xml
            url = uri("https://dali.mitre.org/nexus/content/repositories/mitre-caasd-releases/")
        }
    }
}


fun gitBuildInfo(): ru.concerteza.util.buildnumber.BuildNumber {
    return ru.concerteza.util.buildnumber.BuildNumberExtractor.extract(rootProject.projectDir)
}


subprojects {
    tasks.register("caasdProps") {
        val propsDirPath = "${project.projectDir}/src/main/resources/META-INF/"
        val propsDirFile = File(propsDirPath)
        if (!propsDirFile.exists())
            propsDirFile.mkdirs()
        val propsFile = File("$propsDirPath/caasd.properties")
        if (propsFile.exists())
            propsFile.delete()
        propsFile.createNewFile()


        val props = java.util.Properties()
        props.setProperty("caasd.project.groupId", "${project.group}")
        props.setProperty("caasd.project.artifactId", rootProject.name)
        props.setProperty("caasd.project.version", "${project.version}")
        props.setProperty(LevArtifact.CDA_DATA_RELEASE_VERSION, "${project.version}")

        val timeFormat = "yyyy-MM-dd HH:mm Z"
        props.setProperty("maven.build.timestamp.format", timeFormat)
        props.setProperty("build.timestamp.format", timeFormat)

        val buildInstant = java.time.Instant.now()
        val localDateTime = java.time.ZonedDateTime.ofInstant(buildInstant, java.time.ZoneOffset.UTC)
        val formattedTimestamp = localDateTime.format(java.time.format.DateTimeFormatter.ofPattern(timeFormat))
        props.setProperty("caasd.project.timestamp", formattedTimestamp)

        val userName = System.getProperty("user.name") ?: "UNKNOWN"
        props.setProperty("caasd.project.builtBy", userName)

        val gitInfo = gitBuildInfo()
        props.setProperty("git.revision", gitInfo.revision)
        props.setProperty("git.commitDate", gitInfo.commitDate)
        props.setProperty("git.buildnumber", gitInfo.defaultBuildnumber())
        props.setProperty("git.shortRevision", gitInfo.shortRevision)
        props.setProperty("git.commitsCount", gitInfo.commitsCountAsString)
        props.setProperty("git.branch", gitInfo.branch)
        props.setProperty("git.tag", gitInfo.tag)
        props.setProperty("git.parent", gitInfo.parent)

        val outputStream = java.io.FileOutputStream(propsFile)
        props.store(outputStream, null)
    }
}
