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

# build the boogie-rest shadowjar and then rename it to not contain the version and leave it in ./boogie-rest.jar
RUN BOOGIE_VERSION=$(gradle properties --no-daemon --console=plain -q | grep "^version:" | awk '{printf $2}') \
    && gradle --no-daemon :boogie-rest:shadowJar -PmavenUser=$MAVEN_USER -PmavenPassword=$MAVEN_PASSWORD \
    && mv boogie-rest/build/libs/boogie-rest-$BOOGIE_VERSION-all.jar ./boogie-rest.jar

FROM openjdk:11-jre-slim AS production

WORKDIR /boogie

COPY --from=build /boogie/boogie-rest.jar .

EXPOSE 8087

CMD ["java", "-jar", "-Xmx5120m", "./boogie-rest.jar"]