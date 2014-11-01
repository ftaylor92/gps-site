<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN">
<%@ page import="com.fmt.gps.track.Trip" %>
<%@ page import="com.fmt.gps.track.TrackSegment" %>
<%@ page import="com.fmt.servlet.UploadServlet" %>
<%@ page import="com.fmt.diary.file.AndroidGpxFileDataAccess" %>

<html>
<head>
<title>GPS Diary</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://code.jquery.com/mobile/latest/jquery.mobile.min.css" />
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script src="http://code.jquery.com/mobile/latest/jquery.mobile.min.js"></script>
<!-- my non-jquery scripts always go here -->
<!-- script type="text/javascript" charset="utf-8" src="./cordova-2.7.0.js"></script -->
<script type="text/javascript" charset="utf-8" src="./matt.js"></script>

</head>
<body>
<%
	String USE_KM= Trip.USE_KM ? "1" : "0";
	double MULL_FEET_TOLERANCE= Trip.MULL_FEET_TOLERANCE;
	int    MULL_SECONDS_TOLERANCE= Trip.MULL_SECONDS_TOLERANCE;
	double STOP_PLACE= Trip.STOP_PLACE;
	int    TIME_PAUSE_SECONDS= Trip.TIME_PAUSE_SECONDS;
	int    FPS_SPEED_CHANGE_TOLERANCE= Trip.FPS_SPEED_CHANGE_TOLERANCE;
	double STORE_SPEED_FPS= TrackSegment.STORE_SPEED_FPS;
	double WALK_SPEED_FPS= TrackSegment.WALK_SPEED_FPS;
	double RUN_SPEED_FPS= TrackSegment.RUN_SPEED_FPS;
	double BIKE_SPEED_FPS= TrackSegment.BIKE_SPEED_FPS;
	double VEHICLE_CITY_SPEED_FPS= TrackSegment.VEHICLE_CITY_SPEED_FPS;
	double HIGHWAY_SPEED_FPS= TrackSegment.HIGHWAY_SPEED_FPS;
	double AIR_SPEED_FPS= TrackSegment.AIR_SPEED_FPS;
	int    MIN_SEGMENT_SIZE= TrackSegment.MIN_SEGMENT_SIZE;
	double GPS_SPEED_FPS= TrackSegment.GPS_SPEED_FPS;
	double TOLERANCE_PT_DIFF= TrackSegment.TOLERANCE_PT_DIFF;
	double TOLERANCE_MERGE= TrackSegment.TOLERANCE_MERGE;
	int SECONDS_MARGIN= TrackSegment.SECONDS_MARGIN;
	int STEP= TrackSegment.STEP;
	
	String PARAM= (String)request.getParameter("USE_KM");
	if(null != PARAM) { 
		USE_KM= "USE_KM";
		Trip.USE_KM= true;
	}

	PARAM= (String)request.getParameter("STEP");
	if(null != PARAM) { 
		STEP= Integer.parseInt(PARAM);
		TrackSegment.STEP= STEP;
	}
	PARAM= (String)request.getParameter("FPS_SPEED_CHANGE_TOLERANCE");
	if(null != PARAM) { 
		FPS_SPEED_CHANGE_TOLERANCE= Integer.parseInt(PARAM);
		Trip.FPS_SPEED_CHANGE_TOLERANCE= FPS_SPEED_CHANGE_TOLERANCE;
	}
	PARAM= (String)request.getParameter("TIME_PAUSE_SECONDS");
	if(null != PARAM) { 
		TIME_PAUSE_SECONDS= Integer.parseInt(PARAM);
		Trip.TIME_PAUSE_SECONDS= TIME_PAUSE_SECONDS;
	}
	PARAM= (String)request.getParameter("MIN_SEGMENT_SIZE");
	if(null != PARAM) { 
		MIN_SEGMENT_SIZE= Integer.parseInt(PARAM);
		TrackSegment.MIN_SEGMENT_SIZE= MIN_SEGMENT_SIZE;
	}
	PARAM= (String)request.getParameter("MULL_SECONDS_TOLERANCE");
	if(null != PARAM) { 
		MULL_SECONDS_TOLERANCE= Integer.parseInt(PARAM);
		Trip.MULL_SECONDS_TOLERANCE= MULL_SECONDS_TOLERANCE;
	}
	PARAM= (String)request.getParameter("MULL_FEET_TOLERANCE");
	if(null != PARAM) { 
		MULL_FEET_TOLERANCE= Double.parseDouble(PARAM);
		Trip.MULL_FEET_TOLERANCE= MULL_FEET_TOLERANCE;
	}
	PARAM= (String)request.getParameter("STOP_PLACE");
	if(null != PARAM) { 
		STOP_PLACE= Double.parseDouble(PARAM);
		Trip.STOP_PLACE= STOP_PLACE;
	}
	PARAM= (String)request.getParameter("STORE_SPEED_FPS");
	if(null != PARAM) { 
		STORE_SPEED_FPS= Double.parseDouble(PARAM);
		TrackSegment.STORE_SPEED_FPS= STORE_SPEED_FPS;
	}
	PARAM= (String)request.getParameter("WALK_SPEED_FPS");
	if(null != PARAM) { 
		WALK_SPEED_FPS= Double.parseDouble(PARAM);
		TrackSegment.WALK_SPEED_FPS= WALK_SPEED_FPS;
	}
	PARAM= (String)request.getParameter("RUN_SPEED_FPS");
	if(null != PARAM) { 
		RUN_SPEED_FPS= Double.parseDouble(PARAM);
		TrackSegment.RUN_SPEED_FPS= RUN_SPEED_FPS;
	}
	PARAM= (String)request.getParameter("BIKE_SPEED_FPS");
	if(null != PARAM) { 
		BIKE_SPEED_FPS= Double.parseDouble(PARAM);
		TrackSegment.BIKE_SPEED_FPS= BIKE_SPEED_FPS;
	}
	PARAM= (String)request.getParameter("VEHICLE_CITY_SPEED_FPS");
	if(null != PARAM) { 
		VEHICLE_CITY_SPEED_FPS= Double.parseDouble(PARAM);
		TrackSegment.VEHICLE_CITY_SPEED_FPS= VEHICLE_CITY_SPEED_FPS;
	}
	PARAM= (String)request.getParameter("HIGHWAY_SPEED_FPS");
	if(null != PARAM) { 
		HIGHWAY_SPEED_FPS= Double.parseDouble(PARAM);
		TrackSegment.HIGHWAY_SPEED_FPS= HIGHWAY_SPEED_FPS;
	}
	PARAM= (String)request.getParameter("AIR_SPEED_FPS");
	if(null != PARAM) { 
		AIR_SPEED_FPS= Double.parseDouble(PARAM);
		TrackSegment.AIR_SPEED_FPS= AIR_SPEED_FPS;
	}
	PARAM= (String)request.getParameter("GPS_SPEED_FPS");
	if(null != PARAM) { 
		GPS_SPEED_FPS= Double.parseDouble(PARAM);
		TrackSegment.GPS_SPEED_FPS= GPS_SPEED_FPS;
	}
	PARAM= (String)request.getParameter("TOLERANCE_PT_DIFF");
	if(null != PARAM) { 
		TOLERANCE_PT_DIFF= Double.parseDouble(PARAM);
		TrackSegment.TOLERANCE_PT_DIFF= TOLERANCE_PT_DIFF;
	}
	PARAM= (String)request.getParameter("TOLERANCE_MERGE");
	if(null != PARAM) { 
		TOLERANCE_MERGE= Double.parseDouble(PARAM);
		TrackSegment.TOLERANCE_MERGE= TOLERANCE_MERGE;
	}
	
	if(null != PARAM) { 
		if(null != UploadServlet.lastUploadedFile) {
		    		//ToDo: Display GPS or GPX as Diary
		    		UploadServlet.trip= AndroidGpxFileDataAccess.getDiary(UploadServlet.lastUploadedFile);
		    		UploadServlet.trip= Trip.makeTrip(UploadServlet.carSetPoint, UploadServlet.trip.getSegments().toArray(new TrackSegment[UploadServlet.trip.getSegments().size()]));
		    		//trip= ImportExport.importDiaryFile(lastFile);
		    		//trip= GpxFileDataAccess.getDiary(lastFile);
		    		AndroidGpxFileDataAccess.setTextDescriptions(UploadServlet.trip);
		    		AndroidGpxFileDataAccess.saveDiary();
		    	} else {
		    		//TODO: err
		    	}
	}
