package fi.livi.digitraffic.meri.service.portnet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;

@Service
public class SsnLocationUpdater {
    private final SsnLocationRepository ssnLocationRepository;
    private final SsnLocationClient ssnLocationReader;

    public SsnLocationUpdater(final SsnLocationRepository ssnLocationRepository,
                              final SsnLocationClient ssnLocationReader) {
        this.ssnLocationRepository = ssnLocationRepository;
        this.ssnLocationReader = ssnLocationReader;
    }

    public void updateSsnLocations() throws IOException {
        final List<SsnLocation> oldLocations = ssnLocationRepository.findAll();
        final List<SsnLocation> newLocations = ssnLocationReader.getSsnLocations();

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
        BeanUtils.copyProperties(newLocation, oldLocation);
    }
}
