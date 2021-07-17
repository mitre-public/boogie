plugins {
    id("dev.clojurephant.clojure") version "0.6.0"
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

    // needed for test integration
    testRuntimeOnly("org.ajoberstar:jovial:0.3.0")

    // dependencies for REPL use only
    devImplementation("org.clojure:tools.namespace:1.1.0")

    // Java dependencies for the boogie-arinc java submodule
    api(project(":boogie-core"))
    implementation("org.apache.commons:commons-lang3:3.10")
    implementation("com.ko-sys.av:airac:1.0.0")
    testImplementation(project(":boogie-test"))

    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.3")
}
