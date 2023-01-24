package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class GraphTraversal
{


	//TODO level 1: implement BFS traversal starting from s
	public static ArrayList<Tile> BFS(Tile s)
	{
		// Create an empty queue
		LinkedList<Tile> queue = new LinkedList<Tile>();

		// Create an empty ArrayList for storing the tiles we visit
		ArrayList<Tile> path = new ArrayList<Tile>();

		// Create a HashSet for storing visited tiles
		HashSet<Tile> visited = new HashSet<Tile>();

		// Enqueue s
		queue.add(s);
		visited.add(s);

		// While the queue is not empty
		while (queue.size() != 0) {

			// Dequeue a vertex from the queue
			Tile cur = queue.poll();
			path.add(cur);

			// Iterate through all the neighbors
			for (Tile neighbor: cur.neighbors) {

				// If the neighbor hasn't been visited and is walkable
				if (!visited.contains(neighbor) && neighbor.isWalkable()) {
					queue.add(neighbor);
					visited.add(neighbor);
				}
			}
		}

		return path;
	}


	//TODO level 1: implement DFS traversal starting from s
	public static ArrayList<Tile> DFS(Tile s) {

		// Create an empty stack
		LinkedList<Tile> stack = new LinkedList<Tile>();

		// Create an empty ArrayList for storing the tiles we visit
		ArrayList<Tile> path = new ArrayList<Tile>();

		// Create a HashSet for storing visited tiles
		HashSet<Tile> visited = new HashSet<Tile>();

		// Add s to the stack
		stack.add(s);
		visited.add(s);

		while (stack.size() != 0) {

			// Pop a vertex from the stack
			Tile cur  = stack.removeLast();
			path.add(cur);

			// Iterate through all the neighbors
			for (Tile neighbor: cur.neighbors) {

				// If the neighbor hasn't been visited and is walkable
				if (!visited.contains(neighbor) && neighbor.isWalkable()) {
					stack.add(neighbor);
					visited.add(neighbor);
				}
			}
		}
		return path;
	}

}  