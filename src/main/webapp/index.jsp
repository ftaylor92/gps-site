<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN">
<html>
<head>
<title>GPS Diary</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" href="http://dl.dropboxusercontent.com/u/688127/public-web-site/gpsdiary/icon32.ico"/>
<link rel="stylesheet" href="http://code.jquery.com/mobile/latest/jquery.mobile.min.css" />
<!-- script src="http://code.jquery.com/jquery-latest.min.js"></script -->
<script src="http://code.jquery.com/mobile/latest/jquery.mobile.min.js"></script>
	</head>
<body>
	<h2 class="title">GPS Diary Analysis</h2>

    <form method="POST" action="upload" name="filesubmit" enctype="multipart/form-data">
        Select GPS(diary), KML or GPX file to upload: 
        <%
        String PARAM= (String)request.getParameter("importfile");
    	if(null == PARAM) { 
    		out.println("<input data-inline=\"true\" type=\"file\" name=\"uploadFile\" />");
    	}  else {
    		out.println("<input data-inline=\"true\" type=\"file\" name=\"uploadFile\" value=\""+PARAM+"\"/>");
    		out.println("<script type=\"text/javascript\">");
    		out.println("function submitform()");
    		out.println("{");
    			out.println("  document.filesubmit.submit();");
    		  out.println("}");
    		//out.println("submitform();");
    		out.println("</script>");
    	}
    	%>
        
        <br/><br/>
        <input type="submit" value="Upload" />
    </form>
	<br/><br/><hr/><br/>
	<a href="http://dl.dropboxusercontent.com/u/688127/public-web-site/gpsdiary/gpsdiary.htm">more</a> about this site/app.<br/>
	Upload GPS files here or from GPS Diary <a href="http://play.google.com/store/apps/details?id=com.fmt.fee.gps&feature=more_from_developer">Android App <img src="http://www.logoeps.com/wp-content/uploads/2011/06/android-robot-vector.jpg" height="42" width="42" /></a>
<script>
	$(document).ready(function() {
		$.ajaxSetup({ cache: false, contentType: "application/json; charset=utf-8", dataType:"json"});

		var counterUrl="http://bookmarks.fmtmac.cloudbees.net/rest/counter?site="+ encodeURIComponent(window.location.href);
		$.get(counterUrl, function(data, txtstatus, xbr) {
			$("#counter").html("<br/><br/><small><em>count: "+ data+"</em></small>");
		});
	});
</script>
<div id='counter'></div>
<div id='cloudbees'>
<hr/>
<a href="http://www.cloudbees.com"><img src="http://cloudbees.prod.acquia-sites.com/sites/default/files/styles/large/public/Button-Built-on-CB-1.png?itok=3Tnkun-C" /></a>
</div>
</body>
</html>
