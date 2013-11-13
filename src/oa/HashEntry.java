package oa;

public class HashEntry {
    private long key;
    private long value;

    HashEntry(long key, long value) {
          this.key = key;
          this.value = value;
    }

    public long getValue() {
          return value;
    }

    public void setValue(long value) {
          this.value = value;
    }

    public long getKey() {
          return key;
    }
}