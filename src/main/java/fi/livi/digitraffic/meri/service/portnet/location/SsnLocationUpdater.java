package fi.livi.digitraffic.meri.service.portnet.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.model.portnet.SsnLocation;

@Service
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnNotWebApplication
public class SsnLocationUpdater {
    private final SsnLocationRepository ssnLocationRepository;
    private final SsnLocationClient ssnLocationClient;
    private final LocationCoordinateReader locationCoordinateReader;

    private static final Logger log = LoggerFactory.getLogger(SsnLocationUpdater.class);

    public SsnLocationUpdater(final SsnLocationRepository ssnLocationRepository,
                              final SsnLocationClient ssnLocationClient,
                              final LocationCoordinateReader locationCoordinateReader) {
        this.ssnLocationRepository = ssnLocationRepository;
        this.ssnLocationClient = ssnLocationClient;
        this.locationCoordinateReader = locationCoordinateReader;
    }

    @Transactional
    public void updateSsnLocations() {
        final List<SsnLocation> oldLocations = ssnLocationRepository.findAll();
        final List<SsnLocation> newLocations = ssnLocationClient.getSsnLocations();

        if(mergeLocations(oldLocations, newLocations)) {
            updateCoordinates(oldLocations);
        }
    }

    private void updateCoordinates(final List<SsnLocation> oldLocations) {
        final Map<String, SsnLocation> oldMap = oldLocations.stream().collect(Collectors.toMap(SsnLocation::getLocode, Function.identity()));
        final List<SsnLocation> newCoordinates = locationCoordinateReader.readCoordinates();

        newCoordinates.stream().forEach(nc -> {
            final SsnLocation location = oldMap.get(nc.getLocode());

            if(location == null) {
                log.info("Can't find ssn location for ssnLocationCode={}", nc.getLocode());
            } else {
                location.setWgs84Lat(nc.getWgs84Lat());
                location.setWgs84Long(nc.getWgs84Long());
            }
        });
    }

    private boolean mergeLocations(final List<SsnLocation> oldLocations, final List<SsnLocation> newLocations) {
        final Map<String, SsnLocation> oldMap = oldLocations.stream().collect(Collectors.toMap(SsnLocation::getLocode, Function.identity()));
        final List<SsnLocation> newList = new ArrayList<>();
        int updates = 0;

        for(final SsnLocation l : newLocations) {
            if(!oldMap.containsKey(l.getLocode())) {
                newList.add(l);
            } else {
                if(mergeLocation(oldMap.get(l.getLocode()), l)) {
                    updates++;
                }
            }

            // remove from oldMap, if added or modified
            oldMap.remove(l.getLocode());
        }

        // values in oldMap can be removed, they no longes exist
        ssnLocationRepository.deleteAll(oldMap.values());
        ssnLocationRepository.saveAll(newList);

        log.info("locationsReadCount={} locations, locationsAddedCount={} locationsUpdatedCount={} locationsDeletedCount={} .",
                newLocations.size(), newList.size(), updates, oldMap.values().size());

        return CollectionUtils.isNotEmpty(newList) || updates > 0;
    }

    private static boolean mergeLocation(final SsnLocation oldLocation, final SsnLocation newLocation) {
        final boolean difference = !EqualsBuilder.reflectionEquals(oldLocation, newLocation);

        BeanUtils.copyProperties(newLocation, oldLocation, "locode");

        return difference;
    }
}
