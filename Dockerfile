#
# Dependency build stage
# common-library dependency will be built and installed into the local repo in the container
#
FROM maven:3.9.9-eclipse-temurin-21 AS common_builder
WORKDIR /app/common-library
COPY common-library/pom.xml .
COPY common-library/src ./src
RUN mvn clean install -DskipTests



#
# Build stage
#
FROM maven:3.9.9-eclipse-temurin-21 AS cart_builder
WORKDIR /app/cart-service
COPY cart-service/pom.xml .
COPY cart-service/src ./src
COPY --from=common_builder /root/.m2 /root/.m2
RUN mvn clean package -DskipTests



#
# Package stage
#
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=cart_builder /app/cart-service/target/*.jar app.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "app.jar"]