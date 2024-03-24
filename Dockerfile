FROM gradle:7.4.0-jdk17-alpine AS build
RUN mkdir /workspace
WORKDIR /workspace
COPY settings.gradle build.gradle ./
COPY src ./src

RUN gradle assemble

WORKDIR /workspace/build/libs
EXPOSE 8080
ENTRYPOINT ["java","-jar","completable-0.0.1-SNAPSHOT.jar"]