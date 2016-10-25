package fi.livi.digitraffic.meri.dao;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.model.CodeDescriptionJson;
import fi.livi.util.locking.SqlRepository;

@Repository
public interface CodeDescriptionRepository extends SqlRepository {
    @Query(value = "select code, description from code_description where domain = 'CARGO'", nativeQuery = true)
    Stream<CodeDescriptionJson> streamAllCargoTypes();

    @Query(value = "select code, description from code_description where domain = 'VESSEL'", nativeQuery = true)
    Stream<CodeDescriptionJson> streamAllVesselTypes();

    @Query(value = "select code, description from code_description where domain = 'AGENT'", nativeQuery = true)
    Stream<CodeDescriptionJson> streamAllAgentTypes();
}
