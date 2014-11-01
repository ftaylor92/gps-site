package com.fmt.diary.file;

import java.io.File;
import java.util.List;

import com.fmt.gps.track.TrackPoint;
import com.fmt.gps.track.TrackSegment;
import com.fmt.gps.track.Trip;
import com.fmt.servlet.UploadServlet;

/**
 * Imports and Exports data files.
 * @author root
 **/
public class ImportExport {

	/**
	 * Dialog to select either Import or Export.
	 * @param parent parent
	 * @return whether successful
	 **
	public static boolean importExportDialog(final Trip trip, final Activity parent) {
		ImportExport.parent= parent;

		final Dialog dialog= new Dialog(parent);

		//dialog.setContentView(R.layout.newaccountdialog);
		dialog.setTitle("File Import/Export");
		dialog.setCancelable(true);

		LinearLayout mainLayout= new LinearLayout(parent);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		ListView list= new ListView(dialog.getContext());
		list.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		keys= new ArrayList<String>();
		keys.add("Import GPX File");
		keys.add("Export GPS File");
		keys.add("Import Travelog");
		keys.add("Export Travelog");

		list.setAdapter(new ArrayAdapter<String>(parent, android.R.layout.simple_list_item_1, keys));
		mainLayout.addView(list);

		dialog.setContentView(mainLayout);
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView arg0, android.view.View arg1, int arg2, long arg3) {
				dialog.dismiss();
				
				switch(arg2) {
					case 0:
						importFile(true, parent);
						break;
					case 1:
						exportFile(trip, true, parent);
						break;
					case 2:
						importFile(false, parent);
						break;
					case 3:
						exportFile(trip, false, parent);
						break;
					default:
						break;
				}
			}
		});
		
		dialog.show();

		return true;
	}
	
	/**
	 * Dialog to select which file to export to.
	 **
	public static boolean exportFile(final Trip trip, final boolean gpxOnly, final Activity parent) {
		GpsDiary app= ((GpsDiary)parent.getApplicationContext());
		if(!app.isDiaryReady(parent)) {
			if(app.globals.state.contains(GlobalData.appState.recording)) {
				new LoadGpsPointsFromDatabaseTask().execute();
			} else {
				Toast.makeText(parent, R.string.waitingtripcalc, Toast.LENGTH_SHORT).show();
			}
			return false;
		}
		
		ImportExport.parent= parent;
		int id= GpsDiary.id.lastItem.ordinal()+ 1;
		
		final Dialog dialog= new Dialog(parent);

		//dialog.setContentView(R.layout.newaccountdialog);
		dialog.setTitle(parent.getString(R.string.export)+ (gpxOnly ? parent.getString(R.string.GPXfile) : parent.getString(R.string.Travelog)));
		dialog.setCancelable(true);

		LinearLayout mainLayout= new LinearLayout(parent);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		ListView list= new ListView(dialog.getContext());
		list.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		addOnePair(false, parent.getString(R.string.FileName), propNames.fileName.name(), (gpxOnly ? Travelog.DFILE : Travelog.DTFILE).format(new Date()), id++, false, dialog, mainLayout);

		dialog.setContentView(mainLayout);
		
		OnClickListener listener= new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				switch(GpsDiary.id.values()[v.getId()]) {
				case saveButton: {
					//reconcileProps();
					final String fileName= getEp(propNames.fileName.name(), props).editableView.getText().toString();
					new SaveDiaryTask().execute(fileName, Boolean.toString(gpxOnly));
					Toast.makeText(parent, parent.getString(R.string.filesaveto)+GpsDiary.GPX_PATH.getAbsolutePath()+"/...", Toast.LENGTH_SHORT).show();
					//DailyBalanceAndroid.parent.startLongRunningOperation();
					//DailyBalanceAndroid.savePropsFromDialogProperties(props, DailyBalanceAndroid.db.currentProperties);
				} break;
				case cancelButton: {
					//Toast.makeText(DailyBalanceAndroid.parent, "cancel", Toast.LENGTH_SHORT).show();
				} break;
				default:
					//Toast.makeText(DailyBalanceAndroid.parent, "error: "+ v.getId(), Toast.LENGTH_SHORT).show();
					break;
				}

				dialog.dismiss();
			}
		};
		
		addButtons(false, dialog, mainLayout, listener);
		
		dialog.show();

		return true;
	}*/
	
