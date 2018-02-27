package fi.livi.digitraffic.meri.util.dao;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.Path;

import org.apache.commons.lang3.StringUtils;

import fi.livi.digitraffic.meri.domain.portnet.PortCall;

public final class ShortItemRestrictionUtil {
    private ShortItemRestrictionUtil() {}

    public static void addItemRestrictions(final QueryBuilder<Long, PortCall> qb, final Path<Object> p, final List<String> items) {
        final List<String> notInList = items.stream().filter(n -> StringUtils.startsWith(n, "!")).map(z -> z.substring(1)).collect(
            Collectors.toList());
        final List<String> inList = items.stream().filter(n -> !StringUtils.startsWith(n, "!")).collect(Collectors.toList());

        if(isNotEmpty(inList)) {
            qb.in(p, inList);
        }

        if(isNotEmpty(notInList)) {
            qb.notIn(p, notInList);
        }
    }
}
