package fi.livi.digitraffic.meri.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * Inherited fields are read only from db
 */
@MappedSuperclass
public abstract class ReadOnlyCreatedAndModifiedFields {

    @Column(nullable = false, updatable = false, insertable = false)
    private Instant created;

    @Column(nullable = false, updatable = false, insertable = false)
    private Instant modified;

    @JsonIgnore
    public Instant getCreated() {
        return created;
    }

    @JsonIgnore
    public Instant getModified() {
        return modified;
    }
}
