package sc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SeparateChaining<AnyType>
{
   
	public SeparateChaining( )
    {
        this( DEFAULT_TABLE_SIZE );
    }

    public SeparateChaining( int size )
    {
        theLists = new LinkedList[ nextPrime( size ) ];
        for( int i = 0; i < theLists.length; i++ )
            theLists[ i ] = new LinkedList<AnyType>( );
    }
    public void insert( AnyType x )
    {
        List<AnyType> whichList = theLists[ myhash( x ) ];
        if( !whichList.contains( x ) )
        {
            whichList.add( x );

            if( ++currentSize > theLists.length )
                rehash( );
        }
    }

    public void remove( AnyType x )
    {
        List<AnyType> whichList = theLists[ myhash( x ) ];
        if( whichList.contains( x ) )
    {
        whichList.remove( x );
            currentSize--;
    }
        
    }

    public boolean contains( AnyType x )
    {
    	
        List<AnyType> whichList = theLists[ myhash( x ) ];
        return whichList.contains( x );
    }

    public int find( AnyType x )
    {
    	int find_attempt = 1;
    	List<AnyType> whichList = theLists[ myhash( x ) ];
    	if(whichList.contains(x)){
    		find_attempt += whichList.indexOf(x);
    	}
    	else {
    		find_attempt = 0;
    	}
        return find_attempt;
    }
    
    public void makeEmpty( )
    {
        for( int i = 0; i < theLists.length; i++ )
            theLists[ i ].clear( );
        currentSize = 0;    
    }

    public static int hash( String key, int tableSize )
    {
        int hashVal = 0;

        for( int i = 0; i < key.length( ); i++ )
            hashVal = 37 * hashVal + key.charAt( i );

        hashVal %= tableSize;
        if( hashVal < 0 )
            hashVal += tableSize;

        return hashVal;
    }

    private void rehash( )
    {
        List<AnyType> [ ]  oldLists = theLists;

            // Create new double-sized, empty table
        theLists = new List[ nextPrime( 2 * theLists.length ) ];
        for( int j = 0; j < theLists.length; j++ )
            theLists[ j ] = new LinkedList<AnyType>( );

            // Copy table over
        currentSize = 0;
        for( List<AnyType> list : oldLists )
            for( AnyType item : list )
                insert( item );
    }

    private int myhash( AnyType x )
    {
        int hashVal = x.hashCode( );
        hashVal %= theLists.length;
        if( hashVal < 0 )
            hashVal += theLists.length;
        return hashVal;
    }
    
    private static final int DEFAULT_TABLE_SIZE = 101;
       
    private List<AnyType> [ ] theLists; 
    private int currentSize;

    private static int nextPrime( int n )
    {
        if( n % 2 == 0 )
            n++;

        for( ; !isPrime( n ); n += 2 )
            ;
        return n;
    }
    
    public String toString()
    {
    	String output = "";
    	 for( int j = 0; j < theLists.length; j++ ){
    		 output += "\n"+j+".  \t"+theLists[ j ].toString();
    	 }
		return output;
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

    public static void main( String [ ] args )
    {
        SeparateChaining<Long> H = new SeparateChaining<Long>( );

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
      					 H.insert( Long.parseLong(splited[1]));
      				}
      				else if (splited[0].equalsIgnoreCase("Find")) {
      					sum +=H.find(Long.parseLong(splited[1]));
      				}
      				else if (splited[0].equalsIgnoreCase("Remove") ) {
      					H.remove(Long.parseLong(splited[1]));
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