	/**
	 * Dialog to select which file to import.
	 **
	public static boolean importFile(final boolean gpxOnly, final Activity parent) {
		/*GpsDiary app= (GpsDiary)parent.getApplicationContext();
		if(!app.isDiaryReady(parent)) {
			return false;
		}*
		
		ImportExport.parent= parent;
		int id= GpsDiary.id.lastItem.ordinal()+ 1;
		
		final Dialog dialog= new Dialog(parent);

		//dialog.setContentView(R.layout.newaccountdialog);
		dialog.setTitle(parent.getString(R.string.imports)+ (gpxOnly ? parent.getString(R.string.GPXfile) : parent.getString(R.string.Travelog)));
		dialog.setCancelable(true);

		LinearLayout mainLayout= new LinearLayout(parent);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		ListView list= new ListView(dialog.getContext());
		list.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		//addOnePair(false, "File Name:", propNames.fileName.name(), Travelog.DFILE.format(new Date()), id++, false, dialog, mainLayout);

		FilenameFilter filter= new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return(filename.toLowerCase().endsWith(gpxOnly ? "gpx" : "tvl"));
			}
		};
		File filesDir= GpsDiary.GPX_PATH;
		String[] fileDirs= {Environment.getExternalStorageDirectory().getAbsolutePath(), "/MyTracks/gpx","/mnt/sdcard/MyTracks/gpx", "/", "/mnt/sdcard", GpsDiary.GPX_PATH.getAbsolutePath()};
		String[] files= filesDir.list(filter);
		try {
			for(String dir : fileDirs) {
				if(null == files || 0 == files.length){
					filesDir= new File(dir);
					files= filesDir.list(filter);
				}
			}
		} catch (Exception e) {
			Log.e("Bad Directory", e.getMessage());
		}
		if(null == files || 0 == files.length){
			TextView noFilesMsg= new TextView(dialog.getContext());
			String noFiles= parent.getString(R.string.NoFilesin);
			for(String dir : fileDirs) {
				noFiles+= dir+ ", ";
			}
			noFilesMsg.setText(noFiles);
			mainLayout.addView(noFilesMsg);
		} else {
			GpsDiary.GPX_PATH= filesDir;
			final String[] filez= files;
			Spinner evtTypeView= new Spinner(dialog.getContext());
			evtTypeView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			evtTypeView.setId(id++);
			evtTypeView.setPrompt(parent.getString(R.string.EventType));
			ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
		            dialog.getContext(), android.R.layout.simple_spinner_item, files);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    evtTypeView.setAdapter(adapter);
		    evtTypeView.setOnItemSelectedListener(new OnItemSelectedListener() {
		    	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		    		selectedFile= filez[pos];
		    	}
	
	    	    public void onNothingSelected(AdapterView parent) {
	    	      // Do nothing.
	    	    }
			});
		    mainLayout.addView(evtTypeView);
		}

		dialog.setContentView(mainLayout);
		
		OnClickListener listener= new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				switch(GpsDiary.id.values()[v.getId()]) {
				case saveButton: 
					//reconcileProps();
					new LoadDiaryFileTask().execute(selectedFile);
					//((GpsDiary)parent.getApplicationContext()).globals.fileLoading= true;
					Toast.makeText(parent, /*Environment.getExternalStorageDirectory().getAbsolutePath()* "File Loading "+GpsDiary.GPX_PATH+File.pathSeparator+selectedFile+"...", Toast.LENGTH_SHORT).show();
//				    if(false) {
//					final Thread loadDiary = new Thread() {
//				        public void run() {
//							if(gpxOnly) {
//								importGpxFile(selectedFile);
//							} else {
//								importDiaryFile(selectedFile);
//							}
//				        }
//				    };
//				    //dialog.dismiss();
//				    loadDiary.run(); }
					/*Intent myIntent = new Intent("com.fmt.gps.file.LOAD_DIARY");
					parent.startActivity(myIntent); *

					//DailyBalanceAndroid.parent.startLongRunningOperation();
					//DailyBalanceAndroid.savePropsFromDialogProperties(props, DailyBalanceAndroid.db.currentProperties);
				  break;
				case cancelButton: {
					//dialog.dismiss();
					//Toast.makeText(DailyBalanceAndroid.parent, "cancel", Toast.LENGTH_SHORT).show();
				} break;
				default:
					//Toast.makeText(DailyBalanceAndroid.parent, "error: "+ v.getId(), Toast.LENGTH_SHORT).show();
					break;
				}

				
				dialog.dismiss();
				//loadDiary.run();
			}
		};
		
		addButtons(false, dialog, mainLayout, listener);
		
		dialog.show();

		return true;
	}*/
	
