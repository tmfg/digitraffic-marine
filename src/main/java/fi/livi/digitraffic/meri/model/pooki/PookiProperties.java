
package fi.livi.digitraffic.meri.model.pooki;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.livi.digitraffic.meri.model.pooki.converter.JsonDateTimeDeserializerToZonedDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON Properties object")
@JsonPropertyOrder({"type", "id"})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PookiProperties implements Serializable {

    @ApiModelProperty(required = true)
    private Integer id;

    // Merialueet suomeksi
    @ApiModelProperty(name="areastest", value = "Area in Finnish")
    private String areasFi;

    @ApiModelProperty("Area in Swedish")
    private String areasSv;

    @ApiModelProperty("Area in English")
    private String areasEn;

    // Numerointi alkaa jokaisen vuoden alussa aina ykkösestä ja annetaan vasta sitten kun varoituksen tila muuttuu julkaistuksi
    @ApiModelProperty("Nautical warning number. Starts with the digit 1 at the begining of the year. Set when warning was published.")
    private Integer number;

    // Sijainnin tarkennus suomeksi
    @ApiModelProperty("Location specifier in Finnish")
    private String locationFi;

    @ApiModelProperty("Location specifier in Swedish")
    private String locationSv;

    @ApiModelProperty("Location specifier in English")
    private String locationEn;

    // Merivaroituksen sisältö suomeksi
    @ApiModelProperty("Nautical warning contents in Finnish")
    private String contentsFi;

    @ApiModelProperty("Nautical warning contents in Swedish")
    private String contentsSv;

    @ApiModelProperty("Nautical warning contents in English")
    private String contentsEn;

    // Päiväys ja aika
    @ApiModelProperty("Set when nautical warning was created")
    private ZonedDateTime creationTime;

    // Merivaroitustyypin selite suomeksi
    @ApiModelProperty("Nautical warning type in Finnish")
    private String typeFi;

    @ApiModelProperty("Nautical warning type in Swedish")
    private String typeSv;

    @ApiModelProperty("Nautical warning type in English")
    private String typeEn;

    @ApiModelProperty("Beginning of validity time")
    private ZonedDateTime validityStartTime;

    @ApiModelProperty("End of validity time")
    private ZonedDateTime validityEndTime;

    // Should this be hidden?
    @ApiModelProperty("Nautical warning contents for tooltip")
    private String tooltip;

    @ApiModelProperty("Are there virtual navaids related to this warning")
    private Boolean virtualNavaids;

    @ApiModelProperty("Is navtex message")
    private Boolean navtex;

    // Merivaroitukseen liittyvän turvalaitteen ominaisuustiedot  (rivinvaihdoilla eroteltuina)
    @ApiModelProperty("Aids to navigation related to the nautical warning")
    private String navaidInfo;

    // Merivaroitukseen liittyvän väyläalueen ominaisuustiedot (rivinvaihdoilla eroteltuina)
    @ApiModelProperty("Fairway features related to the nautical warning (separated by line breaks)")
    private String fairwayInfo;

    // Merivaroitukseen liittyvän navigointilinjan ominaisuustiedot (rivinvaihdoilla eroteltuina)
    @ApiModelProperty("Navigation line features related to the nautical warning (separated by line breaks)")
    private String navigationLineInfo;

    @ApiModelProperty("Nautical warning publishing time")
    private ZonedDateTime publishingTime;

    @ApiModelProperty("Notificator")
    private String notificator;

    public PookiProperties(@JsonProperty("ID") final Integer id,
                           @JsonProperty("ALUEET_FI") final String areasFi,
                           @JsonProperty("ALUEET_SV") final String areasSv,
                           @JsonProperty("ALUEET_EN") final String areasEn,
                           @JsonProperty("NUMERO") final Integer number,
                           @JsonProperty("SIJAINTI_FI") final String locationFi,
                           @JsonProperty("SIJAINTI_SV") final String locationSv,
                           @JsonProperty("SIJAINTI_EN") final String locationEn,
                           @JsonProperty("SISALTO_FI") final String contentsFi,
                           @JsonProperty("SISALTO_SV") final String contentsSv,
                           @JsonProperty("SISALTO_EN") final String contentsEn,
                           @JsonProperty("PAIVAYS") @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime creationTime,
                           @JsonProperty("TYYPPI_FI") final String typeFi,
                           @JsonProperty("TYYPPI_SV") final String typeSv,
                           @JsonProperty("TYYPPI_EN") final String typeEn,
                           @JsonProperty("VOIMASSA_ALKAA") @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime validityStartTime,
                           @JsonProperty("VOIMASSA_PAATTYY") @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime validityEndTime,
                           @JsonProperty("VALITTUKOHDE_TOOLTIP") final String tooltip,
                           @JsonProperty("VIRTUAALINENTURVALAITE") final Boolean virtualNavaids,
                           @JsonProperty("NAVTEX") final Boolean navtex,
                           @JsonProperty("asdfTALLENNUSPAIVA") @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime recordingTime,
                           @JsonProperty("TURVALAITE_TXT") final String navaidInfo,
                           @JsonProperty("VAYLAALUE_TXT") final String fairwayInfo,
                           @JsonProperty("NAVIGOINTILINJA_TXT") final String navigationLineInfo,
                           @JsonProperty("ANTOPAIVA") @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime publishingTime,
                           @JsonProperty("TIEDOKSIANTAJA") final String notificator) {
        this.id = id;
        this.areasFi = areasFi;
        this.areasSv = areasSv;
        this.areasEn = areasEn;
        this.number = number;
        this.locationFi = locationFi;
        this.locationSv = locationSv;
        this.locationEn = locationEn;
        this.contentsFi = contentsFi;
        this.contentsSv = contentsSv;
        this.contentsEn = contentsEn;
        this.creationTime = creationTime;
        this.typeFi = typeFi;
        this.typeSv = typeSv;
        this.typeEn = typeEn;
        this.validityStartTime = validityStartTime;
        this.validityEndTime = validityEndTime;
        this.tooltip = tooltip;
        this.virtualNavaids = virtualNavaids;
        this.navtex = navtex;
        this.navaidInfo = navaidInfo;
        this.fairwayInfo = fairwayInfo;
        this.navigationLineInfo = navigationLineInfo;
        this.publishingTime = publishingTime;
        this.notificator = notificator;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
