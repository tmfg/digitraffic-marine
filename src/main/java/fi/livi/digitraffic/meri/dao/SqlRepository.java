package fi.livi.digitraffic.meri.dao;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * You need to add the following for this to work:
 *
 * @EntityScan(basePackages = "fi.livi.digitraffic.meri.util.dao")
 */
@NoRepositoryBean
public interface SqlRepository extends Repository<SqlRepository.DummyEntity, Long> {
    @Entity
    class DummyEntity {
        @Id
        public long id;
    }

}
