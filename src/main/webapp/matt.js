//my custom scripts go here between the two jquery scripts

//gloabl variables

/** all list items. **/
var lis = [];
/** currently edited descrption. **/
var currentDesc= "";

//var BASE_URL= "http://localhost:8080/GPSDiarySite";
var BASE_URL= "http://GPS-site.fmtmac.cloudbees.net";

/**
 * populates Diary List items.
 **/
function populateList(trip) {
	console.log(trip);
	//var trip= eval('(' + myJsonStr + ')');
	var i= 0;
	$("#diaryList").empty();
	for(seg in trip.segments) {

		var imgType= "car";
		if(trip.segments[seg].type == "walk") imgType= "walk";
		if(trip.segments[seg].type == "run") imgType= "walk";
		if(trip.segments[seg].type == "car") imgType= "car";
		if(trip.segments[seg].type == "bike") imgType= "bike";
		if(trip.segments[seg].type == "speedChange")	{
			i++;
			continue;
		}
		//$("#diaryList").append("<li><img src='images/"+imgType+".png' onclick='window.JavaScriptToAndroidBridge.viewMap("+i+");'/><input type='text' value='"+trip.segments[seg].textDescription+"' /><img src='images/edit.png' onclick='window.JavaScriptToAndroidBridge.editText("+i+");'/></li>");
		$("#diaryList").append(getLi("", i, imgType, trip.segments[seg].textDescription));
		//$("#diaryList").append("<li id='li"+i+"'><img height='25' width='25' src='images/"+imgType+".png' onclick='viewMap("+i+");'/>"+trip.segments[seg].textDescription+"<img height='13' width='13' src='images/edit.png' onclick='editTextDescription("+i+");'/></li>");
		i++;
	}
}

function editTextDescriptionEnd(segNum, imageType) {
	//alert("segNum: "+ segNum);
	//var imageType= "car";
	//works: var myHTMLContent = $("#diaryList").html();
	//alert(myHTMLContent);
	//works: myHTMLContent = $("#diaryList li:first").html();
	//alert(myHTMLContent);
	//null: myHTMLContent = $("#diaryList li0").html();
	//alert(myHTMLContent);
	//fail: myHTMLContent = $("#diaryList li:li0").html();
	//alert(myHTMLContent);	
	myHTMLContent = $("#li"+segNum).html();
	console.log("li-myHTMLContent: "+ myHTMLContent);
	//alert(myHTMLContent);
	myHTMLContent = $("#edit"+segNum).html();
	console.log("edit-myHTMLContent: "+ myHTMLContent);
	//alert(myHTMLContent);
	//<a href="foo.html" data-rel="dialog">Open dialog</a>
//	var newDesc= window.JavaScriptToAndroidBridge.editText(segNum, currentDesc);
	//alert("newDesc: "+ newDesc);
	
	//fails: $("#diaryList").append("<li id='li"+i+"'><img height='25' width='25' src='images/"+imgType+".png' onclick='viewMap("+i+");'/>"+"allo"+"<img height='13' width='13' src='images/edit.png' onclick='editTextDescription("+i+");'/></li>");

	
	//nothing: $('#li'+segNum).text(newDesc.toString());
	//$('#li'+segNum).text(newDesc.toString());
	//works: $('#li'+segNum).text("allo");
	//works: $('#li'+segNum).html(myHTMLContent+"<img height='13' width='13' src='images/edit.png' />");
	//works: $('#li'+segNum).html(getLi(myHTMLContent, i, "car", "allo"));
	//works: $('#li'+segNum).html(getLi(myHTMLContent, i, imageType, newDesc));
	//$('#li'+segNum).html(getLi(myHTMLContent, segNum, imageType, newDesc));
	//$('#li'+segNum).html(getLi(myHTMLContent, segNum, imageType, newDesc));
	//alert("curr: "+ currentDesc);
//	$('#li'+segNum).html(getLi(myHTMLContent, segNum, imageType, currentDesc));
//	currentDesc= "";
//	$('#diaryList').listview('refresh');
	console.log("end-myHTMLContent: "+ myHTMLContent);
	console.log("end-currentDesc: "+ currentDesc);
	var passUrl= BASE_URL+ "/rest/edit?desc="+currentDesc+"&segnum="+segNum;
	console.log("passUrl: "+passUrl);
	
    $.get(passUrl,function(data,status){
    	//alert("data: "+ data);

    	jData= eval(data); //when $.ajaxSetup is used
    	console.log("jData: "+ jData.textDescription);

    	newDesc= jData.textDescription;
    	
    	$('#li'+segNum).html(getLi(myHTMLContent, segNum, imageType, currentDesc));
    	currentDesc= "";
    	$('#diaryList').listview('refresh');
      });
}

