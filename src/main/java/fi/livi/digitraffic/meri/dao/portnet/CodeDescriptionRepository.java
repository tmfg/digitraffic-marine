package fi.livi.digitraffic.meri.dao.portnet;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fi.livi.digitraffic.meri.dao.SqlRepository;
import fi.livi.digitraffic.meri.dto.portcall.v1.code.CodeDescriptionJsonV1;

@Repository
@ConditionalOnWebApplication
public interface CodeDescriptionRepository extends SqlRepository {

    @Query(value = "select code, description_fi descriptionFi, description_en descriptionEn from code_description where domain = 'VESSEL'"
        , nativeQuery = true)
    List<CodeDescriptionJsonV1> listAllVesselTypes();

    @Query(value = "select code, description_fi descriptionFi, description_en descriptionEn from code_description where domain = 'AGENT'",
        nativeQuery = true)
    List<CodeDescriptionJsonV1> listAllAgentTypes();
}
