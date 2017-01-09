package fi.livi.digitraffic.meri.service.portnet.berth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.domain.portnet.PortArea;
import fi.livi.digitraffic.meri.domain.portnet.PortAreaKey;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

@Component
public class PortAreaCoordinateReader {
    private static final Logger log = LoggerFactory.getLogger(PortAreaCoordinateReader.class);

    private final URL portAreaLocationUrl;
    //private final String portAreaLocationUrl = "https://services1.arcgis.com/rhs5fjYxdOG1Et61/ArcGIS/rest/services/Satamat/FeatureServer/2/query?where=1%3D1&outFields=port_cod,p_area_cod&f=pgeojson&outSr=4326";

    public PortAreaCoordinateReader() throws MalformedURLException { //final String portAreaLocationUrl) {
        this.portAreaLocationUrl = new URL("https://services1.arcgis.com/rhs5fjYxdOG1Et61/ArcGIS/rest/services/Satamat/FeatureServer/2/query?where=1%3D1&outFields=port_cod,p_area_cod&f=pgeojson&outSr=4326");
    }

    public List<PortArea> readCoordinates() {
        try {
            final JSONObject o = (JSONObject) JSONValue.parse(IOUtils.toString(portAreaLocationUrl, Charset.forName("UTF-8")));

            return ((JSONArray)o.get("features")).stream().map(this::convert).collect(Collectors.toList());
        } catch (final IOException e) {
            log.error("error", e);
        }

        return Collections.emptyList();
    }

    private PortArea convert(final Object o) {
        final JSONObject jo = (JSONObject)o;
        final JSONObject properties = (JSONObject) jo.get("properties");
        final JSONObject geometry = (JSONObject) jo.get("geometry");
        final JSONArray coordinates = (JSONArray) geometry.get("coordinates");
        final PortArea pa = new PortArea(); // do not persist this!

        // some names are lower case for some reason!
        final String locode = properties.getAsString("PORT_COD").toUpperCase();
        final String port_area_code = properties.getAsString("P_AREA_COD").toUpperCase();

        pa.setPortAreaKey(PortAreaKey.of(locode, port_area_code));
        pa.setWgs84Long(getDouble(coordinates.get(0)));
        pa.setWgs84Lat(getDouble(coordinates.get(1)));

        return pa;
    }

    private static Double getDouble(final Object o) {
        if(o instanceof Integer) {
            return ((Integer)o).doubleValue();
        }
        if(o instanceof Double) {
            return (Double)o;
        }

        throw new IllegalArgumentException("can't convert to double " + o);
    }
}
