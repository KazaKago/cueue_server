FROM eclipse-temurin:11-alpine

ENV APP_HOME /app

WORKDIR $APP_HOME
COPY ./ $APP_HOME/
RUN ./gradlew worker:installDist

CMD ["worker/build/install/worker/bin/worker"]
