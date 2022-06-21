FROM openjdk:8
COPY ./serviceAccountKey.json ./serviceAccountKey.json
ADD target/rtre-docker.jar rtre-docker.jar
EXPOSE 3030
ENTRYPOINT ["java", "-jar", "/rtre-docker.jar"]
