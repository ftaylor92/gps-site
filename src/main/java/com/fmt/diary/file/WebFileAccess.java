package com.fmt.diary.file;

import java.io.File;
import java.io.FilenameFilter;

import com.fmt.servlet.UploadServlet;

public class WebFileAccess {
	
	/**
	 * Returns the appropriate GPX or Diary File for this User. (unsafe, not thread safe)
	 * @param forTravelog whether user wants driary or GPX file type
	 * @return appropriate file
	 **/
	public static File getGpxFile(final String extension) {
		FilenameFilter filter= new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return(filename.toLowerCase().endsWith("kml") || filename.toLowerCase().endsWith("tvl") || filename.toLowerCase().endsWith(extension /*forTravelog ? "tvl" : "gpx"*/));
			}
		};
		
		final File filesDir= new File(UploadServlet.uploadPath);
		System.out.printf("Finding %s files in %s", extension, filesDir.getAbsolutePath());
		String[] files= filesDir.list(filter);
		
		File foundFile= null;
		
		for(String aFile : files) {
			if(null != UploadServlet.USER_ID && aFile.contains(UploadServlet.USER_ID)) {
				foundFile= new File(UploadServlet.uploadPath, aFile);
			} else if(null == foundFile) {
				foundFile= new File(UploadServlet.uploadPath, aFile);
			}
		}
		
		return foundFile;
	}
}
