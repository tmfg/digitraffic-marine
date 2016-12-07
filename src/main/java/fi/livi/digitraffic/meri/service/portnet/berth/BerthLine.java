package fi.livi.digitraffic.meri.service.portnet.berth;

public class BerthLine {
    public final String portCode;
    public final String portName;
    public final String portAreaCode;
    public final String portAreaName;
    public final String berthCode;
    public final String berthName;

    public BerthLine(String portCode, String portName, String portAreaCode, String portAreaName, String berthCode, String berthName) {
        this.portCode = portCode;
        this.portName = portName;
        this.portAreaCode = portAreaCode;
        this.portAreaName = portAreaName;
        this.berthCode = berthCode;
        this.berthName = berthName;
    }
}
