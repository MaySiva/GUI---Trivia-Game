package il.ac.tau.cs.sw1.bufferedIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class MyFileReader extends FileReader{

	private int readsCounter = 0;
	public MyFileReader(File arg0) throws FileNotFoundException {
		super(arg0);
	}

	@Override
	public int read(char[] arg0, int arg1, int arg2) throws IOException {
		readsCounter++;
		return super.read(arg0, arg1, arg2);
	}
	
	@Override
	public int read() throws IOException {
		readsCounter++;
		return super.read();
	}

	public int getReadsCount(){
		return readsCounter;
	}
	

}
