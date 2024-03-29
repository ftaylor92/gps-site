package com.fmt.gps.track;

import com.fmt.gps.Travelog;
import com.fmt.gps.track.TrackSegment.caminarType;

import java.util.*;

import org.codehaus.jackson.annotate.*;

/**
 * Represents and entire Trip of lists of GPS points and segments.
 * <p/>
 * @author root
 * <p/>
 */
@JsonAutoDetect
public class Trip
{
    /**
     * whether to log output. *
     */
    public static final boolean LOG = false;
    /**
     * whether to use KM or Miles. *
     */
    public static boolean USE_KM;
    /**
     * how many feet around anchor point until you're moving instead of mulling
     * about. *
     */
    public static double MULL_FEET_TOLERANCE;
    /**
     * minimum amount of seconds you're around anchor point before you're
     * mulling instead of moving slowly. *
     */
    public static int MULL_SECONDS_TOLERANCE;
    /**
     * how many max feet-per-second change(=accelleration) until speed change
     * should be detected. *
     */
    public static int FPS_SPEED_CHANGE_TOLERANCE;
    /**
     * how much time between points until a Pause occurred, which means you
     * weren't moving or were inside. *
     */
    public static int TIME_PAUSE_SECONDS;
    /**
     * How wide a swathe of space given before it looks like you haven't moved
     * from your position. *
     */
    public static double STOP_PLACE;
    /**
     * state of App. *
     */
    //public static enum tripState{gpxOnly, carSetOnly, segsSplit, pointsPopulated, usingDB, usingTrip, tripCompleted, naked};
    /**
     * state of populated Trip object. *
     */
    //@JsonIgnore
    //public Collection<tripState> state= new Vector<tripState>();
    /**
     * All points in trip. *
     */
    @JsonIgnore
    private List<TrackPoint> points = null;
    /**
     * Segments in Trip. *
     */
    private List<TrackSegment> segments = new LinkedList<TrackSegment>();
    /**
     * WayPoints in Trip. *
     */
    private List<WayPoint> waypoints = new LinkedList<WayPoint>();
    
    /** Name of Trip. **/
    public String name= "";
    @JsonIgnore
    public TrackSegment carSetSegment= null;
    private int carSetPoint= -1;
    
    /**
     * Constructor. Sets trip name to today's date. *
     */
    public Trip(int carSetPoint)
    {
    	this.carSetPoint= carSetPoint;
    	name= Travelog.DAY.format(new Date());

        this.USE_KM = false;
        this.MULL_FEET_TOLERANCE = 100.0;
        this.MULL_SECONDS_TOLERANCE = 240;
        this.STOP_PLACE = 50.0;
        this.TIME_PAUSE_SECONDS = 240;
        this.FPS_SPEED_CHANGE_TOLERANCE = 10;	//5
    }

    /**
     * Constructor. Sets trip name to today's date. *
     */
    public Trip(int carSetPoint, boolean USE_KM, double MULL_FEET_TOLERANCE,
                int MULL_SECONDS_TOLERANCE, int FPS_SPEED_CHANGE_TOLERANCE,
                int TIME_PAUSE_SECONDS, double STOP_PLACE)
    {
    	this.carSetPoint= carSetPoint;

        this.USE_KM = USE_KM;
        this.MULL_FEET_TOLERANCE = MULL_FEET_TOLERANCE;
        this.MULL_SECONDS_TOLERANCE = MULL_SECONDS_TOLERANCE;
        this.STOP_PLACE = STOP_PLACE;
        this.TIME_PAUSE_SECONDS = TIME_PAUSE_SECONDS;
        this.FPS_SPEED_CHANGE_TOLERANCE = FPS_SPEED_CHANGE_TOLERANCE;
        
        name= Travelog.DAY.format(new Date());
    }

    public static void log(String msg)
    {
        if (LOG) {
            System.out.println(msg);
        }
    }

    /**
     * @param points the points to set
     * <p/>
     */
    public void setPoints(List<TrackPoint> points)
    {
        this.points = points;
    }

