package fi.livi.digitraffic.meri.service.portnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractReader<T> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected static final String DELIMETER = ";";

    public List<T> readCsv(final String data) {
        final Stream<String> stream = Stream.of(data.split("\n"));
        // skip the first line, then convert others
        return stream.skip(1)
            .map(this::convert)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    protected abstract T convert(final String line);
}