FROM openjdk:17
COPY target/course-service*.jar course-service.jar
ENTRYPOINT ["java", "-jar", "/course-service.jar"]