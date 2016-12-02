package fi.livi.digitraffic.meri.domain.portnet;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class PortArea {
    @EmbeddedId
    private PortAreaKey portAreaKey;

    private String portAreaName;

    public String getPortAreaName() {
        return portAreaName;
    }

    public void setPortAreaName(final String portAreaName) {
        this.portAreaName = portAreaName;
    }

    public PortAreaKey getPortAreaKey() {
        return portAreaKey;
    }

    public void setPortAreaKey(final PortAreaKey portAreaKey) {
        this.portAreaKey = portAreaKey;
    }
}