    /**
     * Returns number of total points in entire trip.
     * <p/>
     * @return number of total points in entire trip
     * <p/>
     */
    @JsonIgnore
    public int getNumberOfPoints()
    {
        int pointNum = 0;
        for (TrackSegment ts : segments) {
            pointNum += ts.points.size();
        }
        return pointNum;
    }

    /**
     * Returns first point in Trip.
     * <p/>
     * @return first point in Trip
     * <p/>
     */
    @JsonIgnore
    public TrackPoint getFirstPoint()
    {
        return segments.get(0).points.get(0);
    }

    /**
     * Returns last point in Trip.
     * <p/>
     * @return last point in Trip
     * <p/>
     */
    @JsonIgnore
    public TrackPoint getLastPoint()
    {
        return segments.get(segments.size() - 1).points.get(segments.get(segments.size() - 1).points.size() - 1);
    }

    /**
     * Returns all points in trip.
     * <p/>
     * @return all TrackPoints in entire trip
     * <p/>
     */
    @JsonIgnore
    public List<TrackPoint> getPoints()
    {
        if (points == null || points.size() == 0) {
            points = new ArrayList<TrackPoint>();
            for (TrackSegment ts : segments) {
                points.addAll(ts.points);
            }
        }

        return points;
    }

    /**
     * makes a list of TrackSegments, which define the trip logically.
     * <p/>
     * @param points all points in trip
     * <p/>
     * @return list of TrackSegments
     * <p/>
     */
    public static Trip makeTrip(int carSetPoint, TrackSegment... points)
    {
        Trip trip = new Trip(carSetPoint);
        trip.segments.addAll(Arrays.asList(points));

//		trip.state.add(tripState.gpxOnly);
//		trip.state.remove(tripState.usingDB);
//		trip.state.add(tripState.usingTrip);

        //------New split by pauseIndoors/gps point time gap--------------
//newsplit:
        log(new Date().toString());
        int debug = 0;
        boolean splittingDone = false;
        while (!splittingDone) {
            splittingDone = true;
            log(String.format("whileSplittingDone: %d\n", trip.segments.size()));
            for (int k = 0; k < trip.segments.size(); k++) {
                final TrackSegment ts = trip.segments.get(k);
                if (ts.isSplitable()) {
                    //if pause/indoors, find gap between points
                    final List<TrackSegment> threeSegs = TrackSegment.splitListByTimeInterruption(ts.points, TIME_PAUSE_SECONDS);
                    if (threeSegs.size() == 3) {
                        trip.segments.remove(k);
                        trip.segments.add(k, threeSegs.get(2));
                        trip.segments.add(k, threeSegs.get(1));
                        trip.segments.add(k, threeSegs.get(0));
                        splittingDone = false;
                        break;
                    } else {
                    }
                }
            }
        }

//		/Travelog.seeList(pts, name)

        //------New split by stop-mulls--------------
        log(new Date().toString());
        debug = 0;
        splittingDone = false;
        while (!splittingDone) {
            splittingDone = true;
            log(String.format("whileSplittingDone: %d\n", trip.segments.size()));

            for (int k = 0; k < trip.segments.size(); k++) {

                final TrackSegment ts = trip.segments.get(k);
                //Travelog.seeList(ts.points, "L:"+k);
                if (ts.isSplitable()) {
                    //if pause/indoors, find gap between points
                    final List<TrackSegment> threeSegs = TrackSegment.splitListByMull(ts.points, MULL_FEET_TOLERANCE, MULL_SECONDS_TOLERANCE);
                    if (threeSegs.size() == 3) {
                        trip.segments.remove(k);
                        trip.segments.add(k, threeSegs.get(2));
                        trip.segments.add(k, threeSegs.get(1));
                        trip.segments.add(k, threeSegs.get(0));
                        splittingDone = false;
                        break;
                    } else {
                    }
                }
            }
        }

        //------New split by speed-changes--------------
        log(new Date().toString());
        debug = 0;
        splittingDone = false;
        while (!splittingDone) {
            splittingDone = true;
            log(String.format("swhileSplittingDone: %d\n", trip.segments.size()));

            for (int k = 0; k < trip.segments.size(); k++) {

                final TrackSegment ts = trip.segments.get(k);
                //Travelog.seeList(ts.points, "L:"+k);
                if (ts.isSplitable()) {
                    //if pause/indoors, find gap between points
                    final List<TrackSegment> threeSegs = TrackSegment.splitListBySpeedChange(ts.points, FPS_SPEED_CHANGE_TOLERANCE);
                    if (threeSegs.size() == 3) {
                        trip.segments.remove(k);
                        trip.segments.add(k, threeSegs.get(2));
                        trip.segments.add(k, threeSegs.get(1));
                        trip.segments.add(k, threeSegs.get(0));
                        splittingDone = false;
                        break;
                    } else {
                    }
                }
            }
        }

        //------removing empty segments-------------------
        log(new Date().toString());
        debug = 0;
        splittingDone = false;
        while (!splittingDone) {
            splittingDone = true;
            log(String.format("swhileSplittingDone: %d\n", trip.segments.size()));

            for (int k = 0; k < trip.segments.size(); k++) {

                final TrackSegment ts = trip.segments.get(k);
                //Travelog.seeList(ts.points, "L:"+k);
                if (ts.points.size() == 0) {
                    trip.segments.remove(k);
                    splittingDone = false;
                    break;

                }
            }
        }

        //-----set undef segments' types-----
        log(new Date().toString());
        for (TrackSegment ts : trip.segments) {
            if (ts.getType().equals(TrackSegment.caminarType.undef)) {
                ts.setType(TrackSegment.setCaminarType(ts.distance, ts.points));
            }
        }

        log(new Date().toString());

        //----------End New Splits--------------
        if (true) {
            return trip;
        }
        //----------End New Splits--------------


        //------Splitting Pauses-------------------
        List<TrackSegment> segs = null; //TrackSegment.splitListByTimeInterruption(points, Travelog.FPS_SPEED_CHANGE_TOLERANCE);
        splittingDone = (segs.size() == 0);

        debug = 0;

        while (!splittingDone) {
            log(String.format("whileSplittingDone: %d\n", debug));
            if (debug > 5) {
                splittingDone = true;
            }
            boolean splitOccurred = false;
            for (int s = 0; s < trip.segments.size(); s++) {
                log(String.format("tripsegs: %d\n", trip.segments.size()));
                TrackSegment ts = trip.segments.get(s);

                List<TrackSegment> splitSegments = TrackSegment.splitListByTimeInterruption(ts.points, FPS_SPEED_CHANGE_TOLERANCE);
                if (splitSegments.size() == 0) {
                    //no split
                } else {
                    trip.segments.remove(s);
                    trip.segments.add(s, splitSegments.get(0));
                    trip.segments.add(s + 1, splitSegments.get(1));
                    trip.segments.add(s + 2, splitSegments.get(2));
                    splitOccurred = true;
                    debug++;
                    break;
                }

                int tot = 0;
                for (TrackSegment seg : trip.segments) {
                    tot += seg.points.size();
                    //seeList(seg.points, "makeTrip::seg.points");
                    //System.out.print(seg.points.size()+ ", ");
                }
                log(String.format("total: %d\n", tot));
                //try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace();}

            }
            if (!splitOccurred) {
                splittingDone = true;
            }
        }

        //---------Splitting HangOut/Mulls--------------------------------------------------
        splittingDone = false;
        segs = null; //TrackSegment.splitListByMull(points, Travelog.MULL_FEET_TOLERANCE, Travelog.MULL_SECONDS_TOLERANCE);
        splittingDone = (segs.size() == 0);

        debug = 0;

        while (!splittingDone) {
            log(String.format("whileSplittingDone: %d\n", debug));
            if (debug > 5) {
                splittingDone = true;
            }
            boolean splitOccurred = false;
            for (int s = 0; s < trip.segments.size(); s++) {
                log(String.format("tripsegs: %d\n", trip.segments.size()));
                TrackSegment ts = trip.segments.get(s);

                List<TrackSegment> splitSegments = TrackSegment.splitListByMull(ts.points, MULL_FEET_TOLERANCE, MULL_SECONDS_TOLERANCE);
                if (splitSegments.size() == 0) {
                    //no split
                } else {
                    trip.segments.remove(s);
                    //splitSegments.get(0).type= TrackSegment.caminarType.mull;
                    trip.segments.add(s, splitSegments.get(0));
                    trip.segments.add(s + 1, splitSegments.get(1));
                    splitOccurred = true;
                    //debug++;
                    break;
                }

                int tot = 0;
                for (TrackSegment seg : trip.segments) {
                    tot += seg.points.size();
                    //Travelog.seeList(seg.points, "makeTrip::seg.points");
                }
                log(String.format("total: %d\n", tot));
                //try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace();}

            }
            if (!splitOccurred) {
                splittingDone = true;
            }
        }

        //---------Splitting Speed Changes--------------------------------------------------
        segs = null; //TrackSegment.splitListBySpeedChange(points, Travelog.FPS_SPEED_CHANGE_TOLERANCE);
        splittingDone = (segs.size() == 0);

        debug = 0;

        while (!splittingDone) {
            log(String.format("whileSplittingDone: %d\n", debug));
            if (debug > 5) {
                splittingDone = true;
            }
            boolean splitOccurred = false;
            for (int s = 0; s < trip.segments.size(); s++) {
                log(String.format("tripsegs: %d\n", trip.segments.size()));
                TrackSegment ts = trip.segments.get(s);

                List<TrackSegment> splitSegments = TrackSegment.splitListBySpeedChange(ts.points, FPS_SPEED_CHANGE_TOLERANCE);
                if (splitSegments.size() == 0) {
                    //no split
                } else {
                    trip.segments.remove(s);
                    trip.segments.add(s, splitSegments.get(0));
                    trip.segments.add(s + 1, splitSegments.get(1));
                    splitOccurred = true;
                    debug++;
                    break;
                }

                int tot = 0;
                for (TrackSegment seg : trip.segments) {
                    tot += seg.points.size();
                    //seeList(seg.points, "makeTrip::seg.points");
                }
                log(String.format("total: %d\n", tot));
                //try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace();}

            }
            if (!splitOccurred) {
                splittingDone = true;
            }
        }

        return trip;
    }

