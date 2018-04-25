package in.kra3.scraper.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;


@Repository
public class LookupRepositoryImpl implements LookupRepository {
    private static final String KEY = "Urls";
    private StringRedisTemplate redisTemplate;
    private HashOperations hashOperations;

    @Autowired
    public LookupRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void addJob(String url, String jobId) {
        hashOperations.put(KEY, getKeyForUrl(url), jobId);
    }

    @Override
    public boolean isJobExists(String url) {
        return hashOperations.hasKey(KEY, getKeyForUrl(url));
    }

    @Override
    public String getJobId(String url) {
        return (String) hashOperations.get(KEY, getKeyForUrl(url));
    }
}
