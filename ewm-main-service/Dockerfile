FROM amazoncorretto:11
LABEL authors="Ola-la"
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081
COPY target/*.jar ewm-main-service.jar
ENTRYPOINT ["java","-jar","/ewm-main-service.jar"]