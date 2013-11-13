package cuckoo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;



public class Cuckoo<AnyType>
{
 
    public Cuckoo( HashFamily<? super AnyType> hf )
    {
        this( hf, DEFAULT_TABLE_SIZE );
    }

    /**
     * Construct the hash table.
     * @param size the approximate initial size.
     */
    public Cuckoo( HashFamily<? super AnyType> hf, int size )
    {
        hashFunctions = hf;
        numHashFunctions = hf.getNumberOfFunctions( );
        subTableSize = nextPrime( size / numHashFunctions );
        subTableStarts = new int[ numHashFunctions ];
        for( int i = 0; i < numHashFunctions; i++ )
            subTableStarts[ i ] = i * subTableSize;
        
        allocateArray( subTableSize * numHashFunctions );
        doClear( );
    }

    
    private static final double MAX_LOAD = 0.49;
    private static final int ALLOWED_REHASHES = 100;
    
    private int rehashes = 0;
    
    /**
     * Insert into the hash table. If the item is
     * already present, return false.
     * @param x the item to insert.
     */
    public boolean insert( AnyType x )
    {         
    	final int COUNT_LIMIT = 100;
        
        if( contains( x ) )
            return false;
        
        if( currentSize >= array.length * MAX_LOAD )
            expand( );

        for( int i = 0, which = 0; i < COUNT_LIMIT; i++, which++ )
        {
            if( which == numHashFunctions )
                which = 0;
            
            int pos = myhash( x, which );

            if( array[ pos ] == null )
            {
                array[ pos ] = x;
                currentSize++;
                return true;
            }

            AnyType tmp = array[ pos ];
            array[ pos ] = x;
            x = tmp;
        }
            
        if( rehashes++ >= ALLOWED_REHASHES )
        {
            expand( );
            rehashes = 0;
        }
        else
            rehash( );
        
        return insert( x );
    }

    protected int myhash( AnyType x, int which )
    {
        int hashVal = hashFunctions.hash( x, which );
        
        hashVal %= subTableSize;
        if( hashVal < 0 )
            hashVal += subTableSize;
        
        return hashVal + subTableStarts[ which ];
    }
    
    private void expand( )
    {
        rehash( numHashFunctions * nextPrime( (int) ( array.length / MAX_LOAD  / numHashFunctions ) ) );
    }
    
    private void rehash( )
    {
        //System.out.println( "NEW HASH FUNCTIONS " + array.length );
        hashFunctions.generateNewFunctions( );
        rehash( array.length );
    }
    
    private void rehash( int newLength )
    {
     //   System.out.println( "REHASH: " + array.length + " " + newLength + " " + currentSize );
        
        AnyType [ ] oldArray = array;    // Create a new double-sized, empty table
        
        if( newLength != array.length )
        {
            subTableSize = newLength / numHashFunctions;
            for( int i = 0; i < numHashFunctions; i++ )
                subTableStarts[ i ] = i * subTableSize;
        }
            
        allocateArray( newLength );
        
        currentSize = 0;
        
            // Copy table over
        for( AnyType str : oldArray )
            if( str != null )
                insert( str );
    }
    
    /**
     * Gets the size of the table.
     * @return number of items in the hash table.
     */
    public int size( )
    {
        return currentSize;
    }
    
    /**
     * Gets the length (potential capacity) of the table.
     * @return length of the internal array in the hash table.
     */
    public int capacity( )
    {
        return array.length;
    }
    
    /**
     * Method that searches two places.
     * @param x the item to search for.
     * @return the position where the search terminates, or -1 if not found.
     */
    private int findPos( AnyType x )
    {
    	for( int i = 0; i < numHashFunctions; i++ )
        {
        	
            int pos = myhash( x, i );
            if( array[ pos ] != null && array[ pos ].equals( x ) )
                return 0;    
        }
        return -1;
    }
    private int find( AnyType x )
    {
    	int find_attempt = 0;
        for( int i = 0; i < numHashFunctions; i++ )
        {
        	
            int pos = myhash( x, i );
            find_attempt +=1;
            if( array[ pos ] != null && array[ pos ].equals( x ) ){
                return find_attempt;
            }
         }
        return find_attempt;
    }
    /**
     * Remove from the hash table.
     * @param x the item to remove.
     * @return true if item was found and removed
     */
    public boolean remove( AnyType x )
    {
        int pos = findPos( x );
        
        if( pos != -1 )
        {
            array[ pos ] = null;
            currentSize--;
        }
        
        return pos != -1;
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return the matching item.
     */
    public boolean contains( AnyType x )
    {
        return findPos( x ) != -1;
    }
    
    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty( )
    {
        doClear( );
    }

    private void doClear( )
    {
        currentSize = 0;
        for( int i = 0; i < array.length; i++ )
            array[ i ] = null;
    }
    

    
    private static final int DEFAULT_TABLE_SIZE = 101;

    private final HashFamily<? super AnyType> hashFunctions;
    private final int numHashFunctions;
    private AnyType [ ] array; // The array of elements
    private int currentSize; 
    private int subTableSize;
    private int [ ] subTableStarts;

    private void allocateArray( int arraySize )
    {
        array = (AnyType[]) new Object[ arraySize ];
    }

    private static int nextPrime( int n )
    {
        if( n % 2 == 0 )
            n++;

        for( ; !isPrime( n ); n += 2 )
            ;

        return n;
    }

    private static boolean isPrime( int n )
    {
        if( n == 2 || n == 3 )
            return true;

        if( n == 1 || n % 2 == 0 )
            return false;

        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 )
                return false;

        return true;
    }


    // Simple main
    public static void main( String [ ] args )
    {
        Cuckoo<String> H = new Cuckoo<String>( new StringHashFamily( 2 ) );

        java.util.Random r = new java.util.Random( );
        HashSet<String> t = new HashSet<String>( );
       
       System.out.println("Enter File Name : ");
		Scanner scanner = new Scanner(System.in);
		FileReader fileReader;
		try {
			fileReader = new FileReader(scanner.nextLine());
			BufferedReader br = new BufferedReader(fileReader);
			String line;
			long sum = 0;
			double startTime = System.currentTimeMillis();
			long count = 0;
			while ((line = br.readLine()) != null) {
				String[] splited = line.split("\\s+");
				
				if (splited[0].equalsIgnoreCase("Insert")) {
					 H.insert( splited[1]);
				}
				else if (splited[0].equalsIgnoreCase("Find")) {
					sum += H.find(splited[1]);
				}
				else if (splited[0].equalsIgnoreCase("Remove") ) {
					H.remove(splited[1]);
				}
				else {}
				count++;
				
			}
			System.out.println("Scanned  Lines   :" +count);
			
			System.out.println("Answer   :" +sum);
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
