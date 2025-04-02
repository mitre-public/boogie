plugins {
    `kotlin-dsl`
}

val ghprToken: String? by project

repositories {
    gradlePluginPortal()
    configureGitHubRepo("mitre-public", "*")
}

fun RepositoryHandler.configureGitHubRepo(org: String, repo: String) {
    maven {
        name = "${org}-${repo}"
        url = uri("https://maven.pkg.github.com/${org}/${repo}")
        credentials(HttpHeaderCredentials::class) { name = "Authorization"; value = "Bearer ${ghprToken}" }
        authentication { create<HttpHeaderAuthentication>("header") }
    }
}