    /**
     * Expands given trip to new date/time.
     * <p/>
     * @param oldTrip original trip
     * @param start   new start time
     * @param end     new end time
     * <p/>
     * @return trip with new dates and times
     * <p/>
     */
    public static Trip expandTrip(Trip oldTrip, Date start, Date end)
    {
        Trip newTrip = new Trip(-1);

        long delay = start.getTime() - oldTrip.getFirstPoint().getTime().getTime();
        long newDuration = end.getTime() - start.getTime();
        long oldDuration = oldTrip.getLastPoint().getTime().getTime() - oldTrip.getFirstPoint().getTime().getTime();
        long multiplier = newDuration / oldDuration;

        for (TrackSegment oldSeg : oldTrip.segments) {
            List<TrackPoint> newPts = new ArrayList<TrackPoint>();
            for (TrackPoint oldPt : oldSeg.points) {
                TrackPoint newPt = (TrackPoint) oldPt.clone();
                newPt.setTime(oldPt.getTime().getTime() * multiplier + delay);
                newPts.add(newPt);
            }
            newTrip.segments.add(new TrackSegment(newPts, oldSeg.getType()));
        }

        return newTrip;
    }

    /**
     * Returns a list of two points containing 2 items: the minimum lat & long
     * and the maximum lat & long.
     * <p/>
     * @return list of two points containing min and max
     * <p/>
     */
    public List<TrackPoint> getMinMaxPoints()
    {
    	List<TrackPoint> minMax= new ArrayList<TrackPoint>();
    	if(segments.size() == 0)	return minMax;
    	
        TrackPoint min, max;
        min = (TrackPoint) segments.get(0).points.get(0).clone();
        max = (TrackPoint) segments.get(0).points.get(0).clone();

        for (TrackPoint pt : getPoints()) {
            if (pt.getLat() < min.getLat()) {
                min.setLat(pt.getLat());
            }
            if (pt.getLat() > max.getLat()) {
                max.setLat(pt.getLat());
            }
            if (pt.getLon() < min.getLon()) {
                min.setLon(pt.getLon());
            }
            if (pt.getLon() > max.getLon()) {
                max.setLon(pt.getLon());
            }
        }

        minMax.add(min);
        minMax.add(max);
        
        return minMax;
    }

