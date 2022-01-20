import groovy.lang.GroovyObject
import net.researchgate.release.GitAdapter
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.TestFilter
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import java.time.LocalDate
import java.time.format.DateTimeFormatter

inline val GroovyObject.gitConfig: GitAdapter.GitConfig get() = getProperty("git") as GitAdapter.GitConfig

val Project.mavenCredentials: Pair<String?, String?>
    get() {
        val mavenUser: String? by this
        val mavenPassword: String? by this

        if (mavenUser === null || mavenPassword === null) {
            logger.warn(
                "Gradle properties <mavenUser> and/or <mavenPassword> have not been defined. Please add these properties to your ~/.gradle/gradle.properties file"
            )
        }
        return Pair(mavenUser, mavenPassword)
    }

internal fun TestFilter.passIfNoTestsMatch() {
    isFailOnNoMatchingTests = false
}

internal fun CheckstyleExtension.doNotShowViolations() {
    isShowViolations = false
}

internal fun now(dateTimeFormatPattern: String): String {
    return DateTimeFormatter.ofPattern(dateTimeFormatPattern).format(LocalDate.now())
}

internal inline val Project.javaSourceSets: SourceSetContainer
    get() {
        return extensions.getByType<JavaPluginExtension>().sourceSets
    }

internal inline val Project.jacocoTasks: List<Task>
    get() {
        return tasks.matching { it.extensions.findByType<JacocoTaskExtension>() !== null }.toList()
    }

internal inline val FileCollection.isNotEmpty: Boolean get() = !isEmpty