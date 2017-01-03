package fi.livi.digitraffic.meri.model.portnet.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="IMO information")
public interface ImoInformationJson {

    @ApiModelProperty(value = "Tells whether information concerns Departure or Arrival")
    String getImoGeneralDeclaration();

    @ApiModelProperty(value = "Brief particular information of voyage. Previous and following ports for example.")
    String getBriefParticularsVoyage();

    @ApiModelProperty(value = "Code of port where discharge occurs")
    String getPortOfDischarge();

    @ApiModelProperty(value = "Number of crew")
    Integer getNumberOfCrew();

    @ApiModelProperty(value = "Number of passengers")
    Integer getNumberOfPassangers();

    @ApiModelProperty(value = "Number of cargo declarations on board")
    Integer getCargoDeclarationOb();

    @ApiModelProperty(value = "Number of crew lists on board")
    Integer getCrewListsOb();

    @ApiModelProperty(value = "Number of crew effect declarations on board")
    Integer getCrewsEffectsDeclarationsOb();

    @ApiModelProperty(value = "Number of ship store declarations on board")
    Integer getShipStoresDeclarationsOb();

    @ApiModelProperty(value = "Number of passenger lists on board")
    Integer getPassangerListsOb();

    @ApiModelProperty(value = "Number of health care declarations on board")
    Integer getHealthDeclarationsOb();
}
