package fi.livi.digitraffic.meri.service.portnet.location;

import org.apache.commons.lang3.StringUtils;

import fi.livi.digitraffic.meri.model.portnet.SsnLocation;
import fi.livi.digitraffic.meri.service.portnet.AbstractReader;

public class SsnLocationReader extends AbstractReader<SsnLocation> {
    @Override
    protected SsnLocation convert(final String line) {
        final String[] components = StringUtils.splitPreserveAllTokens(line, DELIMETER);

        final SsnLocation ssnLocation = new SsnLocation();
        ssnLocation.setLocode(components[0]);
        ssnLocation.setLocationName(getString(components[1]));
        ssnLocation.setCountry(getString(components[2]));
        ssnLocation.setWgs84Lat(LocationParser.parseLatitude(components[3]));
        ssnLocation.setWgs84Long(LocationParser.parseLongitude(components[4]));

        return ssnLocation;
    }

    private static String getString(final String s) {
        return StringUtils.isEmpty(s) ? null : s;
    }
}
