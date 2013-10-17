package cuckoo;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;

/**
 * Cuckoo hashing allows closer to perfect hashing by using 2 different hashcodes
 * that allow 2 positions in the table for each key-value pair
 * 
 * @author Cam
 * @version 1.0, 7/20/2012
 */
public class CuckooHashImpl<K, V> {

        private int CAPACITY;
        private int a = 37, b = 17;
        private Bucket<K, V>[] table;
  
        /**
         * test meth
         */
    public static void main(String[] args) {
            
            CuckooHashImpl<String, String> cMap = new CuckooHashImpl<String, String>(10);
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
    					//cMap.insert(Long.parseLong(splited[1]), Long.parseLong(splited[2]));
    					cMap.put(splited[1], splited[2]);
    				}
    				else if (splited[0].equalsIgnoreCase("Find")) {
//    					if (!cMap.isEmpty()) {
//    						cMap.find(Long.parseLong(splited[1]));
//    					}
//    				else {
//    					System.out.println("SkipList Empty");
//    				}
    				} else if (splited[0].equalsIgnoreCase("FindMin")) {
    			//		long value  = cMap.findmin();
    				//	sum += value;
    					
    				} else if (splited[0].equalsIgnoreCase("FindMax")) {
    				//	long value  = cMap.findmax();
    				//	sum += value;
    					
    				} else if (splited[0].equalsIgnoreCase("Remove") ) {
    					//cMap.remove(Long.parseLong(splited[1]));
    					cMap.remove(splited[1]);
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
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        
        //table.rehash();
        //System.out.println(table.toString());
    		System.out.println(cMap.toString() +"    " + cMap.size());
        cMap.clear();
        System.out.println(cMap.toString() +"    " + cMap.size());
    }
        
        /**
         * Inner bucket class
         * @param <K> - type of key
         * @param <V> - type of value
         */
        private class Bucket<K, V> {
                private K bucKey = null;
                private V value = null;
                
                public Bucket(K k, V v) {
                        bucKey = k; 
                        value = v;
                }
                
                /**
                 * Getters and Setters
                 */
                private K getBucKey() {
                        return bucKey;
                }
                //private void setBucKey(K key) {
                        //bucKey = key;
                //}
                private V getValue() {
                        return value;
                }
                //private void setValue(V value) {
                        //this.value = value;
                //}
        }
        
        /**
         * a two parameter constructor that sets capacity and the loadfactor limit 
         * @param size user input multimap capacity
         * @param lf user input load factor limit
         */
        public CuckooHashImpl (int size) {
                CAPACITY = size;
                table = new Bucket[CAPACITY];
        }                                                  
        
        /**
         * Get the number of elements in the table
         * @return total key-value pairs
         */
        public int size() {
                int count = 0;
                for (int i=0; i<CAPACITY; ++i) {
                        if (table[i] != null)
                                count++;         
                }
                return count;
        }
        
        /**
         * Removes all elements in the table
         */
        public void clear() {
                table = new Bucket[CAPACITY]; 
        }
        
        /**
         * Get a list of all values in the table
         * @return the values as a list
         */
        public List<V> values() {
                List<V> allValues = new ArrayList<V>(); 
                for (int i=0; i<CAPACITY; ++i) {
                        if (table[i] != null) {
                                allValues.add(table[i].getValue());
                        }
                }
                return allValues;
        }
        
        /**
         * Get all the keys in the table
         * @return a set of keys
         */
        public Set<K> keys() {
                Set<K> allKeys = new HashSet<K>();
                for (int i=0; i<CAPACITY; ++i) {
                        if (table[i] != null) {
                                allKeys.add(table[i].getBucKey());
                        }
                }
                return allKeys;
        }
        
        /**
         * Adds a key-value pair to the table by means of cuckoo hashing.
         * Insert into either of 2 separate hash indices, if both are full loop to
         * next hashed index and displace the key-value pair there, else loop for
         * n = size times.
         * @param key the key of the element to add
     * @param value the value of the element to add
         */
        public void put(K key, V value) {
                int index = (int)key.hashCode() % CAPACITY;
                int index2 = (int)altHashCode(key) % CAPACITY;
                
                if (table[index] != null && table[index].getValue().equals(value))
                        return;
                if (table[index2] != null && table[index2].getValue().equals(value))
                        return;
                int pos = index;
                Bucket<K, V> buck = new Bucket<K, V>(key, value);
                for (int i=0; i<3; i++) {
                        if (table[pos] == null) {
                                table[pos] = buck;
                                return;
                        }
                        else {
                                Bucket<K, V> copy = table[pos];
                                table[pos] = buck;
                                buck = copy;
                        }
                        if (pos == index)
                                pos = index2;
                        else
                                pos = index;
                }
                rehash();
                put(key, value);
        }
        
        /**
         * Retrieve a value in O(1) time based on the key b/c it can only be in 1 of 2 locations
         * @param key Key to search for
         * @return the found value or null if it doesn't exist
         */
        public V get(K key) {
                int index = key.hashCode() % CAPACITY;
                int index2 = altHashCode(key) % CAPACITY;
                if (table[index] != null && table[index].getBucKey().equals(key))
                        return table[index].getValue();
                else if (table[index2] != null && table[index2].getBucKey().equals(key))
                        return table[index2].getValue();
                return null;
        }
        
        /**
         * Secondary custom hashcoder for cuckoo hashing
         * @param key The key to be hashed
         * @return hashcode
         */
        public int altHashCode(K key) {
                return a * b + key.hashCode();
        }
        
        /**
         * Removes this key value pair from the table
         * @param key the key to remove
         * @param value the value to remove
         * @return successful removal
         */
        public boolean remove(K key) {
        	V value = get(key);
                int index = key.hashCode() % CAPACITY;
                int index2 = altHashCode(key) % CAPACITY;
                if (table[index] != null && table[index].getValue().equals(value)) {
                        table[index] = null;
                        return true;
                }
                else if (table[index2] != null && table[index2].getValue().equals(value)) {
                        table[index2] = null;
                        return true;
                }
                return false;
        }
        
        /**
         * String representaiton of the table
         * @return the table's contents
         */
        public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("[ ");
                for (int i=0; i<CAPACITY; ++i) {
                        if (table[i] != null) {
                                sb.append("<");
                                sb.append(table[i].getBucKey()); //key
                                sb.append(", ");
                                sb.append(table[i].getValue()); //value
                                sb.append("> ");
                        }
                }
                sb.append("]");
                return sb.toString();
        }
        
        /**
         * a method that regrows the hashtable to capacity: 2*old capacity + 1 and 
         * reinserts all the key->value pairs.
         */
        public void rehash() {
                Bucket<K, V>[] tableCopy = table.clone();
                int OLD_CAPACITY = CAPACITY;
                CAPACITY = (CAPACITY * 2) + 1;
                table = new Bucket[CAPACITY];
                
                for (int i=0; i<OLD_CAPACITY; ++i) {
                        if (tableCopy[i] != null) {
                                put(tableCopy[i].getBucKey(), tableCopy[i].getValue());
                        }
                }
                //reset alt hash func
                Random gen = new Random();
                a = gen.nextInt(37);
                b = gen.nextInt(17);
        }
        
}