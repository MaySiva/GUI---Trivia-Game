package il.ac.tau.cs.sw1.bufferedIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**************************************
 *  Add your code to this class !!!   *
 **************************************/
public class MyBufferReader implements IBufferedReader {
    private FileReader fileReader;
    private int bufferSize;
    private boolean endOfLine;
    private boolean endOfFile;

    //private char [] buffer;
    /*
     * @pre: bufferSize > 0
     * @pre: fReader != null
     */
    public MyBufferReader(FileReader fReader, int bufferSize) {
        //Add your code here
        this.fileReader = fReader;
        this.bufferSize = bufferSize;
        this.endOfLine = false;
        this.endOfFile = false;


    }


    @Override
    public void close() throws IOException {
        //Leave this empty
    }


    @Override
    public String getNextLine() throws IOException {
        //Add your code here
        if (this.endOfFile)
            return null;
        String nextLine = "";
        char[] buffer = new char[this.bufferSize];
        for (int i = 0; i < this.bufferSize && !this.endOfLine; i++) {

            if (i + 1 == this.bufferSize) // End Of buffer we need to start from 0
                i = 0;

            if (this.fileReader.read(buffer, i, 1) != -1) { //End Of file

                if (buffer[i] != '\n')
                    nextLine += buffer[i];

                else if (buffer[i] == '\n')//End Of Line
                    this.endOfLine = true;

            } else {
                this.endOfFile = true;
                return nextLine;
            }
        }

        this.endOfLine = false;
        return nextLine;
    }
}
