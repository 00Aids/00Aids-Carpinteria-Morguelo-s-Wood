FROM openjdk:17
COPY "./target/TallerMecanica-1.jar" "app.jar"
EXPOSE 8022
ENTRYPOINT [ "java", "-jar", "app.jar" ]