
package fi.livi.digitraffic.meri.model.pooki;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import fi.livi.digitraffic.meri.model.pooki.converter.JsonDateTimeDeserializerToZonedDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "GeoJSON Properties object")
@JsonPropertyOrder({"type", "id"})
public class Properties implements Serializable {

    @ApiModelProperty(required = true)
    @JsonProperty("id")
    private Integer id;

    // Merialueet suomeksi
    @ApiModelProperty("Marine area in Finnish")
    @JsonProperty("areasFi")
    private String areasFi;

    @ApiModelProperty("Marine area in Swedish")
    @JsonProperty("areasSv")
    private String areasSv;

    @ApiModelProperty("Marine area in English")
    @JsonProperty("areasEn")
    private String areasEn;

    // Numerointi alkaa jokaisen vuoden alussa aina ykkösestä ja annetaan vasta sitten kun varoituksen tila muuttuu julkaistuksi
    @ApiModelProperty("Nautical warning number. Starts with the digit 1 at the begining of the year. Set when warning state is changed from draft to published.")
    @JsonProperty("number")
    private Integer number;

    // Sijainnin tarkennus suomeksi
    @ApiModelProperty("Location specifier in Finnish")
    @JsonProperty("locationFi")
    private String locationFi;

    @ApiModelProperty("Location specifier in Swedish")
    @JsonProperty("locationSv")
    private String locationSv;

    @ApiModelProperty("Location specifier in English")
    @JsonProperty("locationEn")
    private String locationEn;

    // Merivaroituksen sisältö suomeksi
    @ApiModelProperty("Nautical warning contents in Finnish")
    @JsonProperty("contentsFi")
    private String contentsFi;

    @ApiModelProperty("Nautical warning contents in Swedish")
    @JsonProperty("contentsSv")
    private String contentsSv;

    @ApiModelProperty("Nautical warning contents in English")
    @JsonProperty("contentsEn")
    private String contentsEn;

    // Päiväys ja aika
    @ApiModelProperty("When nautical warning was given")
    @JsonProperty("givenTime")
    private ZonedDateTime givenTime;

    // Merivaroitustyypin selite suomeksi
    @ApiModelProperty("Nautical warning type in Finnish")
    @JsonProperty("typeFi")
    private String typeFi;

    @ApiModelProperty("Nautical warning type in Swedish")
    @JsonProperty("typeSv")
    private String typeSv;

    @ApiModelProperty("Nautical warning contents in English")
    @JsonProperty("typeEn")
    private String typeEn;

    @ApiModelProperty("Beginning of validity time")
    @JsonProperty("validityStartTime")
    private ZonedDateTime validityStartTime;

    @ApiModelProperty("End of validity time")
    @JsonProperty("validityEndTime")
    private ZonedDateTime validityEndTime;

    // Should this be hidden?
    @ApiModelProperty("Nautical warning contents for tooltip")
    @JsonProperty("tooltip")
    private String tooltip;

    @ApiModelProperty("Is safety equipment virtual")
    @JsonProperty("virtualSafetyEquipment")
    private Boolean virtualSafetyEquipment;

    @ApiModelProperty("Is navtex message")
    @JsonProperty("navtex")
    private Boolean navtex;

    // Järjestelmän antama päivämäärä jolloin merivaroituksen luonnos on tallennettu
    @ApiModelProperty("Time when draft of the nautical warning was created")
    @JsonProperty("recordingTime")
    private ZonedDateTime recordingTime;

    // Merivaroitukseen liittyvän turvalaitteen ominaisuustiedot  (rivinvaihdoilla eroteltuina)
    @ApiModelProperty("Safety device features related to the nautical warning (separated by line breaks)")
    @JsonProperty("safetyEquipmentText")
    private String safetyEquipmentText;

    // Merivaroitukseen liittyvän väyläalueen ominaisuustiedot (rivinvaihdoilla eroteltuina)
    @ApiModelProperty("Fairway features related to the nautical warning (separated by line breaks)")
    @JsonProperty("fairwaySpaceText")
    private String fairwaySpaceText;

    // Merivaroitukseen liittyvän navigointilinjan ominaisuustiedot (rivinvaihdoilla eroteltuina)
    @ApiModelProperty("Navigation line features related to the nautical warning (separated by line breaks)")
    @JsonProperty("navigationLineText")
    private String navigationLineText;

