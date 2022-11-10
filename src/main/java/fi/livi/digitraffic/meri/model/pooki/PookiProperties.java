package fi.livi.digitraffic.meri.model.pooki;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fi.livi.digitraffic.meri.model.geojson.Properties;
import fi.livi.digitraffic.meri.model.pooki.converter.JsonDateTimeDeserializerToZonedDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.ZonedDateTime;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

@JsonPropertyOrder({"type", "id"})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PookiProperties extends Properties {

    @Schema(required = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Integer id;

    // Merialueet suomeksi
    @Schema(description = "Area in Finnish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String areasFi;

    @Schema(description = "Area in Swedish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String areasSv;

    @Schema(description = "Area in English")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String areasEn;

    // Numerointi alkaa jokaisen vuoden alussa aina ykkösestä ja annetaan vasta sitten kun varoituksen tila muuttuu julkaistuksi
    @Schema(description = "Nautical warning number. Starts with the digit 1 at the begining of the year. Set when warning was published.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Integer number;

    // Sijainnin tarkennus suomeksi
    @Schema(description = "Location specifier in Finnish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String locationFi;

    @Schema(description = "Location specifier in Swedish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String locationSv;

    @Schema(description = "Location specifier in English")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String locationEn;

    // Merivaroituksen sisältö suomeksi
    @Schema(description = "Nautical warning contents in Finnish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String contentsFi;

    @Schema(description = "Nautical warning contents in Swedish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String contentsSv;

    @Schema(description = "Nautical warning contents in English")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String contentsEn;

    // Päiväys ja aika
    @Schema(description = "Set when nautical warning was created")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public final ZonedDateTime creationTime;

    // Merivaroitustyypin selite suomeksi
    @Schema(description = "Nautical warning type in Finnish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String typeFi;

    @Schema(description = "Nautical warning type in Swedish")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String typeSv;

    @Schema(description = "Nautical warning type in English")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String typeEn;

    @Schema(description = "Beginning of validity time")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final ZonedDateTime validityStartTime;

    @Schema(description = "End of validity time")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final ZonedDateTime validityEndTime;

    // Should this be hidden?
    @Schema(description = "Nautical warning contents for tooltip")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String tooltip;

    @Schema(description = "Are there virtual navaids related to this warning")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Boolean virtualNavaids;

    @Schema(description = "Is navtex message")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Boolean navtex;

    // Merivaroitukseen liittyvän turvalaitteen ominaisuustiedot  (rivinvaihdoilla eroteltuina)
    @Schema(description = "Aids to navigation related to the nautical warning")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String navaidInfo;

    // Merivaroitukseen liittyvän väyläalueen ominaisuustiedot (rivinvaihdoilla eroteltuina)
    @Schema(description = "Fairway features related to the nautical warning (separated by line breaks)")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String fairwayInfo;

    // Merivaroitukseen liittyvän navigointilinjan ominaisuustiedot (rivinvaihdoilla eroteltuina)
    @Schema(description = "Navigation line features related to the nautical warning (separated by line breaks)")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String navigationLineInfo;

    @Schema(description = "Nautical warning publishing time")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public final ZonedDateTime publishingTime;

    @Schema(description = "Notificator")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String notificator;

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
                           @JsonProperty(value = "TALLENNUSPAIVA", access = JsonProperty.Access.WRITE_ONLY) @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime recordingTime,
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
        return ToStringBuilder.reflectionToString(this, JSON_STYLE);
    }
}