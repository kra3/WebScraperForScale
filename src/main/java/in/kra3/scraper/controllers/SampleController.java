package in.kra3.scraper.controllers;

import in.kra3.scraper.data.ScrapeJob;
import in.kra3.scraper.data.ScrapeResult;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
public class SampleController {

    @PostMapping(path = "/", consumes = "application/json")
    @ResponseBody
    Map<String, String> submitJob(@RequestBody ScrapeJob job) {
        if (! isValidUrl(Optional.ofNullable(job.getUrl()))) {
            throw new IllegalArgumentException("url is not valid");  // @todo: 400 bad request
        }
        // @todo: checks url in redis for duplicates
        // @todo: if duplicate, get job id for the url
        // @todo: otherwise create a job and enqueue, set redis key
        Map<String, String> map = new HashMap<>();
        map.put("jobId", "");
        return map;  // @todo: 201 created.
    }

    @GetMapping(path = "/{jobId}", produces = "application/json")
    @ResponseBody
    ScrapeResult getResult(@PathVariable Long jobId) {
        // @todo: check if job exists
        // @todo: read job data from db
        return new ScrapeResult();
    }

    private boolean isValidUrl(Optional<String> mayBeUrl) {
        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(mayBeUrl.orElse(""));
    }
}