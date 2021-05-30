FROM openjdk:16.0.1

LABEL jactor-rises="https://github.com/jactor-rises" \
      email="tor.egil.jacobsen@gmail.com"

COPY target/jactor-persistence-*-app.jar /usr/src/myapp/app.jar
WORKDIR /usr/src/myapp
EXPOSE 1099

CMD java -jar app.jar
