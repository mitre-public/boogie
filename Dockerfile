FROM gradle:6.9.0-jdk8 AS base

WORKDIR /usr/share/ca-certificates

ADD http://pki.mitre.org/MITRE%20BA%20Root.crt http://pki.mitre.org/MITRE%20BA%20NPE%20CA-1.crt \
    http://pki.mitre.org/MITRE%20BA%20NPE%20CA-3.crt http://pki.mitre.org/MITRE%20BA%20NPE%20CA-4.crt ./

RUN for cert in "MITRE BA Root.crt" "MITRE BA NPE CA-1.crt" "MITRE BA NPE CA-3.crt" "MITRE BA NPE CA-4.crt"; \
    do keytool -import -alias "${cert}" -file "${cert}" -keystore "$JAVA_HOME/jre/lib/security/cacerts" -storepass changeit -noprompt; done

FROM base AS gradle-files

# grab everything from the source directory
# remove everything that isn't a relevant gradle.kts file containing dependencies
WORKDIR /boogie
COPY . .
RUN find . \! -name "*.gradle.kts" -mindepth 2 -maxdepth 2 -print | xargs rm -rf

FROM base AS dependencies

# resolve and build all the app dependencies into a directory - as a side effect this builds the gradle cache
WORKDIR /boogie
COPY --from=gradle-files /boogie .
RUN gradle --no-daemon :boogie-rest:copyDeps

# Transfer sources
COPY . .

FROM dependencies AS build

# transfer all the source code again to build the final jar containing the boogie source (dependencies have already been cached)
WORKDIR /boogie

# build the boogie-rest shadowjar and then rename it to not contain the version and leave it in ./boogie-rest.jar
RUN BOOGIE_VERSION=$(gradle properties --no-daemon --console=plain -q | grep "^version:" | awk '{printf $2}') \
    && gradle --no-daemon :boogie-rest:shadowJar \
    && mv boogie-rest/build/libs/boogie-rest-$BOOGIE_VERSION-all.jar ./boogie-rest.jar

FROM openjdk:8-jre-slim AS production

WORKDIR /boogie

COPY --from=build /boogie/boogie-rest.jar .

EXPOSE 8087

CMD ["java", "-jar", "-Xmx5120m", "./boogie-rest.jar"]