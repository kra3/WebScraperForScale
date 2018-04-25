package in.kra3.scraper.listeners;

import in.kra3.scraper.domain.ScrapeRecord;
import in.kra3.scraper.repositories.ScrapeRecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Map;


@Component
public class ScrapeJobListener {
    // PS: in ideal case, workers should be separate project (to build a stand alone executable)
    // so as to keep them light weight. This is the unit which is needed to scale
    // the scraping capabilities. However, this is good for a prototype.
    // also, multiple queues & listeners could be created to distribute work
    // across geographies, ip ranges, etc.,

    private ScrapeRecordRepository scrapeRecordRepository;

    private static final Logger log = LogManager.getLogger(ScrapeJobListener.class);

    @Autowired
    public ScrapeJobListener(ScrapeRecordRepository productRepository) {
        this.scrapeRecordRepository = productRepository;
    }

    // This method get's invoked with messages as they arrive in queue
    public void receiveMessage(Map<String, String> message) {
        log.info("Received <" + message + ">");

        String url = message.get("url");
        String jobId = message.get("jobId");

        ScrapeRecord scrapeRecord = scrapeRecordRepository.findByJobId(jobId);

        if (scrapeRecord == null) {
            log.warn("Job not found for " + jobId);
            return;
        }

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("SkyNet Scraper")  // @todo: rotate user agent strings
                    .get();
            scrapeRecord.setTitle(doc.title());
            scrapeRecord.setContent(doc.outerHtml());
            scrapeRecord.setParsedDate(new Date()); // @todo: get date in UTC
            scrapeRecordRepository.save(scrapeRecord);
        } catch (IOException e) {
            log.warn("Error fetching url: " + url);
        }

        // PS: if result is put into a separate queue, we could design the system
        // such that result could be picked up by different workers for various
        // purposes (such as persist to db stream, process through analytics pipeline, etc.,)

        log.info("Message processed...");
    }
}
