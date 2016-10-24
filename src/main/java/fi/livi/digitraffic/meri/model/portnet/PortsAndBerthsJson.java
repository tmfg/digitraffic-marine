package fi.livi.digitraffic.meri.model.portnet;

import java.util.List;

import com.google.common.collect.ImmutableList;

public final class PortsAndBerthsJson {
    public final List<SsnLocationJson> locations;
    public final List<PortAreaJson> portAreas;
    public final List<BerthJson> berths;

    public PortsAndBerthsJson(final List<SsnLocationJson> locations, final List<PortAreaJson> portAreas, final List<BerthJson> berths) {
        this.locations = ImmutableList.copyOf(locations);
        this.portAreas = ImmutableList.copyOf(portAreas);
        this.berths = ImmutableList.copyOf(berths);
    }
}
