FROM gradle:7.3.3-jdk11 AS base

WORKDIR /certs

ADD http://pki.mitre.org/MITRE%20BA%20ROOT.crt http://pki.mitre.org/MITRE%20BA%20NPE%20CA-1.crt \
    http://pki.mitre.org/MITRE%20BA%20NPE%20CA-3.crt http://pki.mitre.org/MITRE%20BA%20NPE%20CA-4.crt \
    http://pki.mitre.org/ZScaler_Root.crt /certs/

RUN for cert in "MITRE BA ROOT.crt" "MITRE BA NPE CA-1.crt" "MITRE BA NPE CA-3.crt" "MITRE BA NPE CA-4.crt" "ZScaler_Root.crt"; \
    do keytool -import -alias "${cert}" -file "${cert}" -keystore "$JAVA_HOME/lib/security/cacerts" -storepass changeit -noprompt; done

FROM base AS build

# Injectible mavenUser and mavenPassword for use in building the image within the container
ARG MAVEN_USER
ARG MAVEN_PASSWORD

# transfer all the source code again to build the final jar containing the boogie source (dependencies have already been cached)
WORKDIR /boogie

COPY . .

# build the jar and then rename it to not contain the version and leave it in ./boogie.jar
# note this task will also run checkstyle, code coverage, and tests
RUN API_VERSION=$(gradle properties -PmavenUser=$MAVEN_USER -PmavenPassword=$MAVEN_PASSWORD --no-daemon --console=plain -q | grep "^version:" | awk '{printf $2}') \
    && gradle --no-daemon bootJar -PmavenUser=$MAVEN_USER -PmavenPassword=$MAVEN_PASSWORD \
    && mv boogie-rest/build/libs/boogie-rest-$API_VERSION.jar ./boogie.jar

FROM openjdk:11-jre-slim AS production

WORKDIR /boogie
# this is the port that spring (by default) exposes the REST controller on
EXPOSE 8080
COPY --from=build /boogie/boogie.jar .
CMD ["java", "-jar", "./boogie.jar"]