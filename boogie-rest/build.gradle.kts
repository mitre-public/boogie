plugins {
    id("dev.clojurephant.clojure") version "0.6.0"
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
    // allows ./gradlew :boogie-arinc:build :boogie-arinc:taskTree - to print the tree of tasks for a given target task
    // the above prints the tree for the :boogie-arinc:build task and outputs the tree to CLI
    id("com.dorongold.task-tree") version "2.1.0"
}

application {
    // current supported method for setting the main class a la later gradle versions
    mainClass.set("boogie.server")

    // clojurephant/shadow kind of old so we have to include this override - otherwise they barf even though its deprecated
    mainClassName = "boogie.server"
}

// the clojure code needs to be AOT compiled to end up as actually .class files (instead of .clj) files in the final
// compiled boogie-arinc jar - by default clojurephant <i>doesnt</i> AOT compile any files in the clj source set so we
// explicitly call it out here for the main build
clojure {
    builds.named("main") {
        // exclude the check on the server namespace - its simple, small, and adds a bit of time to tests since its standing
        // up a ring server during the check... lol
        setCheckNamespaces(listOf("boogie.routes", "boogie.arinc.cycles", "boogie.arinc.latest", "boogie.routes.expand"))
        aotAll()
    }
}

// what are my sourceSets names :sweatstiny:
tasks.register("printSourceSets") {
    doLast {
        sourceSets.forEach { srcSet ->
            println("[" + srcSet.name + "]")
            print("-->Source directories: " + srcSet.allJava.srcDirs + "\n")
            print("-->Output directories: " + srcSet.output.classesDirs.files + "\n")
        }
    }
}

dependencies {
    // Clojure dependencies for the thin REST API wrapper around the Boogie software
    implementation("org.clojure:clojure:1.10.1")
    implementation("compojure:compojure:1.6.1")
    implementation("cheshire:cheshire:5.10.0")

    implementation("ring:ring:1.9.3")
    implementation("ring:ring-defaults:0.3.2")
    implementation("ring-cors:ring-cors:0.1.13")
    implementation("ring-logger:ring-logger:1.0.1")
    implementation("metosin:reitit:0.5.12")

    implementation("amalloy:ring-gzip-middleware:0.1.4")

    implementation("com.taoensso:timbre:4.10.0")
    implementation("org.clojure:core.cache:1.0.207")
    implementation("org.clojure:data.avl:0.1.0")

    // needed for test integration
    testRuntimeOnly("org.ajoberstar:jovial:0.3.0")

    // dependencies for REPL use only
    devImplementation("org.clojure:tools.namespace:1.1.0")

    api(project(":boogie-core"))
    api(project(":boogie-routes"))
    api(project(":boogie-conformance"))
    api(project(":boogie-arinc"))

    implementation("com.ko-sys.av:airac:1.0.0")
}