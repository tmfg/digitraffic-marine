package fi.livi.digitraffic.meri.domain.winternavigation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
public class ShipActivityPK implements Serializable {

    @Column(name = "vessel_pk", nullable = false)
    private String vesselPK;

    @Column(nullable = false)
    private Integer orderNumber;

    public ShipActivityPK() {
    }

    public ShipActivityPK(String vesselPK, Integer orderNumber) {
        this.vesselPK = vesselPK;
        this.orderNumber = orderNumber;
    }

    public String getVesselPK() {
        return vesselPK;
    }

    public void setVesselPK(String vesselPK) {
        this.vesselPK = vesselPK;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ShipActivityPK that = (ShipActivityPK) o;

        return new EqualsBuilder()
            .append(vesselPK, that.vesselPK)
            .append(orderNumber, that.orderNumber)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(vesselPK)
            .append(orderNumber)
            .toHashCode();
    }
}
