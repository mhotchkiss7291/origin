# ------------------------------------------------------------------
# Hello, World! for docker
#
# To build this image, type: "docker build . -t hello-world"
# To run this image, type: "docker run --rm -it hello-world:latest"
# ------------------------------------------------------------------
FROM ubuntu:20.04

LABEL Description="Mark Hotchkiss' Hello, World for Docker"

RUN apt-get update && \
    apt-get -y install build-essential && \
    apt-get -y install gcc && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /home/develop

COPY . .

RUN g++ main.cpp -o main
