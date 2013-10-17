package cuckoo;


public interface UniversalHashFunction<T extends Long> {
    /**
     * Given as input the number of buckets, produces a random hash function
     * that hashes objects of type T into those buckets with the guarantee
     * that
     *<pre>
     *             Pr[h(x) == h(y)] <= |T| / numBuckets, forall x != y
     *</pre>
     * For all hash functions handed back.
     *
     * @param buckets The number of buckets into which elements should be
     *                partitioned.
     * @return A random hash function whose distribution satisfies the above
     *         property.
     */
    public HashFunction<T> randomHashFunction(int buckets);
}