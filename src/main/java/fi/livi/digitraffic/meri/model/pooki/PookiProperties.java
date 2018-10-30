package fi.livi.digitraffic.meri.model.pooki;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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

    public PookiProperties(final Integer id,
                           final String alueetFi,
                           final String alueetSv,
                           final String alueetEn,
                           final Integer number,
                           final String sijaintiFi,
                           final String sijaintiSv,
                           final String sijaintiEn,
                           final String sisaltoFi,
                           final String sisaltoSv,
                           final String sisaltoEn,
                           @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime paivays,
                           final String tyyppiFi,
                           final String tyyppiSv,
                           final String tyyppiEn,
                           @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime voimassaAlkaa,
                           @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final ZonedDateTime voimassaPaattyy,
                           final String valittykohdeTooltip,
                           final Boolean virtuaalinenturvalaite,
                           final Boolean navtex,
                           @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class)                         final ZonedDateTime asdfTallennuspaiva,
                           final String turvalaiteTxt,
                           final String vaylaalueTxt,
                           final String navigointilinjaTxt,
                           @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class) final
                           ZonedDateTime antopaiva,
                           final String tiedoksiantaja) {
        this.id = id;
        this.areasFi = alueetFi;
        this.areasSv = alueetSv;
        this.areasEn = alueetEn;
        this.number = number;
        this.locationFi = sijaintiFi;
        this.locationSv = sijaintiSv;
        this.locationEn = sijaintiEn;
        this.contentsFi = sisaltoFi;
        this.contentsSv = sisaltoSv;
        this.contentsEn = sisaltoEn;
        this.creationTime = paivays;
        this.typeFi = tyyppiFi;
        this.typeSv = tyyppiSv;
        this.typeEn = tyyppiEn;
        this.validityStartTime = voimassaAlkaa;
        this.validityEndTime = voimassaPaattyy;
        this.tooltip = valittykohdeTooltip;
        this.virtualNavaids = virtuaalinenturvalaite;
        this.navtex = navtex;
        this.navaidInfo = turvalaiteTxt;
        this.fairwayInfo = vaylaalueTxt;
        this.navigationLineInfo = navigointilinjaTxt;
        this.publishingTime = antopaiva;
        this.notificator = tiedoksiantaja;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}