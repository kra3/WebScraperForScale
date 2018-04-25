package in.kra3.scraper.services;

import in.kra3.scraper.ScraperApplication;
import in.kra3.scraper.converters.ScrapeRecordToScrapeResult;
import in.kra3.scraper.data.ScrapeResult;
import in.kra3.scraper.repositories.LookupRepository;
import in.kra3.scraper.repositories.ScrapeRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


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
            throw new Exception("Url is not supported"); // @todo: specific exception
        }

        // @todo: checks url in redis for duplicates
        // @todo: if duplicate, get job id for the url
        // PS: redis implementation could be changed to a bloom filter to save memory.
        // currently, I'm saving url-hash -> jobId for ease of prototype
        // however if switching to bloom filters, we have to query DB for jobId
        // CouchDB/Cassandra could be considered in that case for DB
        // Which are both distributed and is a good choice for big data analysis needs.

        // @todo: otherwise create a job and enqueue, set redis key
        UUID jobId = UUID.randomUUID();
        Map<String, String> job = new HashMap<>();
        job.put("jobId", jobId.toString());
        job.put("url", url);
        rabbitTemplate.convertAndSend(ScraperApplication.queueName, job);
        return jobId.toString();
    }

    @Override
    public ScrapeResult getResult(String jobId) {
        // @todo: check if job exists
        // @todo: read job data from db
        return null;
    }
}