/** 
 * Edit text of one list item.
 * @param segNum which list item to change
 * @param imageType type of image in list
 **/
function editTextDescription(segNum, imageType) {
	//alert("segNum: "+ segNum);
	//var imageType= "car";
	//works: var myHTMLContent = $("#diaryList").html();
	//alert(myHTMLContent);
	//works: myHTMLContent = $("#diaryList li:first").html();
	//alert(myHTMLContent);
	//null: myHTMLContent = $("#diaryList li0").html();
	//alert(myHTMLContent);
	//fail: myHTMLContent = $("#diaryList li:li0").html();
	//alert(myHTMLContent);	
	myHTMLContent = $("#li"+segNum).html();
	console.log("myHTMLContent: "+ myHTMLContent);
	//myHTMLContent= "";
	//alert(myHTMLContent);
	//<a href="foo.html" data-rel="dialog">Open dialog</a>
//	var newDesc= window.JavaScriptToAndroidBridge.editText(segNum, myHTMLContent);
	var passUrl= BASE_URL+ "/rest/edit?desc=&segnum="+segNum;	//"+myHTMLContent+"
	console.log("passUrl: "+passUrl);
	
    $.get(passUrl,function(data,status){
    	//alert("data: "+ data);

    	jData= eval(data); //when $.ajaxSetup is used
    	console.log("jData: "+ jData.textDescription);

    	newDesc= jData.textDescription;
    	currentDesc= newDesc;
    	
    	$('#li'+segNum).html(getLie(myHTMLContent, segNum, imageType, newDesc));
    	$('#diaryList').listview('refresh');
      });
    
//	currentDesc= newDesc;
	//alert("newDesc: "+ newDesc);
	
	//fails: $("#diaryList").append("<li id='li"+i+"'><img height='25' width='25' src='images/"+imgType+".png' onclick='viewMap("+i+");'/>"+"allo"+"<img height='13' width='13' src='images/edit.png' onclick='editTextDescription("+i+");'/></li>");

	
	//nothing: $('#li'+segNum).text(newDesc.toString());
	//$('#li'+segNum).text(newDesc.toString());
	//works: $('#li'+segNum).text("allo");
	//works: $('#li'+segNum).html(myHTMLContent+"<img height='13' width='13' src='images/edit.png' />");
	//works: $('#li'+segNum).html(getLi(myHTMLContent, i, "car", "allo"));
	//works: $('#li'+segNum).html(getLi(myHTMLContent, i, imageType, newDesc));
	//$('#li'+segNum).html(getLi(myHTMLContent, segNum, imageType, newDesc));
//	$('#li'+segNum).html(getLie(myHTMLContent, segNum, imageType, newDesc));
//	$('#diaryList').listview('refresh');
}

/** 
 * Edit text of one list item.
 * @param segNum which list item to change
 * @param imageType type of image in list
 **
function editTextDescription(segNum) {
	alert("segNum: "+ segNum);
	var imageType= "car";
	//works: var myHTMLContent = $("#diaryList").html();
	//alert(myHTMLContent);
	//works: myHTMLContent = $("#diaryList li:first").html();
	//alert(myHTMLContent);
	//null: myHTMLContent = $("#diaryList li0").html();
	//alert(myHTMLContent);
	//fail: myHTMLContent = $("#diaryList li:li0").html();
	//alert(myHTMLContent);	
	myHTMLContent = $("#li"+segNum).html();
	//alert(myHTMLContent);
	//<a href="foo.html" data-rel="dialog">Open dialog</a>
	var newDesc= window.JavaScriptToAndroidBridge.editText(segNum);
	//alert("newDesc: "+ newDesc);
	
	//fails: $("#diaryList").append("<li id='li"+i+"'><img height='25' width='25' src='images/"+imgType+".png' onclick='viewMap("+i+");'/>"+"allo"+"<img height='13' width='13' src='images/edit.png' onclick='editTextDescription("+i+");'/></li>");

	
	//nothing: $('#li'+segNum).text(newDesc.toString());
	//$('#li'+segNum).text(newDesc.toString());
	//works: $('#li'+segNum).text("allo");
	//works: $('#li'+segNum).html(myHTMLContent+"<img height='13' width='13' src='images/edit.png' />");
	//works: $('#li'+segNum).html(getLi(myHTMLContent, i, "car", "allo"));
	//works: $('#li'+segNum).html(getLi(myHTMLContent, i, imageType, newDesc));
	$('#li'+segNum).html(getLi(myHTMLContent, segNum, imageType, newDesc));
	$('#diaryList').listview('refresh');
}*/

