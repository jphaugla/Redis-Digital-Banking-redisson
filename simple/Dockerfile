FROM maven:3.8.4-openjdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:11
ENV DEBIAN_FRONTEND noninteractive
COPY --from=build /usr/src/app/target/redissonStress-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/app/redissonStress-1.0-SNAPSHOT-jar-with-dependencies.jar
COPY --from=build /usr/src/app/src/main/resources/runredisson.sh /usr/app/runredisson.sh
ENTRYPOINT ["/usr/app/runredisson.sh"]
