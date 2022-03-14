# ------------------------------------------------------------------
# Ansible controller 
# ------------------------------------------------------------------
FROM ubuntu:20.04
ARG DEBIAN_FRONTEND=noninteractive

LABEL Description="Mark Hotchkiss' Ansible Controller"

RUN apt-get update && \
    apt-get -y install build-essential && \
    apt-get -y install passwd && \
    apt-get -y install vim && \
    apt-get -y install tree && \
    apt-get -y install net-tools && \
    apt-get -y install iproute2 && \
    apt-get -y install openvpn-systemd-resolved && \
    apt-get -y install openssh-server && \
    apt-get -y install ssh && \
    apt-get -y install isc-dhcp-client && \
    apt-get -y install dnsutils && \
    apt-get -y install iputils-ping && \
    apt-get -y install cron && \
    apt-get -y install at && \
    apt-get -y install ldap-utils && \
    apt-get -y install slapd && \
    apt-get -y install ufw && \
    apt-get -y install iptables && \
    apt-get -y install gcc && \
    apt-get -y install curl && \
#    apt-get -y install python3.8 && \
#    apt-get -y install openjdk-11-jdk && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /home/develop

COPY . .

# Set my editor style to vi
RUN echo 'set -o vi' >> /root/.bashrc 
RUN set -o vi

# Open for root login of ssh
RUN echo 'PermitRootLogin yes' >> /etc/ssh/sshd_config
EXPOSE 22
EXPOSE 80
