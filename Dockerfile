FROM gradle:6.6-jre14 AS build
COPY --chown=gradle:gradle build.gradle /timesheet-parser/build.gradle
COPY --chown=gradle:gradle ./src /timesheet-parser/src
WORKDIR /timesheet-parser
RUN gradle build --no-daemon

FROM openjdk:14-jdk-alpine
COPY --from=build /timesheet-parser/build/libs/*.jar /app/timesheet-parser-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/app/timesheet-parser-0.0.1-SNAPSHOT.jar"]
