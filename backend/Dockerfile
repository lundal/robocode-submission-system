# Create builder image
FROM openjdk:8u171-jdk-alpine3.8 as builder
WORKDIR /tmp/app

# Download dependencies
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw verify --fail-never

# Build app
COPY src src
RUN ./mvnw package

# Create app image
FROM openjdk:8u171-jdk-alpine3.8
COPY --from=builder /tmp/app/target/robocode-0.1.0.jar /opt/app.jar
EXPOSE 9001
CMD java -jar /opt/app.jar
