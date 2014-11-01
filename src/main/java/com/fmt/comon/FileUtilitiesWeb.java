/**
 * 
 */
package com.fmt.comon;

import java.io.*;
import java.util.*;

public class FileUtilitiesWeb {
	/**
	 * returns a list of a text file's text lines
	 * @param aFile file to get lines of text from
	 * @return a list of all the text lines from the text file
	 **/
	public static List<String> getFileLines( File aFile ) throws IOException
	{
		List<String> lines= new ArrayList<String>(100);

		String aLine;
		BufferedReader in= new BufferedReader(new FileReader(aFile));

		while(in.ready())
		{
			aLine= in.readLine();
			lines.add(aLine);
		}
		in.close();

		return lines;
	}

	public static void writeFileLines(File aFile, String lines) throws IOException {
		FileOutputStream fos= new FileOutputStream(aFile);
		PrintWriter pw= new PrintWriter(aFile);
		pw.printf(lines);
		pw.flush();
		pw.close();
		fos.flush();
		fos.close();
	}
}
