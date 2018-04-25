package in.kra3.scraper.services;

import in.kra3.scraper.ScraperApplication;
import in.kra3.scraper.converters.ScrapeRecordToScrapeResult;
import in.kra3.scraper.data.ScrapeResult;
import in.kra3.scraper.domain.ScrapeRecord;
import in.kra3.scraper.repositories.LookupRepository;
import in.kra3.scraper.repositories.ScrapeRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class ScrapeResultServiceImpl implements ScrapeResultService {
    private static final Logger log = LoggerFactory.getLogger(ScrapeResultServiceImpl.class);

    private ScrapeRecordRepository scrapeRecordRepository;
    private ScrapeRecordToScrapeResult scrapeRecordToScrapeResult;
    private RabbitTemplate rabbitTemplate;
    private LookupRepository lookupRepository;

    @Autowired
    public ScrapeResultServiceImpl(ScrapeRecordRepository scrapeRecordRepository,
                                   ScrapeRecordToScrapeResult scrapeRecordToScrapeResult,
                                   RabbitTemplate rabbitTemplate,
                                   LookupRepository lookupRepository) {
        this.scrapeRecordRepository = scrapeRecordRepository;
        this.scrapeRecordToScrapeResult = scrapeRecordToScrapeResult;
        this.rabbitTemplate = rabbitTemplate;
        this.lookupRepository = lookupRepository;
    }

    @Override
    public String submitJob(String url) throws Exception {
        // PS: a scraper in scale will face more challenges unrelated to
        // implementation such as ISPs blocking based on IPs, user agent, etc
        // so an implementation in scale should consider all those variants
        // and spread jobs different IP blocks, rotate user agents etc.
        // which involves quite a bit infra planning and routing logic.

        if (!isValidUrl(url)) {
            log.warn("Unsupported url " + url);
            throw new Exception("Url is not supported"); // @todo: specific exception
        }

        if (lookupRepository.isJobExistsForUrl(url)) { // de-duplication
            log.warn("duplicate scrape request for url " + url);
            return lookupRepository.getKeyForUrl(url);
        }

        // set job in redis to avoid duplicates
        String jobId = lookupRepository.addJob(url);
        // submits the job the message bus
        Map<String, String> job = new HashMap<>();
        job.put("jobId", jobId);
        job.put("url", url);
        rabbitTemplate.convertAndSend(ScraperApplication.queueName, job);
        // create an entry in db
        ScrapeRecord scrapeRecord = new ScrapeRecord();
        scrapeRecord.setJobId(jobId);
        scrapeRecord.setUrl(url);
        scrapeRecordRepository.save(scrapeRecord);

        return jobId;
    }

    @Override
    public ScrapeResult getResult(String jobId) throws Exception {
        if (!lookupRepository.hasJobExists(jobId)) {
            log.warn("Job not found for " + jobId);
            throw new Exception("Job not found");
        }

        if (!scrapeRecordRepository.existsByJobId(jobId)) {
            log.warn("Record not found for job " + jobId);
            throw new Exception("result not found in db");
        }

        ScrapeRecord scrapeRecord = scrapeRecordRepository.findByJobId(jobId);
        return scrapeRecordToScrapeResult.convert(scrapeRecord);
    }
}
