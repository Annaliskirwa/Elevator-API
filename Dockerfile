FROM registry.access.redhat.com/ubi8/openjdk-17:latest

LABEL maintainer="AKirwa@kcbgroup.com"

ENV PORT 8080

COPY target/*.jar /opt/app.jar

WORKDIR /opt

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar