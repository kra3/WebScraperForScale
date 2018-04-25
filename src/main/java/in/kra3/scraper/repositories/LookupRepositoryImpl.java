package in.kra3.scraper.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;


@Repository("redis:lookup-repo")
public class LookupRepositoryImpl implements LookupRepository {
    // PS: redis implementation could be changed to a bloom filter to save memory.

    private static final String KEY = "Urls";
    private StringRedisTemplate redisTemplate;
    private SetOperations setOperations;

    @Autowired
    public LookupRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        setOperations = redisTemplate.opsForSet();
    }

    @Override
    public String addJob(String url) {
        String jobId = getKeyForUrl(url);
        setOperations.add(KEY, jobId);
        return jobId;
    }

    @Override
    public boolean hasJobExists(String jobId) {
        return setOperations.isMember(KEY, jobId);
    }

    @Override
    public boolean isJobExistsForUrl(String url) {
        return setOperations.isMember(KEY, getKeyForUrl(url));
    }
}
