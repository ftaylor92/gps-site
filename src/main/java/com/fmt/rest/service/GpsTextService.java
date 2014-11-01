package com.fmt.rest.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fmt.gps.track.TrackSegment;
import com.fmt.gps.track.TrackSegment.caminarType;
import com.fmt.servlet.UploadServlet;

 //see: http://www.ibm.com/developerworks/web/library/wa-aj-tomcat/index.html
@Path("/txt")
public class GpsTextService {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello(@QueryParam("type") String type) {
		StringBuilder txt= new StringBuilder();
		int i= 1;
		for(TrackSegment ts : UploadServlet.trip.getSegments()) {
			if(ts.getType().equals(caminarType.speedChange))	{
				continue;
			}
			txt.append(String.format("%d) ", i)).append(ts.getTextDescription()).append("\n");
			i++;
		}
		/*try {
			List<String> fLines= FileUtilitiesWeb.getFileLines(WebFileAccess.getGpxFile("diary"));
			for(String line : fLines) {
				txt.append(line).append("\n");
			}
		} catch (IOException e) {
			txt.append(e.getMessage());
			e.printStackTrace();
		}*/
		
		return txt.toString();
	}
}   