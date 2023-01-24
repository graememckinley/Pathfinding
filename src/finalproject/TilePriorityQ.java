package finalproject;

import java.util.ArrayList;
import java.util.Arrays;


import finalproject.system.Tile;

public class TilePriorityQ {
	// TODO level 3: Add fields that can help you implement this data type

	Tile[] priorityQueue;

	int size;

	int front;

	// TODO level 3: implement the constructor for the priority queue
	public TilePriorityQ (ArrayList<Tile> vertices) {

		// Initialize all fields
		this.size = 0;
		this.front = 1;
		this.priorityQueue = new Tile[vertices.size() + 1];

		// Iterate through vertices and add them to the priority queue
		for (Tile vertex: vertices) {
			insert(vertex);
		}

		// Call minHeap to ensure no properties are broken
		downHeap(front, this.size);
	}

	// TODO level 3: implement remove min as seen in class
	public Tile removeMin() {

		// Define a tile as the root of the min heap
		Tile popped = this.priorityQueue[front];

		// Reset the root
		this.priorityQueue[front] = this.priorityQueue[this.size];
		this.size--;

		// Down-heap the new root
		downHeap(front, this.size);

		return popped;
	}

	// TODO level 3: implement updateKeys as described in the pdf
	public void updateKeys(Tile t, Tile newPred, double newEstimate) {

		// If the queue contains t
		if (Arrays.asList(this.priorityQueue).contains(t)) {

			// Set the new cost estimate and predecessor of t
			this.priorityQueue[Arrays.asList(this.priorityQueue).indexOf(t)].predecessor = newPred;
			this.priorityQueue[Arrays.asList(this.priorityQueue).indexOf(t)].costEstimate = newEstimate;

			// Fix the properties of the heap
			upHeap(Arrays.asList(this.priorityQueue).indexOf(t));
			downHeap(Arrays.asList(this.priorityQueue).indexOf(t), this.size);
		}
	}

	// Return the parent position
	public int parent(int pos) {
		return pos / 2;
	}

	// Return the left child position
	public int leftChild(int pos) {
		return (pos * 2);
	}

	// Return the right child position
	public int rightChild(int pos) {
		return (pos * 2) + 1;
	}

	// Return if a Tile is a leaf
	public boolean isLeaf(int pos) {
		return rightChild(pos) >= this.size || leftChild(pos) >= this.size;
	}

	// Insert a tile into the priority queue
	public void insert(Tile tile) {

		// Increase the size of the queue
		this.size++;

		// Insert the new tile at the end of the queue
		this.priorityQueue[this.size] = tile;

		upHeap(this.size);
	}

	public void upHeap(int pos) {
		// Define i
		int i = pos;

		// Up-heap the new tile through the queue
		while (i > 1 && this.priorityQueue[i].costEstimate < this.priorityQueue[i / 2].costEstimate) {
			swap(i, i / 2);
			i = i / 2;
		}
	}

	// Applies the min heap method to a specific position
	public void downHeap(int start, int max) {

		// Define i
		int i = start;

		// While there is a left child
		while (2 * i <= max) {
			int child = 2 * i;

			// If there is a right child
			if (child < max) {

				// If the right child is less than the left child
				if (this.priorityQueue[child + 1].costEstimate < this.priorityQueue[child].costEstimate) {
					child++;
				}
			}

			// If we need to swap with the child
			if (this.priorityQueue[child].costEstimate < this.priorityQueue[i].costEstimate) {
				swap(i, child);
				i = child;

			} else {
				break;
			}
		}

	}

	// Swap two tiles
	public void swap(int x, int y) {

		// Define a temporary Tile
		Tile temp = this.priorityQueue[x];

		// Swap the Tiles
		this.priorityQueue[x] = this.priorityQueue[y];
		this.priorityQueue[y] = temp;
	}

	public void print() {
		for (int i = front; i < (this.size / 2); i++) {
			System.out.println("PARENT: " + this.priorityQueue[i] + " " + this.priorityQueue[i].costEstimate
					+ " LEFT CHILD: " + this.priorityQueue[2 * i] + " " + this.priorityQueue[2 * i].costEstimate
					+ " RIGHT CHILD: " + this.priorityQueue[2 * i + 1] + " " + this.priorityQueue[2 * i + 1].costEstimate);

		}
		System.out.println();
	}

}
