FROM eclipse-mosquitto

COPY config/mosquitto.conf /mqtt/config/mosquitto.conf
COPY config/mosquitto.passwd /mqtt/config/mosquitto.passwd
COPY config/mosquitto.acl /mqtt/config/mosquitto.acl

RUN mosquitto_passwd -b /mqtt/config/mosquitto.passwd updater updater