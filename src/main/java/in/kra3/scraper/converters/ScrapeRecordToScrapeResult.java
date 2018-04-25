package in.kra3.scraper.converters;

import in.kra3.scraper.data.ScrapeResult;
import in.kra3.scraper.domain.ScrapeRecord;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class ScrapeRecordToScrapeResult implements Converter<ScrapeRecord, ScrapeResult> {
    public ScrapeResult convert(ScrapeRecord scrapeRecord) {
        ScrapeResult scrapeResult = new ScrapeResult();
        scrapeResult.setTitle(scrapeRecord.getTitle());
        scrapeResult.setContent(scrapeRecord.getContent());
        scrapeResult.setDate(scrapeRecord.getParsedDate());
        scrapeResult.setUrl(scrapeRecord.getUrl());
        return scrapeResult;
    }
}
