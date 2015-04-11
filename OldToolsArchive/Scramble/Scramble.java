import java.io.*;
import java.util.*;

public class Scramble {

	private int RandomList[];
	private String suffix = null;
	private String SourceList[];
	private File ScrambleDir;
	private Process mChild = null;

	public void getRandomList( int size ) {

		boolean found = false;
		Random r = new Random();
		RandomList = new int[ size ];

		int count = 0;
		int i = 0;
		int j = 0;

		while( count < size ) {
			i = r.nextInt( 99999 );
			while ( j < size ) {
				if( RandomList[ j ] == i  ) {
					found = true;
				}
				j++;
			}
			if( !found ) {
				RandomList[ count ] = i;
				count++;
			}
			found = false;
		}
	}

	public int getSourceList() {

		File dir = new File( "." );
		String[] list = dir.list();

		int count = 0;
		int i = 0;

		while( i < list.length ) {
			if( list[ i ].endsWith( suffix ) ) {
				count++;
			}
			i++;
		}
		SourceList = new String[ count ];

		i = 0;
		int j = 0;

		while( i < list.length ) {
			if( list[ i ].endsWith( suffix ) ) {
				SourceList[ j ] = list[ i ];
				j++;
			}
			i++;
		}

		return count;
	}

	public void copyFiles() {

		int i = 0;

		while( i < SourceList.length ) {

			File srcFile = new File( SourceList[i] );
			File destFile = new File( "scrambled", RandomList[i] + "." + suffix );

			try {
				FileCopy.copy( srcFile, destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
		}
	}

	public static void main( String args[] ) {

		Scramble s = new Scramble();
		s.suffix = args[0];
		int size = s.getSourceList();

		s.getRandomList( size );
		s.copyFiles();

	}
}
