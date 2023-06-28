package fi.livi.digitraffic.meri.domain.portnet;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class PortAreaKey implements Serializable{
    private String locode;
    private String portAreaCode;

    public static PortAreaKey of(final String locode, final String portAreaCode) {
        final PortAreaKey key = new PortAreaKey();

        key.setLocode(locode);
        key.setPortAreaCode(portAreaCode);

        return key;

    }

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

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PortAreaKey that = (PortAreaKey) o;

        if (!locode.equals(that.locode))
            return false;
        return portAreaCode.equals(that.portAreaCode);

    }

    @Override
    public int hashCode() {
        int result = locode.hashCode();
        result = 31 * result + portAreaCode.hashCode();
        return result;
    }

    @Override public String toString() {
        return "PortAreaKey{" +
                "locode='" + locode + '\'' +
                ", portAreaCode='" + portAreaCode + '\'' +
                '}';
    }
}
