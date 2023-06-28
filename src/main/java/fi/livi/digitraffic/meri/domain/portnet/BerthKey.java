package fi.livi.digitraffic.meri.domain.portnet;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class BerthKey implements Serializable{
    private String locode;
    private String portAreaCode;
    private String berthCode;

    public String getLocode() {
        return locode;
    }

    public void setLocode(final String locode) {
        this.locode = locode;
    }

    public String getPortAreaCode() {
        return portAreaCode;
    }

    public void setPortAreaCode(final String portAreaCode) {
        this.portAreaCode = portAreaCode;
    }

    public String getBerthCode() {
        return berthCode;
    }

    public void setBerthCode(final String berthCode) {
        this.berthCode = berthCode;
    }

    public static BerthKey of(final String locode, final String portAreaCode, final String berthCode) {
        final BerthKey key = new BerthKey();

        key.setLocode(locode);
        key.setPortAreaCode(portAreaCode);
        key.setBerthCode(berthCode);

        return key;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BerthKey berthKey = (BerthKey) o;

        if (!locode.equals(berthKey.locode))
            return false;
        if (!portAreaCode.equals(berthKey.portAreaCode))
            return false;
        return berthCode.equals(berthKey.berthCode);

    }

    @Override
    public int hashCode() {
        int result = locode.hashCode();
        result = 31 * result + portAreaCode.hashCode();
        result = 31 * result + berthCode.hashCode();
        return result;
    }
}
