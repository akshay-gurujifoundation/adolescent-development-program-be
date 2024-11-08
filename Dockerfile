FROM openjdk:17
LABEL maintainer="akshay.pokharkar@gurujifoundation.in"
ADD  ./target/Adolescent-Development-Program-0.0.1.jar /opt/Adolescent-Development-Program-0.0.1.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/Adolescent-Development-Program-0.0.1.jar"]
