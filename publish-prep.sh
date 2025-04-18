!/bin/bash

# 1st argument should be the module, e.g., "core" or "arinc"
# 2nd argument should be the version number

# build the project
./gradlew clean :boogie-${1}:build -Pversion=${2}

# prep boogie-${1}
./gradlew generatePomFileForBoogie-${1}Publication
mkdir boogie-${1}/build/maven-central
cp -r boogie-${1}/build/libs/* boogie-${1}/build/maven-central
cp boogie-${1}/build/publications/boogie-${1}/pom-default.xml boogie-${1}/build/maven-central/boogie-${1}-${2}.pom
gpg -ab boogie-${1}/build/maven-central/boogie-${1}-${2}.pom
gpg -ab boogie-${1}/build/maven-central/boogie-${1}-${2}.jar
gpg -ab boogie-${1}/build/maven-central/boogie-${1}-${2}-javadoc.jar
gpg -ab boogie-${1}/build/maven-central/boogie-${1}-${2}-sources.jar
jar -cvf boogie-${1}/build/maven-central/bundle.jar \
  boogie-${1}/build/maven-central/boogie-${1}-${2}-javadoc.jar \
  boogie-${1}/build/maven-central/boogie-${1}-${2}-javadoc.jar.asc \
  boogie-${1}/build/maven-central/boogie-${1}-${2}-sources.jar \
  boogie-${1}/build/maven-central/boogie-${1}-${2}-sources.jar.asc \
  boogie-${1}/build/maven-central/boogie-${1}-${2}.jar \
  boogie-${1}/build/maven-central/boogie-${1}-${2}.jar.asc \
  boogie-${1}/build/maven-central/boogie-${1}-${2}.pom \
  boogie-${1}/build/maven-central/boogie-${1}-${2}.pom.asc
