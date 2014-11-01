package com.fmt.gps.geocode;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.LatLng;

public class GeoCoder {
	
	public static void main(String[] args) {
		GeoCoder gc= new GeoCoder();
		//GeoCodeResult result= gc.reverseGeoCode(34.862024F, -111.76401F);
		GeoCodeResult result= gc.reverseGeoCode(-33.8669920F, 151.1953320F);
		
		System.out.println("result: "+ result.toString());
	}

	//private static final String YAHOO_API_BASE_URL = "http://where.yahooapis.com/geocode?q=%1$s,+%2$s&gflags=R&appid=[yourappidhere]";
	//private static final String GEONAMES_BASE_URL = "http://api.geonames.org/findNearestAddress?lat=%s&lng=%s&username=demo";
	private final static String GOOG_KEY= "AIzaSyB98NbRQPMKRYuRdXPazwNOISMUS-DpYz8";
	private static final String GOOGLE_BASE_URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&sensor=%s";
	private static final String GOOGLE_PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/xml?location=%f,%f&radius=%d&sensor=%s&key=%s";
	//google: http://maps.googleapis.com/maps/api/geocode/json?address=Winnetka&bounds=34.172684,-118.604794|34.236144,-118.500938&sensor=false

	private HttpRetriever httpRetriever = new HttpRetriever();
	private XmlParser xmlParser = new XmlParser();

	public GeoCodeResult reverseGeoCode(double latitude, double longitude) {
		GeocodeResponse geocoderResponse= new GeocodeResponse();
		GeoCodeResult result= new GeoCodeResult();

		try {
			//String 
			String url = String.format(GOOGLE_BASE_URL, String.valueOf(latitude), String.valueOf(longitude), "false");
			url= String.format(GOOGLE_PLACES_BASE_URL, latitude, longitude, 20, "false" , GOOG_KEY);
			System.out.printf("request: %s\n", url);
			String response = httpRetriever.retrieve(url);
			System.out.printf("response: %s...\n", response.substring(0, Math.min(480, response.length()-1)));	//.substring(0, 150)
			//final String oldFormattedAddress= result.formattedAddress;
			result =xmlParser.parseXmlResponse(response);

			if(!(null == result.street || result.street.length() == 0)) {
				System.out.printf("(5)result.street: %s\n", result.street);
				System.out.printf("(5)result.placename: %s\n", result.placename);
				//result.formattedAddress= "Google URL failed";

				/*ArrayList<Map<String,Object>> results= (ArrayList<Map<String,Object>>)geoData.get("results");
					if(!results.isEmpty()) {

						Map<String,Object> addresses= results.get(0);
						result.formattedAddress= (String)addresses.get("formattedAddress");
					}*/

				//result.formattedAddress= (geocoderResponse.getResults().size() > 0) ? geocoderResponse.getResults().get(0).getFormattedAddress() : "-";
				if(null == result.placename) {
					result.formattedAddress= result.street;
				} else {
					result.formattedAddress= result.placename+" at "+ result.street;
				}
			}

			System.out.printf("(6)result.formattedAddress: %s\n", result.formattedAddress);
			if(null == result.formattedAddress || result.formattedAddress.isEmpty()) {
				try {
					final Geocoder geocoder = new Geocoder();
					GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(new LatLng(new BigDecimal(latitude), new BigDecimal(longitude))).setLanguage("en").getGeocoderRequest();
					geocoderResponse = geocoder.geocode(geocoderRequest);
					result.formattedAddress= (geocoderResponse.getResults().size() > 0) ? geocoderResponse.getResults().get(0).getFormattedAddress() : "-";

				} catch (Exception e1) {
					result.formattedAddress= "Google Geocode API failed";
					e1.printStackTrace();
				}
				System.out.printf("(1)result.formattedAddress: %s\n", result.formattedAddress);
			}
			/*ObjectMapper mapper = new ObjectMapper();
				//Map<String,Object> geoData = mapper.readValue(response, Map.class);
				geocoderResponse = mapper.readValue(response, GeocodeResponse.class);
			 */	


			if(null == result.formattedAddress){ 
				result.formattedAddress= "--";
			}

			System.out.printf("(3)result.formattedAddress: %s\n", result.formattedAddress);



		} catch (Exception e) {
			System.err.println("Google Geocode URL failed");
			//result.formattedAddress= "Google Geocode URL failed";
			e.printStackTrace();
		}

		if(null == result.formattedAddress) {
			result.formattedAddress= "---";
		}

		System.out.printf("(4)address(%f,%f): %s\n", latitude, longitude, result.formattedAddress);
		//}

		return result;
	}

}
