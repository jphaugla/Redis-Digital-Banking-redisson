FROM maven:3.8.4-openjdk-8 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:9
COPY --from=build /usr/src/app/target/redis-0.0.1-SNAPSHOT.jar /usr/app/redis-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/redis-0.0.1-SNAPSHOT.jar"]