    @ApiModelProperty("Nautical warning publishing time")
    @JsonProperty("publishingTime")
    private ZonedDateTime publishingTime;

    @ApiModelProperty("Notificator")
    @JsonProperty("notificator")
    private String notificator;

    @JsonProperty("ID")
    public void setIDD(Integer id) {
        this.id = id;
    }

    @JsonProperty("ALUEET_FI")
    public void setAlueetFi(String areasFi) {
        this.areasFi = areasFi;
    }


    @JsonProperty("ALUEET_SV")
    public void setAlueetSv(String areasSv) {
        this.areasSv = areasSv;
    }

    @JsonProperty("ALUEET_EN")
    public void setAlueetEn(String areasEn) {
        this.areasEn = areasEn;
    }

    @JsonProperty("NUMERO")
    public void setNumero(Integer number) {
        this.number = number;
    }

    @JsonProperty("SIJAINTI_FI")
    public void setSijaintiFi(String locationFi) {
        this.locationFi = locationFi;
    }

    @JsonProperty("SIJAINTI_SV")
    public void setSijaintiSv(String locationSv) {
        this.locationSv = locationSv;
    }

    @JsonProperty("SIJAINTI_EN")
    public void setSijaintiEn(String locationEn) {
        this.locationEn = locationEn;
    }

    @JsonProperty("SISALTO_FI")
    public void setSisaltoFi(String contentsFi) {
        this.contentsFi = contentsFi;
    }

    @JsonProperty("SISALTO_SV")
    public void setSisaltoSv(String contentsSv) {
        this.contentsSv = contentsSv;
    }

    @JsonProperty("SISALTO_EN")
    public void setSisaltoEn(String contentsEn) {
        this.contentsEn = contentsEn;
    }

    @JsonProperty("PAIVAYS")
    @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class)
    public void setPaivays(ZonedDateTime time) {
        this.givenTime = time;
    }

    @JsonProperty("TYYPPI_FI")
    public void setTyyppiFi(String typeFi) {
        this.typeFi = typeFi;
    }

    @JsonProperty("TYYPPI_SV")
    public void setTyyppiSv(String typeSv) {
        this.typeSv = typeSv;
    }

    @JsonProperty("TYYPPI_EN")
    public void setTyyppiEn(String typeEn) {
        this.typeEn = typeEn;
    }

    @JsonProperty("VOIMASSA_ALKAA")
    @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class)
    public void setVoimassaAlkaa(ZonedDateTime validityStartTime) {
        this.validityStartTime = validityStartTime;
    }

    @JsonProperty("VOIMASSA_PAATTYY")
    @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class)
    public void setVoimassaPaattyy(ZonedDateTime validityEndTime) {
        this.validityEndTime = validityEndTime;
    }

    @JsonProperty("VALITTUKOHDE_TOOLTIP")
    public void setValittukohdeTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    @JsonProperty("VIRTUAALINENTURVALAITE")
    public void setVirtuaalinenturvalaite(Boolean virtualSafetyEquipment) {
        this.virtualSafetyEquipment = virtualSafetyEquipment;
    }

    @JsonProperty("NAVTEX")
    public void setNavtexi(Boolean navtex) {
        this.navtex = navtex;
    }

    @JsonProperty("TALLENNUSPAIVA")
    @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class)
    public void setTallennuspaiva(ZonedDateTime recordingDateTime) {
        this.recordingTime = recordingDateTime;
    }

    @JsonProperty("TURVALAITE_TXT")
    public void setTurvalaiteTxt(String safetyEquipmentText) {
        this.safetyEquipmentText = safetyEquipmentText;
    }

    @JsonProperty("VAYLAALUE_TXT")
    public void setVaylaalueTxt(String fairwaySpaceText) {
        this.fairwaySpaceText = fairwaySpaceText;
    }

    @JsonProperty("NAVIGOINTILINJA_TXT")
    public void setNavigointilinjaTxt(String navigationLineText) {
        this.navigationLineText = navigationLineText;
    }

    @JsonProperty("ANTOPAIVA")
    @JsonDeserialize(using = JsonDateTimeDeserializerToZonedDateTime.class)
    public void setAntopaiva(ZonedDateTime publishingTime) {
        this.publishingTime = publishingTime;
    }

    @JsonProperty("TIEDOKSIANTAJA")
    public void setTiedoksiantaja(String notificator) {
        this.notificator = notificator;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
