FROM gradle:jdk17-alpine AS build
WORKDIR /app
ARG CONTAINER_PORT
COPY build.gradle /app
RUN gradle --refresh-dependencies
COPY . /app
RUN gradle build -x test

FROM openjdk:17-alpine
COPY --from=build /app/build/libs/unigrab-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE ${CONTAINER_PORT}
CMD ["java", "-jar", "/app.jar"]
