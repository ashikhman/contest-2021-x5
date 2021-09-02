FROM openjdk:11

RUN mkdir /app
WORKDIR /app

COPY build/libs/x5-0.0.1-SNAPSHOT.jar /app/psplayer.jar

EXPOSE 9081

ENTRYPOINT ["java", "-jar", "/app/psplayer.jar"]
