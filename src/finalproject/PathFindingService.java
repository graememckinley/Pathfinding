package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class PathFindingService {
	Tile source;
	Graph g;
	
	public PathFindingService(Tile start) {
    	this.source = start;
    }

	public abstract void generateGraph();
    
    //TODO level 4: Implement basic dijkstra's algorithm to find a path to the final unknown destination
    public ArrayList<Tile> findPath(Tile startNode) {

        // Define a new LinkedList for storing the path
        LinkedList<Tile> path = new LinkedList<>();

        // Set the cost of all vertices to infinity and predecessors to null
        for (Tile tile: this.g.vertices) {
            tile.costEstimate = Double.POSITIVE_INFINITY;
            tile.predecessor = null;
        }

        // Set the cost of startNode to zero
        startNode.costEstimate = 0;

        // Define a queue of the vertices
        TilePriorityQ queue = new TilePriorityQ(this.g.vertices);

        // Define a tile for storing the destination and then iterating
        Tile current = null;

        // Loop while the queue is not empty
        while (queue.size > 0) {

            // Remove the minimum tile
            Tile u = queue.removeMin();

            // Iterate through the neighbors
            for (Tile v: g.getNeighbors(u)) {

                // Relax edges
                if (v.costEstimate > (u.costEstimate + this.g.getEdge(u, v).weight)) {
                    queue.updateKeys(v, u, u.costEstimate + this.g.getEdge(u, v).weight);
                }
            }

            // Check if we are at the destination
            if (u.isDestination) {

                // Store the destination and break
                current = u;
                break;
            }
        }

        // Build the path
        while (current != null) {
            path.addFirst(current);
            current = current.predecessor;
        }

        return new ArrayList<Tile>(path);
    }
    
    //TODO level 5: Implement basic dijkstra's algorithm to path find to a known destination
    public ArrayList<Tile> findPath(Tile start, Tile end) {

        // Define a new LinkedList for storing the path
        LinkedList<Tile> path = new LinkedList<>();

        // Set the cost of all vertices to infinity and predecessors to null
        for (Tile tile: this.g.vertices) {
            tile.costEstimate = Double.POSITIVE_INFINITY;
            tile.predecessor = null;
        }

        // Set the cost of startNode to zero
        start.costEstimate = 0;

        // Define a queue of the vertices
        TilePriorityQ queue = new TilePriorityQ(this.g.vertices);

        // Define a tile for storing the destination and then iterating
        Tile current = null;

        // Loop while the queue is not empty
        while (queue.size > 0) {

            // Remove the minimum tile
            Tile u = queue.removeMin();

            // Iterate through the neighbors
            for (Tile v: g.getNeighbors(u)) {

                // Relax edges
                if (v.costEstimate > (u.costEstimate + this.g.getEdge(u, v).weight)) {
                    queue.updateKeys(v, u, u.costEstimate + this.g.getEdge(u, v).weight);
                }
            }

            // Check if we are at the destination
            if (u.equals(end)) {

                // Store the destination and break
                current = u;
                break;
            }
        }

        // Build the path
        while (current != null) {
            path.addFirst(current);
            current = current.predecessor;
        }

        return new ArrayList<Tile>(path);
    }

    //TODO level 5: Implement basic dijkstra's algorithm to path find to the final destination
    // passing through given waypoints
    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints){

        // Define an ArrayList for storing the path
        ArrayList<Tile> path = new ArrayList<Tile>();
        path.add(start);

        // Iterate through the waypoints
        for (Tile waypoint: waypoints) {

            // Get and remove the last tile in the list
            // This will be the new starting point
            Tile subStart = path.get(path.size() - 1);
            path.remove(subStart);

            // Add the path to the waypoint to our path
            path.addAll(findPath(subStart, waypoint));
        }

        // Find the path from the last waypoint to the destination
        Tile lastWaypoint = path.get(path.size() - 1);
        path.remove(lastWaypoint);
        path.addAll(findPath(lastWaypoint));

        return path;
    }
        
}

