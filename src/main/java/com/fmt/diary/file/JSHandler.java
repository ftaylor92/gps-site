package com.fmt.diary.file;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.fmt.gps.track.TrackPoint;
import com.fmt.gps.track.TrackSegment;
import com.fmt.gps.track.Trip;
import com.fmt.servlet.UploadServlet;

/** Handles JavaScript request/response. **/
public class JSHandler {
	private final String tag= "JSHandler";

	/**
	 * Returns TripSegments as JSON.
	 * @return JSON Trip object
	 **/
	public static String getDiaryJson() {

		String json= null;

		ObjectMapper mapper= new ObjectMapper();
		try {
			json= mapper.writeValueAsString(UploadServlet.trip);
			//System.out.println(json);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * Edits text description of TrackSegment.
	 * @param segNum which segment to edit
	 * @param newDesc new description of segment, or if an empty string makes no changes to segment
	 * @return current description of segment.
	 **/
	public static String editText(int segNum, String newDesc) {
		TrackSegment ts= UploadServlet.trip.getSegments().get(segNum);
		System.out.println("oldTextDesc: "+ ts.getTextDescription());
		System.out.println("newDesc: "+ newDesc);

		/*EditDiary.editDialog(context, ts);
		int pos= newHtml.indexOf("value=");
		if(-1 != pos) {
			String partialHtml= newHtml.substring(pos+7);
			pos= partialHtml.indexOf("\"");
			String newDesc= partialHtml.substring(0, pos);
			if(GpsDiary.LOGG) Log.i(tag, "editText: "+ newDesc);
			ts.setTextDescription(newDesc);
		}*/
		if(0 < newDesc.length()) {
			ts.setTextDescription(newDesc);
		}

		System.out.println("newTextDesc: "+ ts.getTextDescription());
		return ts.getTextDescription();
	}

	/**
	 * Copies Trip to clipboard as text.
	 **/
	public static void copyToClipboard() {
		//		app.copyToClipboard(context);
	}

	/**
	 * Sets location of car parked as this location.
	 * @param parent context
	 **/
	public static void setCarParked() {


		List<TrackPoint> locs= UploadServlet.trip.getPoints(); //app.globals.database.getLocations(SqlLiteDatabaseHelper.TableName.GeoPoints);
		if(null == UploadServlet.trip) {
			UploadServlet.trip= new Trip(-1);
		}
		UploadServlet.trip.setPoints(locs);

		//List<TrackPoint> carLoc= locs.subList(locs.size()- 1, locs.size());
		//TrackSegment carSetSeg= new TrackSegment(carLoc, TrackSegment.caminarType.carSet);
		//app.globals.trip.setCarLoacation(locs.size()- 1);


		UploadServlet.carSetPoint= UploadServlet.trip.getPoints().size()- 1;
		UploadServlet.trip= new Trip(UploadServlet.carSetPoint);
	}
}
