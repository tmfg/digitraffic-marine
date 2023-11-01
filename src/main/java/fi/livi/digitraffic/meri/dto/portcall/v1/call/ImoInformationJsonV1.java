package fi.livi.digitraffic.meri.dto.portcall.v1.call;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="IMO information")
public interface ImoInformationJsonV1 {

    @Schema(description = "Tells whether information concerns Departure or Arrival")
    String getImoGeneralDeclaration();

    @Schema(description = "Brief particular information of voyage. Previous and following ports for example.")
    String getBriefParticularsVoyage();

    @Schema(description = "Code of port where discharge occurs")
    String getPortOfDischarge();

    @Schema(description = "Number of crew")
    Integer getNumberOfCrew();

    @Schema(description = "Number of passengers")
    Integer getNumberOfPassangers();

    @Schema(description = "Number of cargo declarations on board")
    Integer getCargoDeclarationOb();

    @Schema(description = "Number of crew lists on board")
    Integer getCrewListsOb();

    @Schema(description = "Number of crew effect declarations on board")
    Integer getCrewsEffectsDeclarationsOb();

    @Schema(description = "Number of ship store declarations on board")
    Integer getShipStoresDeclarationsOb();

    @Schema(description = "Number of passenger lists on board")
    Integer getPassangerListsOb();

    @Schema(description = "Number of health care declarations on board")
    Integer getHealthDeclarationsOb();
}
