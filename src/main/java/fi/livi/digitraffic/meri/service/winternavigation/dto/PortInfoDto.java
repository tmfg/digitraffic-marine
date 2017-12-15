package fi.livi.digitraffic.meri.service.winternavigation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PortInfoDto {

    public final String portId;

    public final String portSource;

    public final String name;

    public final String locode;

    public final String nationality;

    public final Double lon;

    public final Double lat;

    public final String seaArea;

    public final Integer displayOrder;

    public final String nameDisplayOffset;

    public PortInfoDto(@JsonProperty("portId") final String portId,
                       @JsonProperty("portSource") final String portSource,
                       @JsonProperty("name") final String name,
                       @JsonProperty("locode") final String locode,
                       @JsonProperty("nationality") final String nationality,
                       @JsonProperty("lon") final Double lon,
                       @JsonProperty("lat") final Double lat,
                       @JsonProperty("seaArea") final String seaArea,
                       @JsonProperty("displayOrder") final Integer displayOrder,
                       @JsonProperty("nameDisplayOffset") final String nameDisplayOffset) {
        this.portId = portId;
        this.portSource = portSource;
        this.name = name;
        this.locode = locode;
        this.nationality = nationality;
        this.lon = lon;
        this.lat = lat;
        this.seaArea = seaArea;
        this.displayOrder = displayOrder;
        this.nameDisplayOffset = nameDisplayOffset;
    }
}
