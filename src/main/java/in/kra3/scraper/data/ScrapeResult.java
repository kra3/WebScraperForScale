package in.kra3.scraper.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

public class ScrapeResult {
    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private Date date;

    @Getter
    @Setter
    @NonNull
    private String url;

    @Getter
    @Setter
    private String content;
}
