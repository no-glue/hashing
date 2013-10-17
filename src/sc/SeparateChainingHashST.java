package sc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

public class SeparateChainingHashST<Key extends Long, Value extends Long> {
    private static final int INIT_CAPACITY = 4;

    // largest prime <= 2^i for i = 3 to 31
    // not currently used for doubling and shrinking
    // private static final int[] PRIMES = {
    //    7, 13, 31, 61, 127, 251, 509, 1021, 2039, 4093, 8191, 16381,
    //    32749, 65521, 131071, 262139, 524287, 1048573, 2097143, 4194301,
    //    8388593, 16777213, 33554393, 67108859, 134217689, 268435399,
    //    536870909, 1073741789, 2147483647
    // };

    private int N;                                // number of key-value pairs
    private int M;                                // hash table size
    private SequentialSearchST<Key, Value>[] st;  // array of linked-list symbol tables


    // create separate chaining hash table
    public SeparateChainingHashST() {
        this(INIT_CAPACITY);
    } 

    // create separate chaining hash table with M lists
    public SeparateChainingHashST(int M) {
        this.M = M;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[M];
        for (int i = 0; i < M; i++)
            st[i] = new SequentialSearchST<Key, Value>();
    } 

    // resize the hash table to have the given number of chains b rehashing all of the keys
    private void resize(int chains) {
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<Key, Value>(chains);
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys()) {
                temp.put(key, st[i].get(key));
            }
        }
        this.M  = temp.M;
        this.N  = temp.N;
        this.st = temp.st;
    }

    // hash value between 0 and M-1
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    } 

    // return number of key-value pairs in symbol table
    public int size() {
        return N;
    } 

    // is the symbol table empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // is the key in the symbol table?
    public boolean contains(Key key) {
        return get(key) != null;
    } 

    // return value associated with key, null if no such key
    public Value get(Key key) {
        int i = hash(key);
        return st[i].get(key);
    } 

    // insert key-value pair into the table
    public void put(Key key, Value val) {
        if (val == null) { delete(key); return; }

        // double table size if average length of list >= 10
        if (N >= 10*M) resize(2*M);

        int i = hash(key);
        if (!st[i].contains(key)) N++;
        st[i].put(key, val);
    } 

    // delete key (and associated value) if key is in the table
    public void delete(Key key) {
        int i = hash(key);
        if (st[i].contains(key)) N--;
        st[i].delete(key);

        // halve table size if average length of list <= 2
        if (M > INIT_CAPACITY && N <= 2*M) resize(M/2);
    } 

    // return keys in symbol table as an Iterable
    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys())
                queue.enqueue(key);
        }
        return queue;
    }
    public Set<Key> getKeys() {
    	 Set<Key> set = new HashSet<Key>();
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys()) {
            	set.add(key);
            }
        }
        return set;
    } 

    public Value findmax() {
    	Set<Key> keys = getKeys();
    	  Key max = Collections.max(keys);
    	  Value v = get(max);
    	  return v;
    }
    
    public Value findmin() {
    	Set<Key> keys = getKeys();
    	  Key min = Collections.min(keys);
    	  Value v = get(min);
		return v;
    }
/*    
    public long removeValues(Value value) {
		long counter = 0;
	for(Entry<K, V> entryMap : getKeys()) {
		if(entryMap.getValue().equals(value)) {
			remove(entryMap.getKey());
			counter++;
		}
	}
	return counter;
}
*/


    public static void main(String[] args) { 
        SeparateChainingHashST<Long, Long> cMap = new SeparateChainingHashST<Long, Long>();
       /* st.put("A",123123);
        st.put("B",123122);
        st.put("C",123121);
        st.put("D",123125);
        st.put("E",123124);
        st.put("F",123120);
        // print keys
        for (String s : st.keys()) 
            System.out.println((s + " " + st.get(s)));  */
        long sum=0;
        System.out.println("Enter File Name : ");
		Scanner scanner = new Scanner(System.in);
		FileReader fileReader;
		try {
			fileReader = new FileReader(scanner.nextLine());
			BufferedReader br = new BufferedReader(fileReader);
			String line;
			int counter = 1;
			double startTime = System.currentTimeMillis();
			while ((line = br.readLine()) != null) {
				String[] splited = line.split("\\s+");
				
				if (splited[0].equalsIgnoreCase("Insert")) {
				//	cMap.insert(Long.parseLong(splited[1]), Long.parseLong(splited[2]));
					cMap.put(Long.parseLong(splited[1]), Long.parseLong(splited[2]));
				}
				else if (splited[0].equalsIgnoreCase("Find")) {
					if (!cMap.isEmpty()) {
					//	cMap.find(Long.parseLong(splited[1]));
						cMap.get(Long.parseLong(splited[1]));
					}
				else {
					System.out.println("SkipList Empty");
				}
				} else if (splited[0].equalsIgnoreCase("FindMin")) {
			//		long value  = cMap.findmin();
			//		sum += value;
					
				} else if (splited[0].equalsIgnoreCase("FindMax")) {
			//		long value  = cMap.findmax();
			//		sum += value;
					
				} else if (splited[0].equalsIgnoreCase("Remove") ) {
				//	cMap.remove(Long.parseLong(splited[1]));
					cMap.delete(Long.parseLong(splited[1]));
				}  else if (splited[0].equalsIgnoreCase("RemoveValue")) {
					//	long value  = cMap.removeValues(Long.parseLong(splited[1]));
					//	sum += value;
				}
				 else if (splited[0].equalsIgnoreCase("Size")) {
						long value  = cMap.size();
						sum += value;
				}else {}
				System.out.println("Scanning Line : "+counter);
				counter +=1;
			}
			double endTime = System.currentTimeMillis();
			double totalTime = (endTime - startTime) / 1000;
			System.out.println("Answer = "+sum);
			
			System.out.println("Starting Time : " + startTime + " MilliSeconds");
			System.out.println("Ending Time : " + endTime + " MilliSeconds");
			System.out.println("Total Time : " + totalTime + " Seconds");
			br.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
