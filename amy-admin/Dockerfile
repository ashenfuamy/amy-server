FROM eclipse-temurin:21.0.9_10-jdk-ubi9-minimal
LABEL authors="ashen"

COPY build/libs/amy-admin.jar /app.jar

RUN mkdir /amy
RUN mkdir /amy/poster /amy/avatar /amy/archive /amy/video_1 /amy/public

ENV TZ=Asia/Shanghai

EXPOSE 8085

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","/app.jar", "--spring.profiles.active=prod"]