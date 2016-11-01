package fi.livi.digitraffic.meri.model.portnet;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Imo information")
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ImoInformationJson {
    String getImoGeneralDeclaration();
    String getBriefParticularsVoyage();
    String getPortOfDischarge();

    Integer getNumberOfCrew();
    Integer getNumberOfPassangers();
    Integer getCargoDeclarationOb();
    Integer getCrewListsOb();
    Integer getCrewsEffectsDeclarationsOb();
    Integer getShipStoresDeclarationsOb();
    Integer getPassangerListsOb();
    Integer getHealthDeclarationsOb();

}
