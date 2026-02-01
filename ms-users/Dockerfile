ARG MS_NAME=ms-users
# FROM openjdk:17-jdk-alpine as builder - Deprecado
FROM eclipse-temurin:17-jdk-alpine as builder

ARG MS_NAME

WORKDIR /app/$MS_NAME

COPY ./pom.xml /app
COPY ./ms-users/.mvn ./.mvn
COPY ./ms-users/mvnw .
COPY ./ms-users/pom.xml .


RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline

COPY ./$MS_NAME/src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

ARG MS_NAME

WORKDIR /app
RUN mkdir ./logs
ARG TARGET_FOLDER=/app/$MS_NAME/target

COPY --from=builder $TARGET_FOLDER/$MS_NAME-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8001
ENV PORT $PORT_APP
EXPOSE $PORT

CMD ["java", "-jar", "ms-users-0.0.1-SNAPSHOT.jar"]