package finalproject;

import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.MetroTile;

public class FastestPath extends PathFindingService {
    //TODO level 6: find time prioritized path
    public FastestPath(Tile start) {
        super(start);

        // Generate a graph each time the constructor is called
        generateGraph();
    }

	@Override
	public void generateGraph() {
		// TODO Auto-generated method stub

        // Create a new graph of only vertices
        this.g = new Graph(GraphTraversal.BFS(this.source));

        // Iterate through all the vertices in the graph
        for (Tile tile: this.g.vertices) {

            // Iterate through all the neighbors and add edges
            for (Tile neighbor: tile.neighbors) {

                // Only add an edge if travel is possible
                if (neighbor.isWalkable()) {

                    // If both tiles are metro
                    if (tile.getTileType() == TileType.Metro && neighbor.getTileType() == TileType.Metro) {
                        ((MetroTile) neighbor).fixMetro((MetroTile) tile);
                        this.g.addEdge(tile, neighbor, ((MetroTile) neighbor).metroTimeCost);

                    } else {
                        this.g.addEdge(tile, neighbor, neighbor.timeCost);
                    }

                }
            }
        }
	}

}
