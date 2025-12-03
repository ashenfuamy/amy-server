FROM registry.cn-hangzhou.aliyuncs.com/zxhysite/jdk17-ffpmeg:latest
LABEL authors="ashen"

COPY build/libs/*.jar /app.jar

RUN mkdir /amy \
    mkdir /amy/poster \
    mkdir /amy/video_1 \
    mkdir /amy/archive

ENV TZ=Asia/Shanghai


EXPOSE 8081

ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","/app.jar", "--spring.profiles.active=prod"]