package com.fmt.rest.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fmt.comon.FileUtilitiesWeb;
import com.fmt.diary.file.AndroidGpxFileDataAccess;
import com.fmt.diary.file.JSHandler;
import com.fmt.diary.file.WebFileAccess;
import com.fmt.servlet.UploadServlet;

 //see: http://www.ibm.com/developerworks/web/library/wa-aj-tomcat/index.html
@Path("/xml")
public class GpsXmlService {
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String dbQuery(@QueryParam("type") String type) {
		StringBuilder xml= new StringBuilder();
		
		if(null != UploadServlet.lastUploadedFile) {
			String sXml= AndroidGpxFileDataAccess.makeGpxXml(UploadServlet.trip, true);
			return sXml;
		} else {
			try {
				//FileUtilitiesWeb.writeFileLines(new File(UploadServlet.uploadPath, "f"), sXml);
			
				//read file and return xml
				List<String> fLines= FileUtilitiesWeb.getFileLines(WebFileAccess.getGpxFile(type));
				for(String line : fLines) {
					xml.append(line).append("\n");
				}
			} catch (IOException e) {
				xml.append(e.getMessage());
				e.printStackTrace();
			}
		}
		return xml.toString();
	}
}   