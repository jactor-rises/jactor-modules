FROM openjdk:17

LABEL jactor-rises="https://github.com/jactor-rises" \
      email="tor.egil.jacobsen@gmail.com"

COPY build/libs/jactor-persistence-*-SNAPSHOT.jar /usr/src/myapp/app.jar
WORKDIR /usr/src/myapp
EXPOSE 1099

CMD java -jar app.jar
