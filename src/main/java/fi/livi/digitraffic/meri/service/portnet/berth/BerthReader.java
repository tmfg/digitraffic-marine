package fi.livi.digitraffic.meri.service.portnet.berth;

import org.apache.commons.lang3.StringUtils;

import fi.livi.digitraffic.meri.service.portnet.AbstractReader;

public class BerthReader extends AbstractReader<BerthLine> {
    @Override
    protected BerthLine convert(final String line) {
        final String[] components = StringUtils.splitPreserveAllTokens(line, DELIMETER);

        return new BerthLine(components[0], components[1], components[2], components[3], components[4], components[5]);
    }
}
