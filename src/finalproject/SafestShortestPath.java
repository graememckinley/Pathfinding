package finalproject;


import java.util.ArrayList;
import java.util.LinkedList;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.MetroTile;

public class SafestShortestPath extends ShortestPath {
	public int health;
	public Graph costGraph;
	public Graph damageGraph;
	public Graph aggregatedGraph;

	//TODO level 8: finish class for finding the safest shortest path with given health constraint
	public SafestShortestPath(Tile start, int health) {
		super(start);
		this.health = health;

		// Generate the graphs each time the constructor is called
		generateGraph();
	}

	@Override
	public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints) {

		// Set superclass graph field to costGraph
		this.g = this.costGraph;

		// Define the least distance path
		ArrayList<Tile> p_c = super.findPath(this.source, waypoints);

		// If the least distance path is less than health return
		if (this.damageGraph.computePathCost(p_c) < this.health) {
			return p_c;
		}

		// Set superclass graph field to damageGraph
		this.g = this.damageGraph;

		// Define the lease damage path
		ArrayList<Tile> p_d = super.findPath(this.source, waypoints);

		// Return null if damage cost is greater than health
		if (this.damageGraph.computePathCost(p_d) >= this.health) { // Change to > ?
			return null;
		}

		// Run the LARAC algorithm until something is returned
		while (true) {

			// Compute the multiplier
			double lambda = (this.costGraph.computePathCost(p_c) - this.costGraph.computePathCost(p_d))
					/ (this.damageGraph.computePathCost(p_d) - this.damageGraph.computePathCost(p_c));

			// Get all aggregated edges
			ArrayList<Graph.Edge> allAggEdges = this.aggregatedGraph.getAllEdges();

			// Update all edges
			for (Graph.Edge edge : allAggEdges) {
				edge.weight = this.costGraph.getEdge(edge.origin, edge.destination).weight
						+ lambda * this.damageGraph.getEdge(edge.origin, edge.destination).weight;
			}

			// Set the superclass graph field to aggregatedGraph
			this.g = this.aggregatedGraph;

			// Define the optimal aggregated path
			ArrayList<Tile> p_r = super.findPath(this.source, waypoints);

			// Decide which path to return
			if (this.aggregatedGraph.computePathCost(p_r) == this.aggregatedGraph.computePathCost(p_c)) {
				return p_d;

			} else if (this.damageGraph.computePathCost(p_r) < this.health) { // Change to <= ?
				p_d = p_r;

			} else {
				p_c = p_r;
			}
		}
	}

	public void generateGraph() {
		// TODO Auto-generated method stub

		// Create new graphs of only vertices
		this.costGraph = new Graph(GraphTraversal.BFS(this.source));
		this.damageGraph = new Graph(GraphTraversal.BFS(this.source));
		this.aggregatedGraph = new Graph(GraphTraversal.BFS(this.source));

		// Iterate through all the vertices in the graph
		for (Tile tile: this.costGraph.vertices) {

			// Iterate through all the neighbors and add edges
			for (Tile neighbor: tile.neighbors) {

				// Only add an edge if travel is possible
				if (neighbor.isWalkable()) {

					// If both tiles are metro
					if (tile.getTileType() == TileType.Metro && neighbor.getTileType() == TileType.Metro) {
						((MetroTile) neighbor).fixMetro(tile);
						this.costGraph.addEdge(tile, neighbor, ((MetroTile) neighbor).metroDistanceCost);

					} else {
						this.costGraph.addEdge(tile, neighbor, neighbor.distanceCost);
					}

					// Add to other graphs
					this.damageGraph.addEdge(tile, neighbor, neighbor.damageCost);
					this.aggregatedGraph.addEdge(tile, neighbor, neighbor.damageCost);
				}
			}
		}
	}

}
