FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8080

ENV TOMCAT_VERSION 11.0.9
RUN apk add --no-cache curl unzip bash && \
    curl -O https://dlcdn.apache.org/tomcat/tomcat-11/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.zip && \
    unzip apache-tomcat-${TOMCAT_VERSION}.zip && \
    mv apache-tomcat-${TOMCAT_VERSION} tomcat && \
    rm apache-tomcat-${TOMCAT_VERSION}.zip && \
    chmod +x tomcat/bin/*.sh # Ensure all scripts are executable

RUN rm -rf tomcat/webapps/*

COPY target /app/target
RUN mv /app/target/boisson-1.0-SNAPSHOT.war tomcat/webapps/ROOT.war

CMD ["tomcat/bin/catalina.sh", "run"]
