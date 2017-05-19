package fi.livi.digitraffic.meri.service.winternavigation;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class WinterNavigationShipsDto {

    public final ZonedDateTime dataValidTime;

    public final ZonedDateTime dataQueryTime;

    public final List<WinterNavigationShipDto> ships = new ArrayList<>();

    public WinterNavigationShipsDto(@JsonProperty("dataValidTime") final ZonedDateTime dataValidTime,
                                    @JsonProperty("dataQueryTime") final ZonedDateTime dataQueryTime) {
        this.dataValidTime = dataValidTime;
        this.dataQueryTime = dataQueryTime;
    }

    @JsonSetter(value = "winterShip")
    public void addWinterShip(WinterNavigationShipDto ship) {
        this.ships.add(ship);
    }
}