	/**
	 * Imports data from files in default directory.
	 **/
	public static void importGpxFile(File fileName) {
		importDiaryFile(fileName);

		/* we prefer to use the TrackSegments-aware diary parser:
		List<TrackPoint> pts= GpxFileAccess.getPoints(new File(GpsDiary.GPX_PATH+fileName));
		GpsDiary.database.removeAllPoints();
		for(TrackPoint pt: pts) {
			GpsDiary.database.insertLocation(SqlLiteDatabaseHelper.TableName.GeoPoints, pt);
		}
		GpsDiary.trip= Travelog.makeTrip(pts);
		*/
		//parent.startLongRunningOperation();
	}
	
	/**
	 * Imports data from files in default directory.
	 **/
	public static Trip importDiaryFile(File fileName) {
		Trip trip= AndroidGpxFileDataAccess.getDiary(fileName);
//		trip.state= new ArrayList<Trip.tripState>();
//		trip.state.add(Trip.tripState.gpxOnly);
//		trip.state.add(Trip.tripState.usingTrip);
		trip= Trip.makeTrip(UploadServlet.carSetPoint, trip.getSegments().toArray(new TrackSegment[trip.getSegments().size()]));
		/*GpsDiary.database.removeAllPoints();
		for(TrackPoint pt: pts) {
			GpsDiary.database.insertLocation(SqlLiteDatabaseHelper.TableName.GeoPoints, pt);
		}
		GpsDiary.trip= Travelog.makeTrip(pts);*/
		
		//parent.startLongRunningOperation();
		//GlobalData.trip= trip;
		
		//remove file: new File(GpsDiary.GPX_PATH, fileName).delete();	
		
		return trip;
	}

	/**
	 * Exports data to files in default directory.
	 **/
	public static void saveToGpx(Trip trip, File fileName, final boolean gpxOnly) {


				List<TrackPoint> pts= trip.getPoints();
				if(0 == pts.size()) {
					//Toast.makeText(parent, "Nothing to Save: First Record GPS or Import File", Toast.LENGTH_LONG).show();
				} else {
//					trip= Trip.makeTrip(new TrackSegment(pts, TrackSegment.caminarType.undef));
				}
			
			//save trip to file
			AndroidGpxFileDataAccess.savePoints(fileName, trip, !gpxOnly);

				
		//Toast.makeText(parent, "file saved to "+ GpsDiary.GPX_PATH, Toast.LENGTH_LONG).show();

	}

