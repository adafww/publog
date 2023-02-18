FROM ubuntu:20.04 AS builder

RUN apt-get update && apt-get install -y maven

ENV APP_HOME=/root/dev/app/

WORKDIR $APP_HOME

COPY pom.xml $APP_HOME

COPY src/ $APP_HOME/src/

RUN mvn clean package -DskipTests

FROM openjdk:19

COPY --from=builder /root/dev/app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]