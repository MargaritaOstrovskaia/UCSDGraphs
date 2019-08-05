/** @author Margarita Ostrovskaia
 * date: 04/06/2019 */
package roadgraph;

import geography.GeographicPoint;

 /** A class to represent a Node in a graph which is a road.*/
public class RoadNode {
	private GeographicPoint startLocation;
	private GeographicPoint endLocation;
	private String roadName;
	private String roadType;
	private double length;
	private double time;
	private int speed;
	
	/** create a new road */
	public RoadNode(GeographicPoint startLocation, GeographicPoint endLocation, String roadName, String roadType, double length) {
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.roadName = roadName;
		this.roadType = roadType.toLowerCase();
		this.length = length;
		
		this.calcSpeed();
		this.calcTime();
	}
	
	/** get road start point */
	public GeographicPoint getStartLocation() {
		return startLocation;
	}

	/** get road end point */
	public GeographicPoint getEndLocation() {
		return endLocation;
	}

	/** get road time */
	public double getRoadTime() {
		return this.time;
	}

	/** get road name */
	public String getRoadName() {
		return roadName;
	}

	/** get type of road */
	public String getRoadType() {
		return roadType;
	}

	/** get length of road (km) */
	public double getLength() {
		return length;
	}

	/** get speed of road (kph) */
	public int getSpeed() {
		return speed;
	}

	/** get time of road (min) */
	private double getTime() {
		return time;
	}

	private void calcTime() {
		if (speed == 0)
			time = Double.MAX_VALUE;
		else
			time = length/speed * 60;
	}
	
	private void calcSpeed() {
		if(roadType == null) {
			speed = 40;
			return;
		}
		
		switch (roadType) {
			case "motorway":
				speed = 110;
				break;
			case "motorway_link":
				speed = 45;
				break;
			case "trunk":
				speed = 100;
				break;
			case "trunk_link":
				speed = 40;
				break;
			case "primary":
				speed = 100;
				break;
			case "primary_link":
				speed = 30;
				break;
			case "secondary":
				speed = 55;
				break;
			case "secondary_link":
				speed = 25;
				break;
			case "tertiary":
				speed = 40;
				break;
			case "tertiary_link":
				speed = 20;
				break;
			case "unclassified":
				speed = 25;
				break;
			case "residential":
				speed = 25;
				break;
			default:
				speed = 40;
				return;
		}
	}
}