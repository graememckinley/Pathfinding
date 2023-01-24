package finalproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.*;

public class Graph {
	// TODO level 2: Add fields that can help you implement this data type
    ArrayList<Tile> vertices;

    ArrayList<ArrayList<Edge>> edges;


    // TODO level 2: initialize and assign all variables inside the constructor
	public Graph(ArrayList<Tile> vertices) {
		this.vertices = vertices;
        this.edges = new ArrayList<ArrayList<Edge>>();

        // Create the nested arrays for edges
        for (int i = 0; i < this.vertices.size(); i++) {
            ArrayList<Edge> edge = new ArrayList<Edge>();
            this.edges.add(edge);
        }
	}
	
    // TODO level 2: add an edge to the graph
    public void addEdge(Tile origin, Tile destination, double weight){

        // Edge case checking
        if (origin.isWalkable() && this.vertices.contains(origin) && destination.isWalkable()
                && this.vertices.contains(destination)) {

            // Create a new edge and add it to edges
            Edge edge = new Edge(origin, destination, weight);
            this.edges.get(this.vertices.indexOf(origin)).add(edge);
        }
    }

    
    // TODO level 2: return a list of all edges in the graph
	public ArrayList<Edge> getAllEdges() {

        // Define a new array for storing all edges
        ArrayList<Edge> allEdges = new ArrayList<Edge>();

        // Edge case handling
        if (this.vertices != null && this.vertices.size() != 0) {

            // Iterate through edges
            for (ArrayList<Edge> arr: edges) {

                // Iterate through each sub-array
                for (Edge edge: arr) {
                    allEdges.add(edge);
                }
            }
        }

        return allEdges;
	}

    public void updateEdge(Edge edge, double value) {
        edge.weight = value;
    }

	// TODO level 2: return list of tiles adjacent to t
	public ArrayList<Tile> getNeighbors(Tile t) {

        // Define a new ArrayList for storing neighbors
        ArrayList<Tile> neighbors = new ArrayList<Tile>();

        // Edge case handling
        if (t.isWalkable() && this.vertices.contains(t)) {

            // Iterate through the edges of t
            for (Edge edge: edges.get(this.vertices.indexOf(t))) {
                neighbors.add(edge.destination);
            }
        }

        return neighbors;
    }
	
	// TODO level 2: return total cost for the input path
	public double computePathCost(ArrayList<Tile> path) {

        // Define a counter variable for the cost
        double cost = 0.0;

        // Edge case handling
        if (path.size() > 1) {

            // Iterate through the path
            for (int i = 0; i < (path.size() - 1); i++) {

                // Iterate through the edges for the tile at i
                for (Edge edge: this.edges.get(this.vertices.indexOf(path.get(i)))) {
                    if (edge.destination.equals(path.get(i + 1))) {
                        cost += edge.weight;
                    }
                }
            }
        }

        return cost;
	}

    public Edge getEdge(Tile origin, Tile destination) {

        // Iterate through the edges for the origin
        for (Edge edge: this.edges.get(this.vertices.indexOf(origin))) {
            if (edge.destination.equals(destination)) {
                return edge;
            }
        }
        return null;
    }
	
   
    public static class Edge{
    	Tile origin;
    	Tile destination;
    	double weight;

        // TODO level 2: initialize appropriate fields
        public Edge(Tile s, Tile d, double cost){
        	this.origin = s;
            this.destination = d;
            this.weight = cost;
        }
        
        // TODO level 2: getter function 1
        public Tile getStart(){
            return this.origin;
        }

        
        // TODO level 2: getter function 2
        public Tile getEnd() {
            return this.destination;
        }
        
    }
    
}