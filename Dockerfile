FROM gradle:7.1.1-jdk11 AS build

WORKDIR /home/gradle/src

# only dependancy files download for cache
COPY --chown=gradle:gradle build.gradle settings.gradle gradle ./
RUN gradle build --no-daemon > /dev/null 2>&1 || true

COPY --chown=gradle:gradle . ./
RUN gradle build --no-daemon -x test

FROM openjdk:11.0.11-jdk AS builder
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
RUN java -Djarmode=layertools -jar /app/spring-boot-application.jar extract

FROM openjdk:11.0.11-jdk
WORKDIR /appu
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./
ENTRYPOINT [ "java", "org.springframework.boot.loader.JarLauncher"]