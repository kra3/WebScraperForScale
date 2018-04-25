package in.kra3.scraper.repositories;

import com.sangupta.murmur.Murmur3;

import static com.sangupta.murmur.MurmurConstants.LONG_MASK;

public interface LookupRepository {
    int SEED = 0xB0F57EE3;
    StringBuilder builder = new StringBuilder(100);

    void addJob(String url, String jobId);

    boolean isJobExists(String url);

    String getJobId(String url);

    default String getKeyForUrl(String url) {
        byte[] urlBytes = url.getBytes();
        long[] longs = Murmur3.hash_x64_128(urlBytes, urlBytes.length, SEED);

        builder.setLength(0);

        int i1 = (int) (longs[0] >>> 32);
        int i2 = (int) (longs[0] & LONG_MASK);
        int i3 = (int) (longs[1] >>> 32);
        int i4 = (int) (longs[1] & LONG_MASK);

        builder.append(String.valueOf(i2));
        builder.append(',');
        builder.append(String.valueOf(i1));
        builder.append(',');
        builder.append(String.valueOf(i4));
        builder.append(',');
        builder.append(String.valueOf(i3));

        return builder.toString();
    }
}
