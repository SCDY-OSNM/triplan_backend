FROM openjdk:17-jdk-slim
WORKDIR /app

# 환경변수 정의
ARG TRIPLAN_EUREKA_URL


# 환경변수 설정
ENV TRIPLAN_EUREKA_URL=${TRIPLAN_EUREKA_URL}


COPY build/libs/eureka-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]