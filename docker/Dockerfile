FROM openjdk:17-slim
ARG BUILD
ARG GIT_COMMIT_SHA
ARG GIT_URL

COPY app/build/libs/developer-app.jar /

WORKDIR /
ENV DD_VERSION=0.1.$BUILD
ENV DD_TAGS="git.commit.sha:$GIT_COMMIT_SHA git.repository_url:$GIT_URL"

ENTRYPOINT java -jar developer-app.jar