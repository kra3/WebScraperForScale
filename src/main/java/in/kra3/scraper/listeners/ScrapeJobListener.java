package in.kra3.scraper.listeners;

import in.kra3.scraper.domain.ScrapeRecord;
import in.kra3.scraper.repositories.ScrapeRecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class ScrapeJobListener {
    // PS: in ideal case, workers should be separate project (to build a stand alone executable)
    // so as to keep them light weight. This is the unit which is needed to scale
    // the scraping capabilities. However, this is good for a prototype.

    private ScrapeRecordRepository scrapeRecordRepository;

    private static final Logger log = LogManager.getLogger(ScrapeJobListener.class);

    @Autowired
    public ScrapeJobListener(ScrapeRecordRepository productRepository) {
        this.scrapeRecordRepository = productRepository;
    }

    // This method get's invoked with messages as they arrive in queue
    public void receiveMessage(Map<String, String> message) {
        log.info("Received <" + message + ">");
        // @todo: get url from message and parse it

        // @todo: post the result back to another queue
        // PS: if result is put into the queue, it could be picked up by
        // different listeners for various purposes (such as persist to db,
        // stream to analytics pipeline, etc.,)

        // PS: for simplified prototype: persist directly to db
//        ScrapeRecord product = scrapeRecordRepository.findById(id).orElse(null);
//        product.setMessageReceived(true);
//        product.setMessageCount(product.getMessageCount() + 1);

//        scrapeRecordRepository.save(product);
        log.info("Message processed...");
    }
}
