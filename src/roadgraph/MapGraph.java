/** @author UCSD MOOC development team and Margarita Ostrovskaia
 * date: 04/06/2019 */
package roadgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

 /** A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between */
public class MapGraph {
	private HashMap<GeographicPoint, IntersectionNode> intersections;
	private int countEdges;
	private boolean isLengthCompare;
	
	/** Create a new empty MapGraph */
	public MapGraph() {
		countEdges = 0;
		isLengthCompare = true;
		intersections = new HashMap<GeographicPoint, IntersectionNode>();
	}
	
	/**Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph. */
	public int getNumVertices() {
		return intersections.size();
	}
	
	/**Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints */
	public Set<GeographicPoint> getVertices() {
		return intersections.keySet();
	}
	
	/**Get the number of road segments in the graph
	 * @return The number of edges in the graph. */
	public int getNumEdges() {
		return countEdges;
	}
	
	/**Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node was already in the graph, or the parameter is null). */
	public boolean addVertex(GeographicPoint location) {
		if(intersections.containsKey(location) || location == null)
			return false;
		
		this.intersections.put(location, new IntersectionNode(location, isLengthCompare));
		return true;
	}
	
	/**Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been added as nodes to the graph, 
	 * 	if any of the arguments is null, or if the length is less than 0. */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName, String roadType, double length) throws IllegalArgumentException {
		// get intersection node in location 'from'
		IntersectionNode node = intersections.get(from);
		if(node.addRoad(to, roadName, roadType, length))
			countEdges++;
	}
	
	/** Find the path */
	private List<GeographicPoint> findGPPath(GeographicPoint start, GeographicPoint goal, HashMap<GeographicPoint, GeographicPoint> parentMap) {
		if(parentMap.size() == 0)
			return null;
		
		//int count = 1; // for test
		LinkedList<GeographicPoint> result = new LinkedList<>();
		GeographicPoint nextPoint = goal;
		result.add(nextPoint);
		while(nextPoint != null) {
			//System.out.println(String.format("findGPPath %d: %s", count, nextPoint.toString()));  // for test
			
			if (nextPoint.equals(start)) {
				//System.out.println(String.format("findGPPath %d points", count)); // for test
				return result;
			}
			//count++; // for test
			
			nextPoint = parentMap.get(nextPoint);
			result.addFirst(nextPoint);
		}
		
		return null;
	}

	/** Find the path from start to goal using breadth first search
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted) path from start to goal (including both start and goal). */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted) path from start to goal (including both start and goal). */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
		// check variables
		if (start == null || goal == null) {
			System.out.println("bfs: Start or goal node is null!  No path exists.");
			return null;
		}
		
		// initialization
		HashMap<GeographicPoint, GeographicPoint> parentMap = new HashMap<GeographicPoint, GeographicPoint>();
        Queue<GeographicPoint> queue = new LinkedList<>();
		HashSet<GeographicPoint> visited = new HashSet<GeographicPoint>();
		
		// breadth first search
		boolean isFound = false;
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
        		GeographicPoint currentIntersection = queue.remove();
        		if(currentIntersection.equals(goal)) {
        			isFound = true;
        			break;
        		}
        		
        		IntersectionNode currentNode = intersections.get(currentIntersection);
            if(currentNode != null) {
            		// hook for visualization
            		nodeSearched.accept(currentIntersection);
                
            		for(RoadNode r : currentNode.getNextRoads()) {
                		if (!visited.contains(r.getEndLocation())) {
                			visited.add(r.getEndLocation());
	                		queue.add(r.getEndLocation());
	                		parentMap.put(r.getEndLocation(), currentIntersection);
                		}
                }
            }
        }

        // route not found
		if (!isFound) {
			System.out.println("bfs: No path exists");
			return null;
		}

		// if route found, reconstruct the path
		return findGPPath(start, goal, parentMap);
	}

	/** Find the path from start to goal using Dijkstra's algorithm
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from start to goal (including both start and goal).*/
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from start to goal (including both start and goal). */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
		// check variables
		if (start == null || goal == null) {
			System.out.println("dijkstra: Start or goal node is null!  No path exists.");
			return null;
		}
		
		// initialization 
		HashMap<GeographicPoint, GeographicPoint> parentMap = new HashMap<>();
		HashSet<IntersectionNode> visited = new HashSet<>();
		PriorityQueue<IntersectionNode> queue = new PriorityQueue<>();
		
		// dijkstra search
		boolean isFound = false;
		IntersectionNode startNode = intersections.get(start);
		startNode.setDistanceToStart(new Double(0));
		startNode.setTimeToStart(new Double(0));
		queue.add(startNode);
        
        while (!queue.isEmpty()) {
        		IntersectionNode currentNode = queue.remove();
    			if(!visited.contains(currentNode)) {
    				visited.add(currentNode); 
        			GeographicPoint currentPoint = currentNode.getLocation();
        			
    				//System.out.println(String.format("dijkstra visited: %s", currentNode.toString())); //for test
        			// Hook for visualization
    				nodeSearched.accept(currentPoint); 
    				if(currentPoint.equals(goal)) {
    					isFound = true;
    					break;
    				}

            		for(RoadNode road : currentNode.getNextRoads()) {
            			GeographicPoint endPoint = road.getEndLocation();
            			IntersectionNode nextNode = intersections.get(endPoint);
                		if (!visited.contains(nextNode)) {
                			if(isLengthCompare) {
                				// Length Compare
	                			double actualDistance = nextNode.getDistanceToStart();
	                			double predictedDistance = currentNode.getDistanceToStart() + road.getLength();
	                			if(predictedDistance < actualDistance) {
		                			nextNode.setDistanceToStart(predictedDistance);
		
			                		parentMap.put(endPoint, currentPoint);
			                		queue.add(nextNode);
	                			}
                			}
                			else {
                				// Time Compare
	                			double actualTime = nextNode.getTimeToStart();
	                			double predictedTime = currentNode.getTimeToStart() + road.getRoadTime();
	                			if(predictedTime < actualTime) {
		                			nextNode.setTimeToStart(predictedTime);
		
			                		parentMap.put(endPoint, currentPoint);
			                		queue.add(nextNode);
	                			}
                			}
                		}
                }
    			}
        }
        
        // clear distance
        for(IntersectionNode v : visited)
        		v.setDefaultDistance();

        // route not found
		//System.out.println(String.format("dijkstra visited %d points", visited.size())); // for test
		if (!isFound) {
			//System.out.println("dijkstra: No path exists"); // for test
			return null;
		}

		// if route found, reconstruct the path
		return findGPPath(start, goal, parentMap);
	}

	/** Find the path from start to goal using A-Star search
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from start to goal (including both start and goal). */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from start to goal (including both start and goal). */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
		// check variables
		if (start == null || goal == null) {
			//System.out.println("aStarSearch: Start or goal node is null!  No path exists."); // for test
			return null;
		}
		
		// initialization 
		IntersectionNode startNode = intersections.get(start);
		IntersectionNode endNode = intersections.get(goal);
        HashMap<GeographicPoint,GeographicPoint> parentMap = new HashMap<>();
        HashSet<IntersectionNode> visited = new HashSet<>();
        HashMap<IntersectionNode, Double> distances = new HashMap<>();
        Queue<IntersectionNode> priorityQueue = new PriorityQueue<>();
 
        //  A Star Search
        startNode.setDistanceToStart(new Double(0));
        startNode.setTimeToStart(new Double(0));
        distances.put(startNode, new Double(0));
        priorityQueue.add(startNode);
        IntersectionNode currentNode = null;
 
        boolean isFound = false;
        while (!priorityQueue.isEmpty()) {
        		currentNode = priorityQueue.remove();
 
            if (!visited.contains(currentNode) ){
                visited.add(currentNode);
    				GeographicPoint currentPoint = currentNode.getLocation();
    				
    				//System.out.println(String.format("aStarSearch visited: %s", currentNode.toString())); // for test
    				// Hook for visualization
				nodeSearched.accept(currentPoint);
				if(currentPoint.equals(goal)) {
					isFound = true;
					break;
				}
 
                List<RoadNode> roads = currentNode.getNextRoads();
                for (RoadNode road : roads) {
	        			GeographicPoint endPoint = road.getEndLocation();
	        			IntersectionNode nextNode = intersections.get(endPoint);
	        			
                    if (!visited.contains(nextNode) ){ 
            				if(isLengthCompare) {
	                        double predictedDistance = nextNode.getLocation().distance(endNode.getLocation());
	                        double totalDistance = currentNode.getDistanceToStart() + road.getLength() + predictedDistance;
	 
	                        // check if distance smaller
	                        Double currentTotalDistance = distances.get(nextNode);
	                        if(currentTotalDistance == null)
	                        		currentTotalDistance = Double.MAX_VALUE;
	                        
	                        if(totalDistance < currentTotalDistance ){
	                            // update n's distance
	                            distances.put(nextNode, totalDistance);
	
	                            nextNode.setDistanceToStart(totalDistance);
	                            parentMap.put(endPoint, currentPoint);
	                            priorityQueue.add(nextNode);
	                        }
            				}
            				else {
    	                        double predictedTime = nextNode.getLocation().distance(endNode.getLocation()) / 40 * 60;
    	                        double totalTime = currentNode.getTimeToStart() + road.getRoadTime() + predictedTime;
    	 
    	                        // check if distance smaller
    	                        Double currentTotalTime = distances.get(nextNode);
    	                        if(currentTotalTime == null)
    	                        		currentTotalTime = Double.MAX_VALUE;
    	                        
    	                        if(totalTime < currentTotalTime ){
    	                            // update n's distance
    	                            distances.put(nextNode, totalTime);
    	
    	                            nextNode.setTimeToStart(totalTime);
    	                            parentMap.put(endPoint, currentPoint);
    	                            priorityQueue.add(nextNode);
    	                        }
            				}
                    }
                }
            }
        }
        
        // clear distance
        for(IntersectionNode v : visited)
        		v.setDefaultDistance();

        // route not found
		//System.out.println(String.format("aStarSearch visited %d points", visited.size())); // for test
		if (!isFound) {
			//System.out.println("aStarSearch: No path exists"); // for test
			return null;
		}

		// if route found, reconstruct the path
		return findGPPath(start, goal, parentMap);
	}
