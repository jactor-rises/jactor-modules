FROM openjdk:18-slim

LABEL jactor-rises="https://github.com/jactor-rises" \
      email="tor.egil.jacobsen@gmail.com"

COPY build/libs/persistence-*-SNAPSHOT.jar /usr/src/myapp/app.jar
WORKDIR /usr/src/myapp
EXPOSE 1099

CMD java -jar app.jar
