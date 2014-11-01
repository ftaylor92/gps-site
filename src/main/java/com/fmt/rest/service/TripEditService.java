package com.fmt.rest.service;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.fmt.diary.file.AndroidGpxFileDataAccess;
import com.fmt.diary.file.JSHandler;

 //see: http://www.ibm.com/developerworks/web/library/wa-aj-tomcat/index.html
@Path("/edit")
public class TripEditService {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String dbQuery(@QueryParam("desc") String newDesc, @QueryParam("segnum") String segnum) {
		/*if(0 < newDesc.length()) {
			newDesc= newDesc.substring(newDesc.indexOf("<at ")+ 1);
			newDesc= newDesc.substring(0, newDesc.indexOf("<img"));
		}*/
		
		String json= null;

		ObjectMapper mapper= new ObjectMapper();
		try {
			json= mapper.writeValueAsString(JSHandler.editText(Integer.parseInt(segnum), newDesc));
			json= "{ \"textDescription\": "+json+" }";
			//System.out.println(json);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AndroidGpxFileDataAccess.saveDiary();
		
		return json;
	}
}   