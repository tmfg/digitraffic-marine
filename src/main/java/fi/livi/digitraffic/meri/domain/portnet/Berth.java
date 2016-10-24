package fi.livi.digitraffic.meri.domain.portnet;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class Berth {
    @EmbeddedId
    private BerthKey berthKey;

    private String berthName;

    public String getBerthName() {
        return berthName;
    }

    public void setBerthName(String berthName) {
        this.berthName = berthName;
    }

    public BerthKey getBerthKey() {
        return berthKey;
    }

    public void setBerthKey(BerthKey berthKey) {
        this.berthKey = berthKey;
    }
}
