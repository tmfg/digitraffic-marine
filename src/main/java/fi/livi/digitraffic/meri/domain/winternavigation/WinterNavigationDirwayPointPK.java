package fi.livi.digitraffic.meri.domain.winternavigation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
public class WinterNavigationDirwayPointPK implements Serializable {

    @Column(name = "dirway_name", nullable = false)
    private String dirwayName;

    @Column(name = "order_number", nullable = false)
    private Long orderNumber;

    public WinterNavigationDirwayPointPK() {
    }

    public WinterNavigationDirwayPointPK(String dirwayName, long orderNumber) {
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
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        WinterNavigationDirwayPointPK that = (WinterNavigationDirwayPointPK) o;

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
