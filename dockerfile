FROM openjdk:8u111-jre-alpine
EXPOSE 8080
COPY build/libs/user-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]