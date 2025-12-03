FROM registry.cn-hangzhou.aliyuncs.com/zxhysite/jdk17-ffpmeg:latest
LABEL authors="ashen"

COPY build/libs/*.jar /app.jar

RUN mkdir /amy
RUN mkdir /amy/poster /amy/avatar /amy/archive /amy/video_1

ENV TZ=Asia/Shanghai

EXPOSE 8081

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","/app.jar", "--spring.profiles.active=prod"]