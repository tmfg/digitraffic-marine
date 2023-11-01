package fi.livi.digitraffic.meri.model.portnet;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@DynamicInsert
public class ImoInformation {
    @SequenceGenerator(name = "SEQ_IMO_INFORMATION", sequenceName = "SEQ_IMO_INFORMATION", allocationSize = 1)
    @GeneratedValue(generator = "SEQ_IMO_INFORMATION")
    @Id
    private Long imoInformationId;

    private String imoGeneralDeclaration;
    private String briefParticularsVoyage;
    private String portOfDischarge;

    private Integer numberOfCrew;
    private Integer numberOfPassangers;
    private Integer cargoDeclarationOb;
    private Integer crewListsOb;
    private Integer crewsEffectsDeclarationsOb;
    private Integer shipStoresDeclarationsOb;
    private Integer passangerListsOb;
    private Integer healthDeclarationsOb;

    public Long getImoInformationId() {
        return imoInformationId;
    }

    public void setImoInformationId(final Long imoInformationId) {
        this.imoInformationId = imoInformationId;
    }

    public String getImoGeneralDeclaration() {
        return imoGeneralDeclaration;
    }

    public void setImoGeneralDeclaration(final String imoGeneralDeclaration) {
        this.imoGeneralDeclaration = imoGeneralDeclaration;
    }

    public String getBriefParticularsVoyage() {
        return briefParticularsVoyage;
    }

    public void setBriefParticularsVoyage(final String briefParticularsVoyage) {
        this.briefParticularsVoyage = briefParticularsVoyage;
    }

    public String getPortOfDischarge() {
        return portOfDischarge;
    }

    public void setPortOfDischarge(final String portOfDischarge) {
        this.portOfDischarge = portOfDischarge;
    }

    public Integer getNumberOfCrew() {
        return numberOfCrew;
    }

    public void setNumberOfCrew(final Integer numberOfCrew) {
        this.numberOfCrew = numberOfCrew;
    }

    public Integer getNumberOfPassangers() {
        return numberOfPassangers;
    }

    public void setNumberOfPassangers(final Integer numberOfPassangers) {
        this.numberOfPassangers = numberOfPassangers;
    }

    public Integer getCargoDeclarationOb() {
        return cargoDeclarationOb;
    }

    public void setCargoDeclarationOb(final Integer cargoDeclarationOb) {
        this.cargoDeclarationOb = cargoDeclarationOb;
    }

    public Integer getCrewListsOb() {
        return crewListsOb;
    }

    public void setCrewListsOb(final Integer crewListsOb) {
        this.crewListsOb = crewListsOb;
    }

    public Integer getCrewsEffectsDeclarationsOb() {
        return crewsEffectsDeclarationsOb;
    }

    public void setCrewsEffectsDeclarationsOb(final Integer crewsEffectsDeclarationsOb) {
        this.crewsEffectsDeclarationsOb = crewsEffectsDeclarationsOb;
    }

    public Integer getShipStoresDeclarationsOb() {
        return shipStoresDeclarationsOb;
    }

    public void setShipStoresDeclarationsOb(final Integer shipStoresDeclarationsOb) {
        this.shipStoresDeclarationsOb = shipStoresDeclarationsOb;
    }

    public Integer getPassangerListsOb() {
        return passangerListsOb;
    }

    public void setPassangerListsOb(final Integer passangerListsOb) {
        this.passangerListsOb = passangerListsOb;
    }

    public Integer getHealthDeclarationsOb() {
        return healthDeclarationsOb;
    }

    public void setHealthDeclarationsOb(final Integer healthDeclarationsOb) {
        this.healthDeclarationsOb = healthDeclarationsOb;
    }
}
