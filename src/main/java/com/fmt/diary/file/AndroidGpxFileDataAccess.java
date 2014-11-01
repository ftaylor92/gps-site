package com.fmt.diary.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.fmt.comon.FileUtilitiesWeb;
import com.fmt.gps.data.GpxFileDataAccess;
import com.fmt.gps.track.TrackSegment;
import com.fmt.gps.track.Trip;
import com.fmt.servlet.UploadServlet;

public class AndroidGpxFileDataAccess extends GpxFileDataAccess {
	
	public static void setTextDescriptions(Trip trip) {
		for(TrackSegment s : trip.getSegments()) {
			s.setTextDescription(s.getTextDescription());
		}
	}
	
	public static void saveDiary() {
		//save diary everytime
		String diaryTxt= AndroidGpxFileDataAccess.makeGpxXml(UploadServlet.trip, true);
		try {
			FileUtilitiesWeb.writeFileLines(new File(UploadServlet.uploadPath, "latest.tvl"), diaryTxt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * saves Trip to file
	 * @param gpxFile which file to save to
	 * @param trip entire trip data
	 * @param context context
	 * @param diary whether to save diary info too
	 **/
	public static void savePoints(File gpxFile, Trip trip, boolean diary) {
		try {
			FileOutputStream fos = getFileOutputStream(gpxFile);
			
			fos.write(makeGpxXml(trip, diary).getBytes());
			
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
