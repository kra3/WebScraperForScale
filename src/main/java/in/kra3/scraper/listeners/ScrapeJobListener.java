package in.kra3.scraper.listeners;

import in.kra3.scraper.domain.ScrapeRecord;
import in.kra3.scraper.repositories.ScrapeRecordRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class ScrapeJobListener {
    private ScrapeRecordRepository scrapeRecordRepository;

    private static final Logger log = LogManager.getLogger(ScrapeJobListener.class);

    public ScrapeJobListener(ScrapeRecordRepository productRepository) {
        this.scrapeRecordRepository = productRepository;
    }

    // this is the worker bean. This method get's invoked with messages
    public void receiveMessage(Map<String, String> message) {
        log.info("Received <" + message + ">");
        Long id = Long.valueOf(message.get("id"));
        ScrapeRecord product = scrapeRecordRepository.findById(id).orElse(null);
//        product.setMessageReceived(true);
//        product.setMessageCount(product.getMessageCount() + 1);

        scrapeRecordRepository.save(product);
        log.info("Message processed...");
    }
}