/**
 * Returns <li> HTML for one list item.
 * @param oldHTML
 * @param i
 * @param imgType
 * @param newDesc
 * @returns {String} HTML of one li
 */
function getLi(oldHTML, i, imageType, newDesc) {
	//orig: return "<li id='li"+i+"'><img height='25' width='25' src='images/"+imageType+".png' onclick='viewMap("+i+");'/>"+newDesc+"<img height='13' width='13' src='images/edit.png' onclick='editTextDescription("+i+","+imageType+");'/></li>";
	return "<li id='li"+i+"'><img height='25' width='25' src='images/"+imageType+".png' onclick='viewMap("+i+");'/>"+newDesc+"<img height='13' width='13' src='images/edit.png' onclick='editTextDescription("+i+",\""+imageType+"\");'/></li>";
	//dialog: return "<li id='li"+i+"'><img height='25' width='25' src='images/"+imageType+".png' onclick='viewMap("+i+");'/>"+newDesc+"<a href='dialog.htm' data-rel='dialog' data-transition='pop'><img height='13' width='13 src='images/edit.png'/></a></li>";
}

/**
 * Returns <li> HTML for one list item.
 * @param oldHTML
 * @param i
 * @param imgType
 * @param newDesc
 * @returns {String} HTML of one li
 */
function getLie(oldHTML, i, imageType, newDesc) {
	//orig: return "<li id='li"+i+"'><img height='25' width='25' src='images/"+imageType+".png' onclick='viewMap("+i+");'/>"+newDesc+"<img height='13' width='13' src='images/edit.png' onclick='editTextDescription("+i+","+imageType+");'/></li>";
	//return "<li id='li"+i+"'><img height='25' width='25' src='images/"+imageType+".png' onclick='viewMap("+i+");'/><input type='text' value='"+newDesc+"' /><img height='13' width='13' src='images/edit.png' onclick='editTextDescriptionEnd("+i+",\""+imageType+"\");'/></li>";
	//return "<li id='li"+i+"'><img height='25' width='25' src='images/"+imageType+".png' onclick='viewMap("+i+");'/><input rows='4' type='text' value='"+newDesc+"' /><img height='13' width='13' src='images/edit.png' onclick='editTextDescriptionEnd("+i+",\""+imageType+"\");'/></li>";
	return "<li id='li"+i+"'><img height='25' width='25' src='images/"+imageType+".png' onclick='viewMap("+i+");'/><input onkeyup='setDescVal(this)' id='edit"+i+"' rows='4' type='text' value='"+newDesc+"' /><img height='13' width='13' src='images/edit.png' onclick='editTextDescriptionEnd("+i+",\""+imageType+"\");'/></li>";
	//dialog: return "<li id='li"+i+"'><img height='25' width='25' src='images/"+imageType+".png' onclick='viewMap("+i+");'/>"+newDesc+"<a href='dialog.htm' data-rel='dialog' data-transition='pop'><img height='13' width='13 src='images/edit.png'/></a></li>";
}

function setDescVal(input) {
	//alert("key"+ input.value);
	currentDesc= input.value;
}

//<input type='text' value='"+trip.segments[seg].textDescription+"' />

/** views segment on map.
 * @param segNum which segment to focus on
 **/
function viewMap(segNum) {
	window.JavaScriptToAndroidBridge.viewMap(segNum);
}

/**
 * First function I created.
 **
function getInfo() {
	//alert(window.JavaScriptToAndroidBridge.getJson("nada"));
	
	
	var myJson= '{ "name" : "John","state"  : "Doe","city" : 23 }';
	var myJsonS= window.JavaScriptToAndroidBridge.getJsonTxt("nada");
	alert(myJson);
	alert(myJsonS);
	var myJsonn= $.parseJSON(myJson);
	var myJsonnS= $.parseJSON(myJsonS);
	var myObj= eval('(' + myJsonS + ')');
	alert(myObj);
	//myJsonn = jQuery.parseJSON('{"name":"John"}');
	
	alert(myJsonn.name);
	alert(myJsonn.state);
	alert(myObj.name);
	alert(myObj.state);
	alert(myJsonnS.name);
	alert(myJsonnS.state);
	
	return "gaga";
}*/

/** Gets URL parameter.
 * @param name name of URL parameter
 * @return value of parameter
 **/
function gup(name) {
  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( window.location.href );
  if( results == null )
    return "";
  else
    return results[1];
}
