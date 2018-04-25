package in.kra3.scraper.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class ScrapeJob {
    @Getter
    @Setter
    @NonNull
    private String url;
}
