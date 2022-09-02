FROM eclipse-temurin:18-alpine

ENV APP_HOME /app

WORKDIR $APP_HOME
COPY ./ $APP_HOME/
RUN ./gradlew installDist

CMD ["server/build/install/server/bin/server"]
