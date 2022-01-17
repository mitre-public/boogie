### Boogie Scripts

This directory contains a collection of scripts (both `bash` and `kotlin`) which are imported into the main Boogie gradle build 
and used from CI to control the general integration testing pipeline and code coverage/quality reporting.

### Kotlin scripts

These scripts exist to modify and append additional tasks or other functionality to the baseline Boogie gradle build. The included 
scripts work as follows:

| Script | Description |
|:-------|:------------|
| [Build docs](https://github.com/mitre-tdp/boogie/blob/main/scripts/gradle/build-docs.gradle.kts) | Adds a gradle task which builds and zips the javadocs for all of the methods and classes in the repo. |
| [Build reporting](https://github.com/mitre-tdp/boogie/blob/main/scripts/gradle/build-reporting.gradle.kts) | Adds a gradle task which can be used to parse the junit output of run tests and upload it to [sonar]() so it can be scanned and introspected. |
| [Artifactory releasing](https://github.com/mitre-tdp/boogie/blob/main/scripts/gradle/artifactory-releasing.gradle.kts) | Adds a release task which can be used to automatically bump the repo to a release version, release to Codev Artifactory, and then update to a new snapshot and publish that as well. |

### Bash scripts 

The various CI bash scripts are called in differing stages of the overall [CI process](https://github.com/mitre-tdp/boogie/tree/main/.github/workflows) 
running on [bamboo](https://pandafood.mitre.org/browse/TTFS-SHIM). Generally speaking they work as follows:

| Script | Parameters | Description |
|:-------|:-----------|:------------|