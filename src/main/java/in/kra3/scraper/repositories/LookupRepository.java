package in.kra3.scraper.repositories;

import com.sangupta.murmur.Murmur3;


public interface LookupRepository {
    int SEED = 0xB0F57EE3;

    String addJob(String url);

    boolean hasJobExists(String jobId);

    boolean isJobExistsForUrl(String url);


    default String getKeyForUrl(String url) {
        byte[] urlBytes = url.getBytes();
        long key = Murmur3.hash_x86_32(urlBytes, urlBytes.length, SEED);
        return String.valueOf(key);
    }
}
