FROM openjdk:17-slim
ARG BUILD
ARG GIT_COMMIT_SHA

ADD app/build/distributions/spring-boot.tar /
WORKDIR /spring-boot
ENV BUILD=$BUILD
ENV GIT_COMMIT_SHA=$GIT_COMMIT_SHA

CMD [ "bin/spring-boot" ]