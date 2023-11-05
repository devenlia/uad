FROM gradle:jdk17 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . /home/gradle/src
RUN gradle clean build

FROM openjdk:17-jdk
ARG JAR_FILE=/home/gradle/src/build/libs/uad-[0-9].[0-9].[0-9].jar
COPY --from=build ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]