FROM openjdk:21-jdk
WORKDIR /movie_api
COPY MovieApiApplication.jar MovieApiApplication.jar
ENTRYPOINT ["java", "-jar", "MovieApiApplication.jar"]