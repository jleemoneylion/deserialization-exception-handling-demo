# Create image using Amazon Corretto 17 base image
FROM amazoncorretto:17

# Copy the jar file named poison-pill-example-0.0.1-SNAPSHOT.jar from the target folder to the container as app.jar
COPY target/poison-pill-example-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]

