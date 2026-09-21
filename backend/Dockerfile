# Multi-stage build: this Maven build runs on Render's build servers, which
# have normal internet access - so this is also where the real dependency
# resolution and Spring context wiring actually gets verified, since the
# sandbox this project was built in has Maven Central blocked at the network
# level (see README's "Notes and limits" section).
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Warms the local repo cache from just the pom first, so editing source files
# later doesn't force a full re-download on every rebuild.
RUN mvn -q -B dependency:go-offline || true
COPY src ./src
RUN mvn -q -B -DskipTests package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render's free plan gives 512MB total to the container. The JVM itself
# (metaspace, thread stacks, code cache) eats a meaningful chunk of that
# before any application heap, so the heap is capped well under 512MB and a
# smaller per-thread stack size keeps a burst of concurrent requests from
# blowing the container's memory limit.
ENV JAVA_OPTS="-Xmx350m -Xss512k -XX:+UseSerialGC -XX:MaxMetaspaceSize=96m"

# Render sets $PORT at runtime; application.yml reads it via server.port=${PORT:8081}.
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
