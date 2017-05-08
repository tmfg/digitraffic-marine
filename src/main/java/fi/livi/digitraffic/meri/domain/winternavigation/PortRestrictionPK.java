package fi.livi.digitraffic.meri.domain.winternavigation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Embeddable
public class PortRestrictionPK implements Serializable {

    @Column(name = "locode", nullable = false)
    private char[] locode;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;

    public PortRestrictionPK() {
    }

    public PortRestrictionPK(String locode, Integer orderNumber) {
        this.locode = locode.toCharArray();
        this.orderNumber = orderNumber;
    }

    public char[] getLocode() {
        return locode;
    }

    public void setLocode(char[] locode) {
        this.locode = locode;
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

        PortRestrictionPK that = (PortRestrictionPK) o;

        return new EqualsBuilder()
            .append(locode, that.locode)
            .append(orderNumber, that.orderNumber)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(locode)
            .append(orderNumber)
            .toHashCode();
    }
}
