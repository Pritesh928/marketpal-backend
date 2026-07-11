# docker file for deployment of the pre build image of the backend instead of a springboot application with uneven dependencies.
FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . .

RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

CMD ["java","-jar","target/springboot-auth-backend-0.0.1-SNAPSHOT.jar"]   