package in.kra3.scraper.services;

import in.kra3.scraper.data.ScrapeResult;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.Optional;


public interface ScrapeResultService {
    /**
     * Submits a scrape job after validation & returns jobId if sucessful
     *
     * @param url a url to scrape
     * @return jobId
     * @throws Exception
     */
    String submitJob(String url) throws Exception; // @todo: create specific exception

    /**
     * Returns result for the given jobId. If job is in progress, most fields will be empty.
     *
     * @param jobId
     * @return ScrapeResult
     */
    ScrapeResult getResult(String jobId) throws Exception;


    default boolean isValidUrl(String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }
}
