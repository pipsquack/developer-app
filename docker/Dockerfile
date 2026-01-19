FROM eclipse-temurin:17-alpine-3.22
ARG BUILD
ARG GIT_COMMIT_SHA
ARG GIT_URL
ARG GIT_BRANCH

COPY app/build/libs/developer-app.jar /

WORKDIR /
ENV DD_VERSION=0.1.$BUILD
ENV DD_GIT_COMMIT_SHA=$GIT_COMMIT_SHA
ENV DD_GIT_REPOSITORY_URL=$GIT_URL

### Temporary until DI support the env vars above
ENV DD_TAGS=git.commit.sha:$GIT_COMMIT_SHA,git.repository_url:$GIT_URL

ENTRYPOINT java -jar developer-app.jar