FROM gradle:7.5.1-jdk11 AS build

# transfer all the source code again to build the final jar containing the boogie source (dependencies have already been cached)
WORKDIR /boogie

COPY . .

# build the jar and then rename it to not contain the version and leave it in ./boogie.jar
# note this task will also run checkstyle, code coverage, and tests
RUN --mount=type=secret,id=gradle.properties,dst=/root/.gradle/gradle.properties API_VERSION=$(gradle properties --no-daemon --console=plain -q | grep "^version:" | awk '{printf $2}') \
    && gradle --no-daemon bootJar \
    && mv boogie-rest/build/libs/boogie-rest-$API_VERSION.jar ./boogie.jar

FROM openjdk:11-jre-slim AS production

WORKDIR /boogie

# override the java opts for JVM on launch if provided this is typically used with
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

# this is the port that spring (by default) exposes the REST controller on
EXPOSE 8080
COPY --from=build /boogie/boogie.jar .
CMD java $JAVA_OPTS -jar ./boogie.jar