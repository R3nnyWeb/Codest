FROM gradle:jdk19 AS build

COPY codest-user-api/src /app/codest-user-api/src
COPY codest-user-api/build.gradle.kts /app/codest-user-api/build.gradle.kts
COPY codest-shared/src  /app/codest-shared/src
COPY codest-shared/build.gradle.kts /app/codest-shared/build.gradle.kts
COPY codest-logger/src  /app/codest-logger/src
COPY codest-logger/build.gradle.kts /app/codest-logger/build.gradle.kts
COPY settings.gradle.kts /app/settings.gradle.kts
COPY gradle.properties /app/gradle.properties
WORKDIR /app
RUN gradle codest-user-api:distTar
RUN tar -xf codest-user-api/build/distributions/app.tar -C /opt

FROM openjdk:19-jdk
COPY --from=build /opt/app /opt/app
ENV CLASSPATH=/opt/app/lib/*
WORKDIR /opt/app/lib
CMD java $JAVA_OPTS r3nny.codest.user.api.AppKt