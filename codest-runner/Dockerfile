FROM gradle:jdk19 AS build

COPY codest-runner/src /app/codest-runner/src
COPY codest-runner/build.gradle.kts /app/codest-runner/build.gradle.kts
COPY codest-shared/src  /app/codest-shared/src
COPY codest-shared/build.gradle.kts /app/codest-shared/build.gradle.kts
COPY codest-logger/src  /app/codest-logger/src
COPY codest-logger/build.gradle.kts /app/codest-logger/build.gradle.kts
COPY settings.gradle.kts /app/settings.gradle.kts
COPY gradle.properties /app/gradle.properties
WORKDIR /app
RUN gradle codest-runner:distTar
RUN tar -xf codest-runner/build/distributions/app.tar -C /opt

FROM openjdk:19-jdk

RUN  microdnf  install -y python3
RUN  microdnf install gcc python3-devel
RUN python3 -m pip install psutil

COPY --from=build /opt/app /opt/app
ENV CLASSPATH=/opt/app/lib/*
WORKDIR /opt/app/lib
CMD java $JAVA_OPTS r3nny.codest.runner.AppKt