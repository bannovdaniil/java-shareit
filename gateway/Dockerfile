FROM amazoncorretto:11-alpine-jdk
COPY target/shareit-gateway*.jar shareit_gateway.jar
ENTRYPOINT ["java","-jar","/shareit_gateway.jar"] 
