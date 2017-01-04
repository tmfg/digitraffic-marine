package fi.livi.digitraffic.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;

/**
 * Split id-list to sublists of 1000, because oracle can't handle
 * over 1000 inlist parameters
 */
public final class SublistFetcher {
    public static final int ORACLE_SAFE_INCREMENT = 1000;

    private SublistFetcher() {}

    public static <K, T> List<K> fetch(final List<T> idList, final Function<List<T>, List<K>> function) {
        return fetch(idList, ORACLE_SAFE_INCREMENT, function);
    }

    public static <K, T> List<K> fetch(final List<T> idList, final int size, final Function<List<T>, List<K>> function) {
        return ListUtils.partition(idList, size).stream()
                .flatMap(l -> function.apply(l).stream())
                .collect(Collectors.toList());
    }

}