    /**
     * @return the segments
     */
    public List<TrackSegment> getSegments()
    {
        return segments;
    }

    /**
     * @param segments the segments to set
     */
    public void setSegments(List<TrackSegment> segments)
    {
        this.segments = segments;
    }

    /**
     * @return the waypoints
     */
    public List<WayPoint> getWaypoints()
    {
        return waypoints;
    }

    /**
     * @param waypoints the waypoints to set
     */
    public void setWaypoints(List<WayPoint> waypoints)
    {
        this.waypoints = waypoints;
    }

	public String getName() {
		return null;
	}
	
    private void setCarLocationInSegments(int carSetPoint) {
           TrackSegment carSeg= getCarSegment();
           if(null != carSeg) { 
                   if(-1 == carSetPoint) {
                           carSeg.setType(TrackSegment.caminarType.car);

                   } else {
                           carSeg.setType(TrackSegment.caminarType.carSet);

                   }
           } else {
                   log("no car loc seg");
           }
   }

    /**
     * Returns car location TrackSegment.
     * @return car location TrackSegment
     **/
    @JsonIgnore
    public TrackSegment getCarSegment() {
            int ptNum= 0;
            for(TrackSegment seg: segments) {
                    if(seg.getType().equals(TrackSegment.caminarType.carSet) || seg.getType().equals(TrackSegment.caminarType.car)) {

                            return seg;
                    }
                    ptNum+= seg.points.size();
                    if(-1 != carSetPoint && ptNum >= carSetPoint) {
                            return seg;
                    }
            }
            //if no car segment
            return null;
    }

