package fi.livi.digitraffic.meri.service.portnet;

import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;
import org.apache.commons.lang3.StringUtils;

public class SsnLocationReader extends AbstractReader<SsnLocation> {

    @Override
    protected SsnLocation convert(String line) {

        final String[] components = StringUtils.splitPreserveAllTokens(line, DELIMETER);

        SsnLocation ssnLocation = new SsnLocation();
        ssnLocation.setLocode(components[0]);
        ssnLocation.setLocationName(components[1]);
        ssnLocation.setCountry(components[2]);
        ssnLocation.setWgs84Lat(LocationParser.parseLatitude(components[3]));
        ssnLocation.setWgs84Long(LocationParser.parseLongitude(components[4]));

        return ssnLocation;
    }
}