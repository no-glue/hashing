package oa;

public class HashEntry {
    private long key;
	private long value;
    
    HashEntry(long key, long value) {
        this.setKey(key);
        this.setValue(value);
  }

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

}