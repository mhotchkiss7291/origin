# ------------------------------------------------------------------
# Hello, World! for docker
#
# To build this image, type: "docker build . -t hello-world"
# To run this image, type: "docker run --rm -it hello-world:latest"
# ------------------------------------------------------------------
FROM ubuntu:20.04
ARG DEBIAN_FRONTEND=noninteractive

LABEL Description="Mark Hotchkiss' Hello, World for Docker"

RUN apt-get update && \
    apt-get -y install build-essential && \
    apt-get -y install passwd && \
    apt-get -y install vim && \
    apt-get -y install net-tools && \
    apt-get -y install iproute2 && \
    apt-get -y install openvpn-systemd-resolved && \
    apt-get -y install isc-dhcp-client && \
    apt-get -y install dnsutils && \
#    apt-get -y install gcc && \
#    apt-get -y install python3.8 && \
#    apt-get -y install openjdk-11-jdk && \
    apt-get -y install  libnss-ldapd && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /home/develop

COPY . .

# RUN g++ HelloWorld.cpp -o HelloWorld
