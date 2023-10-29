FROM openjdk:17-slim
ARG BUILD
ARG GIT_COMMIT_SHA

COPY app/build/libs/developer-app.jar /

WORKDIR /
ENV BUILD=$BUILD
ENV GIT_COMMIT_SHA=$GIT_COMMIT_SHA

ENTRYPOINT "java -jar developer-app.jar"