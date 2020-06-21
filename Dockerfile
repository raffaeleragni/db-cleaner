FROM openjdk:13

RUN mkdir /app && mkdir /app/lib
WORKDIR /app

EXPOSE 8080
CMD java \
  -XX:MaxRAMPercentage=80 \
  -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=127.0.0.1:9999 \
  -jar app.jar config.properties

COPY target/lib/* /app/lib/

COPY target/*.jar /app/app.jar

COPY configuration.properties /app/config.properties