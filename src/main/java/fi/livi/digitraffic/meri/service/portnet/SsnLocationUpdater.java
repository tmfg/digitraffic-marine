package fi.livi.digitraffic.meri.service.portnet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;

@Service
public class SsnLocationUpdater {
    private final SsnLocationRepository ssnLocationRepository;
    private final SsnLocationXsfReader ssnLocationReader;

    public SsnLocationUpdater(final SsnLocationRepository ssnLocationRepository,
                              final SsnLocationXsfReader ssnLocationReader) {
        this.ssnLocationRepository = ssnLocationRepository;
        this.ssnLocationReader = ssnLocationReader;
    }

    public void updateSsnLocations(final Path path) throws OpenXML4JException, SAXException, IOException {
        final List<SsnLocation> oldLocations = ssnLocationRepository.findAll();
        final List<SsnLocation> newLocations = ssnLocationReader.readLocations(path);

        mergeLocations(oldLocations, newLocations);
    }

    private void mergeLocations(final List<SsnLocation> oldLocations, final List<SsnLocation> newLocations) {
        final Map<String, SsnLocation> oldMap = oldLocations.stream().collect(Collectors.toMap(SsnLocation::getLocode, Function.identity()));
        final List<SsnLocation> newList = new ArrayList<>();

        newLocations.stream().forEach(l -> {
            if(!oldMap.containsKey(l.getLocode())) {
                newList.add(l);
            } else {
                mergeLocation(oldMap.get(l.getLocode()), l);
            }

            // remove from oldMap, if added or modified
            oldMap.remove(l.getLocode());
        });

        // values in oldMap can be removed, they no longes exist
        ssnLocationRepository.delete(oldMap.values());

        ssnLocationRepository.save(newList);
    }

    private void mergeLocation(final SsnLocation oldLocation, final SsnLocation newLocation) {
        oldLocation.setLocationName(newLocation.getLocationName());
        oldLocation.setCountry(newLocation.getCountry());
        oldLocation.setWgs84Long(newLocation.getWgs84Long());
        oldLocation.setWgs84Lat(newLocation.getWgs84Lat());
    }
}
