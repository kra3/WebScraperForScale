package in.kra3.scraper.repositories;

import in.kra3.scraper.domain.ScrapeRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("db:store")
public interface ScrapeRecordRepository extends CrudRepository<ScrapeRecord, String> {
    ScrapeRecord findByJobId(String jobId);
    boolean existsByJobId(String jobId);
}
