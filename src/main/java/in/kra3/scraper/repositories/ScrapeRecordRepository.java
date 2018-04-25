package in.kra3.scraper.repositories;

import in.kra3.scraper.domain.ScrapeRecord;
import org.springframework.data.repository.CrudRepository;

public interface ScrapeRecordRepository  extends CrudRepository<ScrapeRecord, String> {
    ScrapeRecord findByJobId(String jobId);
}
