FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

EXPOSE 8005
ADD ./target/microservicio-items-0.0.1-SNAPSHOT.jar microservicio-items.jar

ENTRYPOINT [ "java", "-jar", "microservicio-items.jar" ]