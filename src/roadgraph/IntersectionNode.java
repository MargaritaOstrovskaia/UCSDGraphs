/** @author Margarita Ostrovskaia
 * date: 04/06/2019 */
package roadgraph;

import java.util.ArrayList;
import java.util.List;

import geography.GeographicPoint;

/** A class to represent a Node in a graph which is a intersection of roads.*/
public class IntersectionNode implements Comparable<IntersectionNode> {
	
	private GeographicPoint location;
	private List<RoadNode> roads;
	
	private double distanceToStart;
	private double timeToStart;
	
	private boolean isLengthCompare;
	
	/** create a new intersection */
	public IntersectionNode(GeographicPoint location, boolean isLengthCompare) {
		setDefaultDistance();
		this.isLengthCompare = isLengthCompare;
		this.location = location;
		roads = new ArrayList<>();
	}

	/** create new road & add road to this intersection */
	public boolean addRoad(GeographicPoint endRoadLocation, String roadName, String roadType, double length) {
		RoadNode road = new RoadNode(location, endRoadLocation, roadName, roadType, length);
		return this.roads.add(road);
	}
	
	/** get list of roads */
	public List<RoadNode> getNextRoads() {
		return roads;
	}

	/** get intersection location */
	public GeographicPoint getLocation() {
		return location;
	}

	/** distance for search */
	public double getDistanceToStart() {
		return distanceToStart;
	}

	public void setDistanceToStart(double distanceForSearch) {
		if( this.distanceToStart > distanceForSearch )
			this.distanceToStart = distanceForSearch;
	}

	/** time for search */
	public double getTimeToStart() {
		return timeToStart;
	}

	public void setTimeToStart(double timeToStart) {
		if( this.timeToStart > timeToStart )
			this.timeToStart = timeToStart;
	}

	/** set default for search */
	public void setDefaultDistance() {
		this.distanceToStart = Double.MAX_VALUE;
		this.timeToStart = Double.MAX_VALUE;
	}
	
	/** compare intersections */
	@Override
	public int compareTo(IntersectionNode o) {
		if (this.isLengthCompare) {
			if(this.distanceToStart > o.getDistanceToStart())
				return 1;
			else if(this.distanceToStart < o.getDistanceToStart())
				return -1;
			else 
				return 0;
		}
		else {
			if(this.timeToStart > o.getTimeToStart())
				return 1;
			else if(this.timeToStart < o.getTimeToStart())
				return -1;
			else 
				return 0;
		}
	}

	@Override
	public String toString() {
		String str = this.location.toString() + " ";
		if (this.isLengthCompare)
			return "distanceToStart " + distanceToStart + "; " + this.location.toString();
		else
			return "timeToStart " + timeToStart + "; " + this.location.toString();
	}
}