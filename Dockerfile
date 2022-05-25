FROM openjdk:11
EXPOSE 8080
ADD target/foundationapp.jar foundationapp.jar
ENTRYPOINT ["java","-jar","/foundationapp.jar"]

