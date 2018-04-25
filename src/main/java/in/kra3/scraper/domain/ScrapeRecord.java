package in.kra3.scraper.domain;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
public class ScrapeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String jobId;
    private String title;
    private Date parsedDate;
    @Column(nullable = false)
    private String url;
    private String content;
}
