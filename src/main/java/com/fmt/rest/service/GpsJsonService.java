package com.fmt.rest.service;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.fmt.diary.file.AndroidGpxFileDataAccess;
import com.fmt.diary.file.JSHandler;
import com.fmt.diary.file.WebFileAccess;
import com.fmt.gps.track.TrackSegment;
import com.fmt.gps.track.Trip;
import com.fmt.servlet.UploadServlet;

 //see: http://www.ibm.com/developerworks/web/library/wa-aj-tomcat/index.html
@Path("/json")
public class GpsJsonService {
	
	//private static final String TOMCAT_LOCAL_DIR = "/var/lib/tomcat7/webapps/GPSDiarySite";

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String dbQuery(@QueryParam("type") String type, 
			@Context HttpServletResponse servletResponse,
			@Context HttpServletRequest servletRequest) {
		if(null == UploadServlet.lastUploadedFile) {
			UploadServlet.uploadPath = servletRequest.getServletContext().getRealPath("")+ File.separator + UploadServlet.UPLOAD_DIRECTORY;
			UploadServlet.lastUploadedFile= WebFileAccess.getGpxFile((null == type) ? "gpx" : type);
			UploadServlet.trip= AndroidGpxFileDataAccess.getDiary(UploadServlet.lastUploadedFile);
			UploadServlet.trip= Trip.makeTrip(UploadServlet.carSetPoint, UploadServlet.trip.getSegments().toArray(new TrackSegment[UploadServlet.trip.getSegments().size()]));
		}
		return JSHandler.getDiaryJson();
	}
}   