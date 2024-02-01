FROM amazoncorretto:11
LABEL authors="Ola-la"
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9191
COPY target/*.jar stats-client.jar
ENTRYPOINT ["java","-jar","/stats-client.jar"]