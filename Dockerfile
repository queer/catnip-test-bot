FROM maven:3-jdk-8-slim

COPY . /app
WORKDIR /app
RUN mvn -B -q clean package

FROM openjdk:8-jdk-slim
COPY --from=0 /app/target/catnip*.jar /app/catnip.jar

ENTRYPOINT ["/usr/bin/java", "-Xms128M", "-Xmx3072M", "-jar", "/app/catnip.jar"]