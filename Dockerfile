FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# JMX Exporter
ADD https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.20.0/jmx_prometheus_javaagent-0.20.0.jar /jmx.jar
COPY jmx-config.yaml /jmx-config.yaml

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8084 9404

ENTRYPOINT ["java","-javaagent:/jmx.jar=9404:/jmx-config.yaml","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=70","-XX:MetaspaceSize=128m","-XX:MaxMetaspaceSize=256m","-XX:MaxDirectMemorySize=128m","-XX:+ExitOnOutOfMemoryError","-Xlog:gc*,metaspace","-jar","app.jar"]

