# ------------------------------------------------------------------
# MySQL Server
#
# To build this image, type: "docker build . -t mysql-server"
# To run this image, type: "docker run --rm -it mysql-server:latest"
# ------------------------------------------------------------------
FROM ubuntu:20.04
ARG DEBIAN_FRONTEND=noninteractive

LABEL Description="Mark Hotchkiss' MySQL Server for Docker"

RUN apt-get update && \
    apt-get -y install docker.io && \
    apt-get -y install mysql && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /home/develop

COPY . .

# Set my editor style to vi
RUN echo 'set -o vi' >> /root/.bashrc 
RUN set -o vi

