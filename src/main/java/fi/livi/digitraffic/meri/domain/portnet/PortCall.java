package fi.livi.digitraffic.meri.domain.portnet;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class PortCall {
    @Id
    private long portCallId;

    private String portToVisit;
    private String prevPort;
    private String nextPort;

    public long getPortCallId() {
        return portCallId;
    }

    public void setPortCallId(long portCallId) {
        this.portCallId = portCallId;
    }

    public String getPortToVisit() {
        return portToVisit;
    }

    public void setPortToVisit(String portToVisit) {
        this.portToVisit = portToVisit;
    }

    public String getPrevPort() {
        return prevPort;
    }

    public void setPrevPort(String prevPort) {
        this.prevPort = prevPort;
    }

    public String getNextPort() {
        return nextPort;
    }

    public void setNextPort(String nextPort) {
        this.nextPort = nextPort;
    }
}
