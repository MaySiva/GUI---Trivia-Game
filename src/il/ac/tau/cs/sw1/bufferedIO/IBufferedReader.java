package il.ac.tau.cs.sw1.bufferedIO;

import java.io.Closeable;
import java.io.IOException;

/* don't change this interface */

public interface IBufferedReader extends Closeable{
	
	/*
	 * @post: if there are still data to read, $ret = the next line.
	 * @post: if there is no next line to read, $ret = null
	 */
	public String getNextLine() throws IOException;
}
