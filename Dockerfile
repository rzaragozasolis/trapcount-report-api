FROM adoptopenjdk:11.0.10_9-jre-openj9-0.24.0-focal
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]