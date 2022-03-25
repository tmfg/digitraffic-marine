package fi.livi.digitraffic.meri.mqtt;

public class MqttDataMessageV2 {
    private final String topic;
    private final Object data;

    public MqttDataMessageV2(final String topic, final Object data) {
        this.topic = topic;
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "DataMessage{topic: '" + topic + ", data: " + data + '}';
    }
}
