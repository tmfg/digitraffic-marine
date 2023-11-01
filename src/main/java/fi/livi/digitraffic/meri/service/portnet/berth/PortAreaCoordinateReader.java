package fi.livi.digitraffic.meri.service.portnet.berth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.model.portnet.PortArea;
import fi.livi.digitraffic.meri.model.portnet.PortAreaKey;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

@Component
@ConditionalOnNotWebApplication
public class PortAreaCoordinateReader {
    private static final Logger log = LoggerFactory.getLogger(PortAreaCoordinateReader.class);

    private final URL portAreaCoordinatesUrl;

    public PortAreaCoordinateReader(@Value("${ais.liikennetilanne.port_area.url}")final String portAreaCoordinatesUrl) throws MalformedURLException {
            this.portAreaCoordinatesUrl = new URL(portAreaCoordinatesUrl);
    }

    public List<PortArea> readCoordinates() {
        try {
            final JSONObject o = (JSONObject) JSONValue.parse(IOUtils.toString(portAreaCoordinatesUrl, StandardCharsets.UTF_8));

            return ((JSONArray)o.get("features")).stream().map(PortAreaCoordinateReader::convert).collect(Collectors.toList());
        } catch (final IOException e) {
            log.error("error", e);
        }

        return Collections.emptyList();
    }

    private static PortArea convert(final Object o) {
        final JSONObject jo = (JSONObject)o;
        final JSONObject properties = (JSONObject) jo.get("properties");
        final JSONObject geometry = (JSONObject) jo.get("geometry");
        final JSONArray coordinates = (JSONArray) geometry.get("coordinates");
        final PortArea pa = new PortArea(); // do not persist this!

        // some names are lower case for some reason!
        final String locode = properties.getAsString("PORT_COD").toUpperCase();
        final String portAreaCode = properties.getAsString("P_AREA_COD").toUpperCase();

        pa.setPortAreaKey(PortAreaKey.of(locode, portAreaCode));
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
