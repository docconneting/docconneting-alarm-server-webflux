# 1. Java 17 slim 이미지를 사용
FROM openjdk:17-jdk-slim

# 2. JAR 파일을 컨테이너에 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app.jar

# 3. 실행 시 prod 프로파일을 활성화하고, config 경로를 명시
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "-Dspring.config.additional-location=optional:file:/app/config/", "/app.jar"]