%>

	<div data-role="page" id="bar">

		<div data-role="header">
			<h1>Travelog</h1>
		</div>
		<!-- /header -->

		<div data-role="content">

			<ul id="diaryList" data-role="listview" data-theme="g">
			</ul>

		</div>
		<!-- /content -->

		<button data-inline="true" onclick="window.open('./rest/txt');">Save Diary Text</button>
		<button data-inline="true" onclick="window.open('./download.htm');">Download to Android</button>
		<button data-inline="true" onclick="window.location.replace('./diary-tweak.jsp');">Tweak Diary Params</button>


<script type="text/javascript" charset="utf-8">
		//var BASE_URL = "http://localhost:8080/GPSDiarySite";
		var BASE_URL= "http://GPS-site.fmtmac.cloudbees.net";

		function getGpsData(/*user, pass, site*/) {

			var passUrl = BASE_URL + "/rest/json?type=gpx";
			console.log("passUrl: " + passUrl);

			$.get(passUrl, function(data, status) {
				//alert("data: "+ data);

				jData = eval(data); //when $.ajaxSetup is used
				//alert("jData: "+ data);

				populateList(jData);
			});

			return false;
		}

		$(document)
				.ready(
						function() {

							$
									.ajaxSetup({
										cache : false,
										contentType : "application/json; charset=utf-8",
										dataType : "json"
									});

							$('#ajaxError').ajaxError(
									function(e, xhr, settings, exception) {
										$(this).text(
												'Error in: ' + settings.url
														+ ' - Error: '
														+ exception
														+ " - Response: "
														+ xhr.responseText);
									});

							getGpsData();

							var counterUrl = "http://bookmarks.fmtmac.cloudbees.net/rest/counter?site="
									+ encodeURIComponent(window.location.href);
							$.get(counterUrl, function(data, txtstatus, xbr) {
								$("#counter").html(
										"<br/><br/><small><em>count: " + data
												+ "</em></small>");
							});

						}); //ready()

		//$("#dialog").dialog();
</script>
</body>
</html>
