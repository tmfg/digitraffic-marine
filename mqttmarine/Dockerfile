FROM fedora:27

# copy config and repo keys and all
COPY config /mqtt/config

# install gnupg
#RUN apt-get update && apt-get -y install gnupg

RUN dnf -y install mosquitto-1.5.1-1.fc27 && \
    dnf --showduplicates list mosquitto && \
    mkdir -p /mqtt/config /mqtt/data /mqtt/log && \
    dnf clean all --enablerepo=\* && \
    rm -rf /var/cache/yum

#RUN apt-key add /mqtt/config/mosquitto-repo.gpg.key && \
#    cp /mqtt/config/mosquitto-stretch.list /etc/apt/sources.list.d/mosquitto-stretch.list && \
#    apt-get update && \
#    apt-cache madison mosquitto && \
#    apt-get -y install mosquitto=1.5-0mosquitto2 && \
#    rm -rf /var/lib/apt/lists/* && \
#    mkdir -p /mqtt/config /mqtt/data /mqtt/log

VOLUME ["/mqtt/config", "/mqtt/data", "/mqtt/log"]

EXPOSE 1883 9001

ENTRYPOINT ["/usr/sbin/mosquitto", "-c", "/mqtt/config/mosquitto.conf"]