    /**
     * Returns whether car location was manually set.
     * @return whether car location was manually set
     **/
    @JsonIgnore
    public boolean isCarLocationSetInSegments() {
            /*log("carLocPt: "+ carSetPoint);
            if(carSetPoint == -1) {
                    log("carLocPt3: "+ carSetPoint);
            } else {
                    log("carLocPt4: "+ (carSetPoint == -1));
                    return true;
            }*/
            log("carLocPt2: "+ carSetPoint);
            for(TrackSegment seg: segments) {
                    if(seg.getType().equals(TrackSegment.caminarType.carSet))    return true;
            }
            return false;
    }
    
    /**
     * Returns Trip narrative.
     * @return entire trip narrative as a String
     **/
    public String getFullNarrative() {
            String desc= getTextDescription(this.getPoints())+Travelog.CR;
            double calories= 0.0;
            for(TrackSegment ts: segments) {
                    if(!(ts.getType().equals(TrackSegment.caminarType.undef) || ts.getType().equals(TrackSegment.caminarType.speedChange))) {
                            log(ts.getType().name());
                            desc+= ts.getTextDescription()+ Travelog.CR;
                            calories+= ts.getCalories();
                    }
            }
            //TODO: desc+= String.format("You burned %f calories today.", calories);
            return desc;
    }
    
    /** 
 * returns text description of a list of points.
 * @param points points to analyse to make description
 **/
public static String getTextDescription(List<TrackPoint> points) {
        StringBuilder logBuf= new StringBuilder();
        Formatter log = new Formatter(logBuf, Locale.US);
        
        log.format("Number of Points: %d%s",points.size(), Travelog.CR);
        if(!points.isEmpty())	log.format("Trip on %s from %s to %s%s", Travelog.DAY.format(points.get(0).getTime()), Travelog.TIME.format(points.get(0).getTime()).toLowerCase(), Travelog.TIME.format(points.get(points.size()- 1).getTime()).toLowerCase(), Travelog.TIME.format(points.get(points.size()- 1).getTime()).toLowerCase(), Travelog.CR);

        TrackSegment trkseg= new TrackSegment(points, caminarType.undef);
        //final List<TrackPoint> popPoints= TrackSegment.populatePoints(points);
        //*void*/TrackSegment.Xsplit(trkseg.points);
        log.format("Total Distance: %s%s", TrackSegment.formatDistance(TrackSegment.getTotalDistance(trkseg.points)), Travelog.CR);
        log.format("Avg Speed: %f%s", TrackSegment.getAvgFps(trkseg.points), Travelog.CR);
        
        return logBuf.toString();
}

}

