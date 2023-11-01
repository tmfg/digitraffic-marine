package fi.livi.digitraffic.meri.model.winternavigation;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class WinterNavigationDirwayPointPK implements Serializable {

    @Column(name = "dirway_name", nullable = false)
    private String dirwayName;

    @Column(name = "order_number", nullable = false)
    private Long orderNumber;

    public WinterNavigationDirwayPointPK() {
    }

    public WinterNavigationDirwayPointPK(final String dirwayName, final long orderNumber) {
        this.dirwayName = dirwayName;
        this.orderNumber = orderNumber;
    }

    public String getDirwayName() {
        return dirwayName;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final WinterNavigationDirwayPointPK that = (WinterNavigationDirwayPointPK) o;

        return new EqualsBuilder()
            .append(dirwayName, that.dirwayName)
            .append(orderNumber, that.orderNumber)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(dirwayName)
            .append(orderNumber)
            .toHashCode();
    }
}
