package in.kra3.scraper.controllers;

import in.kra3.scraper.data.ScrapeJob;
import in.kra3.scraper.data.ScrapeResult;
import in.kra3.scraper.services.ScrapeResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
public class ScrapeJobController {
    private ScrapeResultService scrapeResultService;

    @Autowired
    public ScrapeJobController(ScrapeResultService scrapeResultService) {
        this.scrapeResultService = scrapeResultService;
    }

    @PostMapping(path = "/", consumes = "application/json")
    @ResponseBody
    Map<String, String> submitJob(@RequestBody ScrapeJob job) {
        try {
            String jobId = scrapeResultService.submitJob(job.getUrl());
            Map<String, String> map = new HashMap<>();
            map.put("jobId", jobId);
            return map;  // @todo: 201 created.
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid url OR other errors"); // @todo: 400 bad request
        }
    }

    @GetMapping(path = "/{jobId}", produces = "application/json")
    @ResponseBody
    ScrapeResult getResult(@PathVariable String jobId) {
        return scrapeResultService.getResult(jobId);
    }

}