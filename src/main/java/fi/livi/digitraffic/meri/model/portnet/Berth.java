package fi.livi.digitraffic.meri.model.portnet;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
@DynamicUpdate
public class Berth {
    @EmbeddedId
    private BerthKey berthKey;

    private String berthName;

    public String getBerthName() {
        return berthName;
    }

    public void setBerthName(final String berthName) {
        this.berthName = berthName;
    }

    public BerthKey getBerthKey() {
        return berthKey;
    }

    public void setBerthKey(final BerthKey berthKey) {
        this.berthKey = berthKey;
    }
}
