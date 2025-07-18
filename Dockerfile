# Use an official OpenJDK 17 runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Expose the port the app runs on
EXPOSE 8080

# Download and extract Tomcat
ENV TOMCAT_VERSION 11.0.9
RUN apk add --no-cache curl unzip bash && \
    curl -O https://dlcdn.apache.org/tomcat/tomcat-11/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.zip && \
    unzip apache-tomcat-${TOMCAT_VERSION}.zip && \
    mv apache-tomcat-${TOMCAT_VERSION} tomcat && \
    rm apache-tomcat-${TOMCAT_VERSION}.zip && \
    chmod +x tomcat/bin/*.sh # Ensure all scripts are executable

# Remove default webapps
RUN rm -rf tomcat/webapps/*

# Copy the entire target directory and then move the WAR file
COPY target /app/target
RUN mv /app/target/examgestionboisson-1.0-SNAPSHOT.war tomcat/webapps/ROOT.war

# Start Tomcat
CMD ["tomcat/bin/catalina.sh", "run"]
