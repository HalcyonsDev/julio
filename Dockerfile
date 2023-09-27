FROM openjdk:17-jdk

WORKDIR /app

COPY target/julio-0.0.1-SNAPSHOT.jar /app/julio.jar

EXPOSE 8080

CMD ["java", "-jar", "julio.jar"]