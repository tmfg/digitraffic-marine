package fi.livi.digitraffic.meri.service.portnet;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallJson;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;
import fi.livi.digitraffic.meri.service.BadRequestException;

@Service
public class PortCallService {
    private final UpdatedTimestampRepository updatedTimestampRepository;
    private final PortCallRepository portCallRepository;

    private final EntityManager entityManager;

    private final SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");

    public PortCallService(final UpdatedTimestampRepository updatedTimestampRepository,
                           final PortCallRepository portCallRepository,
                           final EntityManager entityManager) {
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.portCallRepository = portCallRepository;
        this.entityManager = entityManager;
    }

    private CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    @Transactional(readOnly = true)
    public PortCallsJson findPortCalls(final Date date, final ZonedDateTime from, final String locode, final String vesselName,
                                       final Integer mmsi, final Integer imo, final List<String> nationality, final Integer vesselTypeCode) {
        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(UpdatedTimestampRepository.UpdatedName.PORT_CALLS.name());

        final List<Long> portCallIds = getPortCallIds(date, from, locode, vesselName, mmsi, imo, nationality, vesselTypeCode);

        if (CollectionUtils.isEmpty(portCallIds)) {
            return new PortCallsJson(lastUpdated, Collections.emptyList());
        }

        if (portCallIds.size() > 1000) {
            throw new BadRequestException("Too big resultset, try narrow down");
        }

        final List<PortCallJson> portCallList = portCallRepository.findByPortCallIdIn(portCallIds);

        return new PortCallsJson(lastUpdated, portCallList);
    }

    private List<Long> getPortCallIds(final Date date, final ZonedDateTime from, final String locode, final String vesselName,
                                      final Integer mmsi, final Integer imo, final List<String> nationality, final Integer vesselTypeCode) {
        final CriteriaBuilder cb = getCriteriaBuilder();
        final CriteriaQuery<Long> query = cb.createQuery(Long.class);
        final Root<PortCall> root = query.from(PortCall.class);
        final List<Predicate> predicateList = new ArrayList();

        if (date != null) {
            predicateList.add(cb.equal(
                cb.function("to_char", Timestamp.class, root.get("portCallTimestamp"), cb.literal("YYYY-MM-dd")), formatter.format
                    (date)));
        }
        if (from != null) {
            predicateList.add(cb.greaterThanOrEqualTo(root.get("portCallTimestamp"), Date.from(from.toInstant())));
        }
        if (locode != null) {
            predicateList.add(cb.equal(root.get("portToVisit"), locode));
        }
        if (vesselName != null) {
            predicateList.add(cb.equal(cb.lower(root.get("vesselName")), vesselName.toLowerCase()));
        }
        if(mmsi != null) {
            predicateList.add(cb.equal(root.get("mmsi"), mmsi));
        }
        if(imo != null) {
            predicateList.add(cb.equal(root.get("imoLloyds"), imo));
        }

        if(isNotEmpty(nationality)) {
            addNationalityRestriction(predicateList, cb, root, nationality);
        }

        if(vesselTypeCode != null) {
            predicateList.add(cb.equal(root.get("vesselTypeCode"), vesselTypeCode));
        }

        query.select(root.get("portCallId")).where(predicateList.toArray(new Predicate[] {}));

        return entityManager.createQuery(query).getResultList();
    }

    private void addNationalityRestriction(final List<Predicate> predicateList, final CriteriaBuilder cb, final Root<PortCall> root,
        final List<String> nationality) {
        final List<String> notInList = nationality.stream().filter(n -> StringUtils.startsWith(n, "!")).map(z -> z.substring(1)).collect(Collectors.toList());
        final List<String> inList = nationality.stream().filter(n -> !StringUtils.startsWith(n, "!")).collect(Collectors.toList());

        if(isNotEmpty(inList)) {
            predicateList.add(cb.in(root.get("nationality")).value(inList));
        }

        if(isNotEmpty(notInList)) {
            predicateList.add(cb.in(root.get("nationality")).value(notInList).not());
        }
    }
}
