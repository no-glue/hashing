package oa;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
public class OpenAddressing {
    private final int DEFAULT_TABLE_SIZE = 101;
    private float threshold = 0.75f;
    private int maxSize = 96;
    private int size = 0;

    HashEntry[] table;

    OpenAddressing() {
          table = new HashEntry[DEFAULT_TABLE_SIZE];
          for (int i = 0; i < DEFAULT_TABLE_SIZE; i++)
                table[i] = null;
    }

    void setThreshold(float threshold) {
          this.threshold = threshold;
          maxSize = (int) (table.length * threshold);
    }

    public long get(long key) {
    		int hash = hashFn(key);
          int initialHash = -1;
          while (hash != initialHash
                      && (table[hash] == DeletedEntry.getUniqueDeletedEntry()
                      || table[hash] != null
                      && table[hash].getKey() != key)) {
                if (initialHash == -1)
                      initialHash = hash;
                hash = (hash + 1) % table.length;
          }
          if (table[hash] == null || hash == initialHash)
                return -1;
          else
                return table[hash].getValue();
    }
    
    public int find(long key) {
    	int find_attempt = 1;
		int hash = hashFn(key);
      int initialHash = -1;
      while (hash != initialHash
                  && (table[hash] == DeletedEntry.getUniqueDeletedEntry()
                  || table[hash] != null
                  && table[hash].getKey() != key)) {
            if (initialHash == -1)
                  initialHash = hash;
            hash = (hash + 1) % table.length;
            find_attempt += 1;
      }
      if (table[hash] == null || hash == initialHash)
            return 0;
      else
            return find_attempt;
}
    public void put(long value) {
    	long key = value;
          int hash = hashFn(key);
          int initialHash = -1;
          int indexOfDeletedEntry = -1;
          while (hash != initialHash
                      && (table[hash] == DeletedEntry.getUniqueDeletedEntry()
                      || table[hash] != null
                      && table[hash].getKey() != key)) {
                if (initialHash == -1)
                      initialHash = hash;
                if (table[hash] == DeletedEntry.getUniqueDeletedEntry())
                      indexOfDeletedEntry = hash;
                hash = (hash + 1) % table.length;
          }
          if ((table[hash] == null || hash == initialHash)
                      && indexOfDeletedEntry != -1) {
                table[indexOfDeletedEntry] = new HashEntry(key, value);
                size++;
          } else if (initialHash != hash)
                if (table[hash] != DeletedEntry.getUniqueDeletedEntry()
                           && table[hash] != null && table[hash].getKey() == key)
                      table[hash].setValue(value);
                else {
                      table[hash] = new HashEntry(key, value);
                      size++;
                }
          if (size >= maxSize)
                resize();
    }
    private int hashFn(long key) {
    	int hash = (int) (key % table.length);
    	return hash;
	}

    private void resize() {
          int tableSize = 2 * table.length;
          maxSize = (int) (tableSize * threshold);
          HashEntry[] oldTable = table;
          table = new HashEntry[tableSize];
          size = 0;
          for (int i = 0; i < oldTable.length; i++)
                if (oldTable[i] != null
                           && oldTable[i] != DeletedEntry.getUniqueDeletedEntry())
                      put(oldTable[i].getValue());
    }

    public void remove(long l) {
    	int hash = hashFn(l);
          int initialHash = -1;
          while (hash != initialHash
                      && (table[hash] == DeletedEntry.getUniqueDeletedEntry()
                      || table[hash] != null
                      && table[hash].getKey() != l)) {
                if (initialHash == -1)
                      initialHash = hash;
                hash = (hash + 1) % table.length;
          }
          if (hash != initialHash && table[hash] != null) {
                table[hash] = DeletedEntry.getUniqueDeletedEntry();
                size--;
          }
    }

    public static void main(String[] args) { 
        OpenAddressing cMap = new OpenAddressing();
	    System.out.println("Enter File Name : ");
		Scanner scanner = new Scanner(System.in);
		FileReader fileReader;
		try {
			fileReader = new FileReader(scanner.nextLine());
			BufferedReader br = new BufferedReader(fileReader);
			String line;
			int sum = 0;
			double startTime = System.currentTimeMillis();
			long count = 0;
			while ((line = br.readLine()) != null) {
				String[] splited = line.split("\\s+");
				
				if (splited[0].equalsIgnoreCase("Insert")) {
					cMap.put(Long.parseLong(splited[1]));
				}
				else if (splited[0].equalsIgnoreCase("Find")) {
					sum += cMap.find(Long.parseLong(splited[1]));
				} else if (splited[0].equalsIgnoreCase("Remove") ) {
					cMap.remove(Long.parseLong(splited[1]));
				} 
				else {}
				count++;
				
			}
			System.out.println("Scanned  Lines  : "+count);
			System.out.println("Answer  : "+sum);
			double endTime = System.currentTimeMillis();
			double totalTime = (endTime - startTime) / 1000;
			System.out.println("Starting Time : " + startTime + " MilliSeconds");
			System.out.println("Ending Time : " + endTime + " MilliSeconds");
			System.out.println("Total Time : " + totalTime + " Seconds");
			br.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}