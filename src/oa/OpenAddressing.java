package oa;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
public class OpenAddressing {
    private final static int TABLE_SIZE = 1000;

    HashEntry[] table;

    OpenAddressing() {
          table = new HashEntry[TABLE_SIZE];
          for (int i = 0; i < TABLE_SIZE; i++)
                table[i] = null;
    }

    public long get(long key) {
          int hash = (int) (key % TABLE_SIZE);
          int initialHash = -1;
          while (hash != initialHash
                      && (table[hash] == DeletedEntry.getUniqueDeletedEntry() || table[hash] != null
                                 && table[hash].getKey() != key)) {
                if (initialHash == -1)
                      initialHash = hash;
                hash = (hash + 1) % TABLE_SIZE;
          }
          if (table[hash] == null || hash == initialHash)
                return -1;
          else
                return table[hash].getValue();
    }

    public void put(long key, long value) {
          int hash = (int) (key % TABLE_SIZE);
          int initialHash = -1;
          int indexOfDeletedEntry = -1;
          while (hash != initialHash
                      && (table[hash] == DeletedEntry.getUniqueDeletedEntry() || table[hash] != null
                                 && table[hash].getKey() != key)) {
                if (initialHash == -1)
                      initialHash = hash;
                if (table[hash] == DeletedEntry.getUniqueDeletedEntry())
                      indexOfDeletedEntry = hash;
                hash = (hash + 1) % TABLE_SIZE;
          }
          if ((table[hash] == null || hash == initialHash)
                      && indexOfDeletedEntry != -1)
                table[indexOfDeletedEntry] = new HashEntry(key, value);
          else if (initialHash != hash)
                if (table[hash] != DeletedEntry.getUniqueDeletedEntry()
                           && table[hash] != null && table[hash].getKey() == key)
                      table[hash].setValue(value);
                	else
                      table[hash] = new HashEntry(key, value);
    }

    public void remove(long key) {
          int hash = (int) (key % TABLE_SIZE);
          int initialHash = -1;
          while (hash != initialHash
                      && (table[hash] == DeletedEntry.getUniqueDeletedEntry() || table[hash] != null
                                 && table[hash].getKey() != key)) {
                if (initialHash == -1)
                      initialHash = hash;
                hash = (hash + 1) % TABLE_SIZE;
          }
          if (hash != initialHash && table[hash] != null)
                table[hash] = DeletedEntry.getUniqueDeletedEntry();
    }
    
    public static void main(String[] args) { 
        OpenAddressing cMap = new OpenAddressing();
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
					cMap.get(Long.parseLong(splited[1]));
					
					/*if (!cMap.isEmpty()) {
					//	cMap.find(Long.parseLong(splited[1]));
						
					}
				else {
					System.out.println("SkipList Empty");
				}
				*/
				} else if (splited[0].equalsIgnoreCase("FindMin")) {
			//		long value  = cMap.findmin();
			//		sum += value;
					
				} else if (splited[0].equalsIgnoreCase("FindMax")) {
			//		long value  = cMap.findmax();
			//		sum += value;
					
				} else if (splited[0].equalsIgnoreCase("Remove") ) {
				//	cMap.remove(Long.parseLong(splited[1]));
					cMap.remove(Long.parseLong(splited[1]));
				}  else if (splited[0].equalsIgnoreCase("RemoveValue")) {
					//	long value  = cMap.removeValues(Long.parseLong(splited[1]));
					//	sum += value;
				}
				 else if (splited[0].equalsIgnoreCase("Size")) {
				//		long value  = cMap.size();
				//		sum += value;
				}else {}
				System.out.println("Scanning Line : "+counter);
				counter +=1;
			}
			System.out.println(cMap.get(Long.parseLong("7197966325281384634")));
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