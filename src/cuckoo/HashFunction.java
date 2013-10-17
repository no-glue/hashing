package cuckoo;


public interface HashFunction<T extends Long> {
    /**
     * Given an object, returns the hash code of that object.
     *
     * @param obj The object whose hash code should be computed.
     * @return The object's hash code.
     */
    public int hash(T obj);
}