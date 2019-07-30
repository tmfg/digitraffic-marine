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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    // Merialueet suomeksi
    @ApiModelProperty(value = "Area in Finnish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String areasFi;

    @ApiModelProperty("Area in Swedish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String areasSv;

    @ApiModelProperty("Area in English")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String areasEn;

    // Numerointi alkaa jokaisen vuoden alussa aina ykkösestä ja annetaan vasta sitten kun varoituksen tila muuttuu julkaistuksi
    @ApiModelProperty("Nautical warning number. Starts with the digit 1 at the begining of the year. Set when warning was published.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer number;

    // Sijainnin tarkennus suomeksi
    @ApiModelProperty("Location specifier in Finnish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String locationFi;

    @ApiModelProperty("Location specifier in Swedish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String locationSv;

    @ApiModelProperty("Location specifier in English")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String locationEn;

    // Merivaroituksen sisältö suomeksi
    @ApiModelProperty("Nautical warning contents in Finnish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String contentsFi;

    @ApiModelProperty("Nautical warning contents in Swedish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String contentsSv;

    @ApiModelProperty("Nautical warning contents in English")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String contentsEn;

    // Päiväys ja aika
    @ApiModelProperty("Set when nautical warning was created")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime creationTime;

    // Merivaroitustyypin selite suomeksi
    @ApiModelProperty("Nautical warning type in Finnish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String typeFi;

    @ApiModelProperty("Nautical warning type in Swedish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String typeSv;

    @ApiModelProperty("Nautical warning type in English")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String typeEn;

    @ApiModelProperty("Beginning of validity time")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime validityStartTime;

    @ApiModelProperty("End of validity time")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime validityEndTime;

    // Should this be hidden?
    @ApiModelProperty("Nautical warning contents for tooltip")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String tooltip;

    @ApiModelProperty("Are there virtual navaids related to this warning")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean virtualNavaids;

    @ApiModelProperty("Is navtex message")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean navtex;

    // Merivaroitukseen liittyvän turvalaitteen ominaisuustiedot  (rivinvaihdoilla eroteltuina)
    @ApiModelProperty("Aids to navigation related to the nautical warning")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String navaidInfo;

    // Merivaroitukseen liittyvän väyläalueen ominaisuustiedot (rivinvaihdoilla eroteltuina)
    @ApiModelProperty("Fairway features related to the nautical warning (separated by line breaks)")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String fairwayInfo;

    // Merivaroitukseen liittyvän navigointilinjan ominaisuustiedot (rivinvaihdoilla eroteltuina)
    @ApiModelProperty("Navigation line features related to the nautical warning (separated by line breaks)")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String navigationLineInfo;

    @ApiModelProperty("Nautical warning publishing time")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime publishingTime;

    @ApiModelProperty("Notificator")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String notificator;

    public PookiProperties(@JsonProperty(value = "ID", access = JsonProperty.Access.WRITE_ONLY) final Integer id,
                           @JsonProperty(value = "ALUEET_FI", access = JsonProperty.Access.WRITE_ONLY) final String areasFi,
                           @JsonProperty(value = "ALUEET_SV", access = JsonProperty.Access.WRITE_ONLY) final String areasSv,
                           @JsonProperty(value = "ALUEET_EN", access = JsonProperty.Access.WRITE_ONLY) final String areasEn,
                           @JsonProperty(value = "NUMERO", access = JsonProperty.Access.WRITE_ONLY) final Integer number,
                           @JsonProperty(value = "SIJAINTI_FI", access = JsonProperty.Access.WRITE_ONLY) final String locationFi,
                           @JsonProperty(value = "SIJAINTI_SV", access = JsonProperty.Access.WRITE_ONLY) final String locationSv,
                           @JsonProperty(value = "SIJAINTI_EN", access = JsonProperty.Access.WRITE_ONLY) final String locationEn,
                           @JsonProperty(value = "SISALTO_FI", access = JsonProperty.Access.WRITE_ONLY) final String contentsFi,
                           @JsonProperty(value = "SISALTO_SV", access = JsonProperty.Access.WRITE_ONLY) final String contentsSv,
                           @JsonProperty(value = "SISALTO_EN", access = JsonProperty.Access.WRITE_ONLY) final String contentsEn,
                           @JsonProperty(value = "PAIVAYS", access = JsonProperty.Access.WRITE_ONLY) @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime creationTime,
                           @JsonProperty(value = "TYYPPI_FI", access = JsonProperty.Access.WRITE_ONLY) final String typeFi,
                           @JsonProperty(value = "TYYPPI_SV", access = JsonProperty.Access.WRITE_ONLY) final String typeSv,
                           @JsonProperty(value = "TYYPPI_EN", access = JsonProperty.Access.WRITE_ONLY) final String typeEn,
                           @JsonProperty(value = "VOIMASSA_ALKAA", access = JsonProperty.Access.WRITE_ONLY) @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime validityStartTime,
                           @JsonProperty(value = "VOIMASSA_PAATTYY", access = JsonProperty.Access.WRITE_ONLY) @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime validityEndTime,
                           @JsonProperty(value = "VALITTUKOHDE_TOOLTIP", access = JsonProperty.Access.WRITE_ONLY) final String tooltip,
                           @JsonProperty(value = "VIRTUAALINENTURVALAITE", access = JsonProperty.Access.WRITE_ONLY) final Boolean virtualNavaids,
                           @JsonProperty(value = "NAVTEX", access = JsonProperty.Access.WRITE_ONLY) final Boolean navtex,
                           @JsonProperty(value = "asdfTALLENNUSPAIVA", access = JsonProperty.Access.WRITE_ONLY) @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime recordingTime,
                           @JsonProperty(value = "TURVALAITE_TXT", access = JsonProperty.Access.WRITE_ONLY) final String navaidInfo,
                           @JsonProperty(value = "VAYLAALUE_TXT", access = JsonProperty.Access.WRITE_ONLY) final String fairwayInfo,
                           @JsonProperty(value = "NAVIGOINTILINJA_TXT", access = JsonProperty.Access.WRITE_ONLY) final String navigationLineInfo,
                           @JsonProperty(value = "ANTOPAIVA", access = JsonProperty.Access.WRITE_ONLY) @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime publishingTime,
                           @JsonProperty(value = "TIEDOKSIANTAJA", access = JsonProperty.Access.WRITE_ONLY) final String notificator) {
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