	/**
	 * Gets Editable property from map.
	 * @param prop name of prop
	 * @param props all properties
	 * @return Editable property object
	 **
	public static EditableProperty getEp(String prop, List<EditableProperty> props) {
		for(EditableProperty p : props) {
			if(p.propertyName.equals(prop))	return p;
		}
		
		return null;
	}*/
	
	/**
	 * adds an editable pair or Label and editable field to dialog.
	 * @param forNum if this is a number-only input
	 * @param label label of field
	 * @param propName property name to extract EditableProperty object later
	 * @param value initial value of editable text field
	 * @param id android item id number
	 * @param forDate if this is a date input
	 * @param dialog parent dialog
	 * @param layout layout to place items apon
	 **
	public static void addOnePair(boolean forNum, String label, String propName, String value, final int id, boolean forDate, Dialog dialog, ViewGroup layout) {
		if(null == label)	label= propName;

		LinearLayout line= new LinearLayout(dialog.getContext());
		line.setOrientation(LinearLayout.HORIZONTAL);
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 60));
		line.setGravity(Gravity.RIGHT);
		
		EditableProperty prop= new EditableProperty();
		prop.propertyName= propName;
		//prop.viewId= ;

		TextView labelView= new TextView(dialog.getContext());
		labelView.setText(label+":");
		labelView.setTextSize(12);
		line.addView(labelView);

		if(forDate) {
			final Button editValue= new Button(dialog.getContext());
			editValue.setId(id);
			editValue.setText(value);
			editValue.setHeight(*editValue.getHeight()/2*10);
			editValue.setEms(8);
			editValue.setTextSize(12);
			ImportExport.listeners.put(id, new EditableDateSetListener(editValue));
			editValue.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ImportExport.parent.showDialog(id);
				}
			});
			line.addView(editValue);
			prop.editableView= editValue;
		} else {
			EditText editValue= new EditText(dialog.getContext());
			editValue.setId(id);
			editValue.setText(value);
			editValue.setHeight(*editValue.getHeight()/2*10);
			/*editValue.setMaxHeight(5);
			editValue.setLayoutParams(new LayoutParams(30, 30));*
			editValue.setEms(6);
			editValue.setTextSize(12);
			if(forNum) {
				editValue.setInputType(InputType.TYPE_CLASS_NUMBER+InputType.TYPE_NUMBER_FLAG_DECIMAL+InputType.TYPE_NUMBER_FLAG_SIGNED);
			}
			line.addView(editValue);
			prop.editableView= editValue;
		}

		prop.oldValue= value;
		props.add(prop);

		layout.addView(line);
	}*/
	
	/**
	 * Adds buttons to bottom of dialog.
	 * @param forDelete Save/Delete instead of Ok/Cancel
	 * @param dialog parent dialog
	 * @param layout layout
	 * @param listener button click listener
	 *
	public static void addButtons(boolean forDelete, Dialog dialog, ViewGroup layout, OnClickListener listener) {
		LinearLayout line= new LinearLayout(dialog.getContext());
		line.setOrientation(LinearLayout.HORIZONTAL);
		line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		line.setGravity(Gravity.CENTER);
		
		Button saveButton= new Button(dialog.getContext()); //(Button)dialog.findViewById(R.id.Button01); //
		saveButton.setId(GpsDiary.id.saveButton.ordinal());
		saveButton.setText(R.string.Ok*DailyBalanceAndroid.id.saveCurrentButton.name()*);
		saveButton.setEms(6);
		line.addView(saveButton);

		Button cancelButton= new Button(dialog.getContext()); //(Button)dialog.findViewById(R.id.Button01); //
		cancelButton.setId(GpsDiary.id.cancelButton.ordinal());
		cancelButton.setText(forDelete ? R.string.Delete : R.string.cance*DailyBalanceAndroid.id.cancelButton.name()*);
		line.addView(cancelButton);
		layout.addView(line);
		
		saveButton.setOnClickListener(listener);
		cancelButton.setOnClickListener(listener);
	}*/
}
