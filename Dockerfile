FROM gradle:jdk17 AS builder
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . /home/gradle/src
RUN gradle clean build

FROM eclipse-temurin:17 AS layers
WORKDIR layer
ARG JAR_FILE=/home/gradle/src/build/libs/uad-[0-9].[0-9].[0-9].jar
COPY --from=builder ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17-jre

WORKDIR /opt/app
RUN addgroup --system uadapp && adduser --system --shell /usr/sbin/nologin --ingroup uadapp uadapp
COPY --from=layers /layer/dependencies/ ./
COPY --from=layers /layer/spring-boot-loader/ ./
COPY --from=layers /layer/snapshot-dependencies/ ./
COPY --from=layers /layer/application/ ./
RUN chown -R uadapp:uadapp /opt
USER uadapp
HEALTHCHECK --interval=30s --timeout=3s --retries=1 CMD wget -qO- http://localhost:8080/actuator/health/ | grep UP || exit 1
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher", "--spring.profiles.active=prod"]