/*	
	public static void main(String[] args)
	{
		// You can use this method for testing.  
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// Here are some test cases you should try before you attempt the Week 3 End of Week Quiz, 
		// EVEN IF you score 100% on the programming assignment.
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		System.out.println("\n\nTest 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		System.out.println(String.format("Start: %s, End: %s", testStart.toString(), testEnd.toString()));
		System.out.println("\nTest 1 using simpletest: Dijkstra should be 9");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		System.out.println("\nTest 1 using simpletest: AStar should be 5");
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("\n\nTest 2 using utc: Dijkstra should be 13 and AStar should be 5.");
		System.out.println(String.format("Start: %s, End: %s", testStart.toString(), testEnd.toString()));
		System.out.println("\nTest 2 using utc: Dijkstra should be 13.");
		testroute = testMap.dijkstra(testStart,testEnd);
		System.out.println("\nTest 2 using utc: AStar should be 5.");
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("\n\nTest 3 using utc: Dijkstra should be 37 and AStar should be 10");
		System.out.println(String.format("Start: %s, End: %s", testStart.toString(), testEnd.toString()));
		System.out.println("\nTest 3 using utc: Dijkstra should be 37");
		testroute = testMap.dijkstra(testStart,testEnd);
		System.out.println("\nTest 3 using utc: AStar should be 10");
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		// Use this code in Week 3 End of Week Quiz
		MapGraph theMap = new MapGraph();
		System.out.print("\n\nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		System.out.println(String.format("\nWeek Quiz using utc: Start %s, End %s", start.toString(), end.toString()));

		System.out.println("\nWeek Quiz using utc");
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		System.out.println("\nWeek Quiz using utc");
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
	}
*/
}