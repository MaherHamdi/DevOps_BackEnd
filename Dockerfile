FROM openjdk:11
EXPOSE 8082
COPY target/devops-project.jar devops-project.jar
ENTRYPOINT ["java","-jar","/devops-project.jar"]