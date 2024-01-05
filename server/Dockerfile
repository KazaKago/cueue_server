FROM eclipse-temurin:21-alpine

ENV APP_HOME /app

WORKDIR $APP_HOME
COPY ./ $APP_HOME/
RUN ./gradlew server:installDist

CMD ["server/build/install/server/bin/server"]
