# Stage 1: Build with Gradle
FROM gradle:8.7.0-jdk21-alpine AS builder
WORKDIR /app

# Cache Gradle dependencies
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build application
RUN ./gradlew clean bootJar --no-daemon -Pprod

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy built JAR
COPY --from=builder /app/build/libs/app.jar app.jar

# Add non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Configure environment
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

# Optimized JVM options
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]