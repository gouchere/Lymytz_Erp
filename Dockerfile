FROM ubuntu:20.04
LABEL authors="Yves G"
# Définit les variables d'environnement
ENV JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk
ENV GLASSFISH_HOME=/usr/local/glassfish-3.1.2
ENV PATH=$PATH$:$JAVA_HOME:$GLASSFISH_HOME/bin

USER root

RUN apt-get update && \
    apt-get install -y curl unzip zip inotify-tools openjdk-8-jdk inotify-tools && \
    rm -rf /val/libs/apt/lists/*

# Download install glassfish
RUN curl -L -o /tmp/glassfish-3.1.2.zip  http://download.java.net/glassfish/3.1.2/release/glassfish-3.1.2.zip  && \
    unzip /tmp/glassfish-3.1.2.zip -d /usr/local && \
    rm -rf /tmp/glassfish-3.1.2.zip \

RUN ECHO 'PATH content ... '$PATH
EXPOSE 8080 4848 8181
WORKDIR /usr/local/glassfish
ENTRYPOINT ["asadmin", "start-domain", "--verbose"]