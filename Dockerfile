FROM openjdk:11-slim-buster

LABEL org.opencontainers.image.source=https://github.com/isaqueChaves/maven-repository

#Install gcsfuse, Google bucket's driver.
RUN apt-get update -y
RUN apt-get install curl fuse gnupg -y
RUN curl -L -O https://github.com/GoogleCloudPlatform/gcsfuse/releases/download/v0.30.0/gcsfuse_0.30.0_amd64.deb
RUN dpkg --install gcsfuse_0.30.0_amd64.deb
RUN apt-get remove -y curl --purge && apt-get autoremove -y && rm -rf /var/lib/apt/lists/*

RUN groupadd -g 1000 java && useradd -rm -d /home/java -g java -u 1000 java
USER java
WORKDIR /home/java
COPY target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]