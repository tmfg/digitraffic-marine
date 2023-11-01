package fi.livi.digitraffic.meri.service.portnet.location;

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
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.model.portnet.SsnLocation;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

@Component
public class LocationCoordinateReader {
    private static final Logger log = LoggerFactory.getLogger(LocationCoordinateReader.class);

    private final URL ssnLocationCoordinatesUrl;

    public LocationCoordinateReader(@Value("${ais.liikennetilanne.ssn_location.url}")final String ssnLocationCoordinatesUrl) throws MalformedURLException {
        this.ssnLocationCoordinatesUrl = new URL(ssnLocationCoordinatesUrl);
    }

    public List<SsnLocation> readCoordinates() {
        try {
            final JSONObject o = (JSONObject) JSONValue.parse(IOUtils.toString(ssnLocationCoordinatesUrl, StandardCharsets.UTF_8));

            return ((JSONArray)o.get("features")).stream().map(LocationCoordinateReader::convert).collect(Collectors.toList());
        } catch (final IOException e) {
            log.error("error", e);
        }

        return Collections.emptyList();
    }

    private static SsnLocation convert(final Object o) {
        final JSONObject jo = (JSONObject)o;
        final JSONObject properties = (JSONObject) jo.get("properties");
        final JSONObject geometry = (JSONObject) jo.get("geometry");
        final JSONArray coordinates = (JSONArray) geometry.get("coordinates");
        final SsnLocation location = new SsnLocation(); // do not persist this!

        // some names are lower case for some reason!
        final String locode = properties.getAsString("PORT_COD").toUpperCase();

        location.setLocode(locode);
        location.setWgs84Long(getDouble(coordinates.get(0)));
        location.setWgs84Lat(getDouble(coordinates.get(1)));

        return location;
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
