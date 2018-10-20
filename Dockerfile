FROM maven:3

COPY . /app
WORKDIR /app
RUN mvn -B -q clean package

FROM openjdk:10-jre-slim
COPY --from=0 /app/target/catnip*.jar /app/catnip.jar

ENTRYPOINT ["/usr/bin/java", "-Xms128M", "-Xmx8192M", "-jar", "/app/catnip.jar"]