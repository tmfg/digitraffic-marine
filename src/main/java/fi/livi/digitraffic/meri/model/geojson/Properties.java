package fi.livi.digitraffic.meri.model.geojson;

import java.io.Serializable;

import fi.livi.digitraffic.meri.model.ais.VesselLocationProperties;
import fi.livi.digitraffic.meri.model.pooki.PookiProperties;
import fi.livi.digitraffic.meri.model.portnet.metadata.BerthProperties;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortAreaProperties;
import fi.livi.digitraffic.meri.model.portnet.metadata.SsnLocationProperties;
import fi.livi.digitraffic.meri.model.sse.SseProperties;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirwayProperties;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortProperties;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShipProperties;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "GeoJSON Properties object", subTypes = { BerthProperties.class,
                                                                  PookiProperties.class,
                                                                  PortAreaProperties.class,
                                                                  SseProperties.class,
                                                                  SsnLocationProperties.class,
                                                                  VesselLocationProperties.class,
                                                                  WinterNavigationDirwayProperties.class,
                                                                  WinterNavigationPortProperties.class,
                                                                  WinterNavigationShipProperties.class
                                                                })
public abstract class Properties implements Serializable {

}
