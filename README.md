# Pathfinding

This projest was completed in fulfillment of COMP 250 - Intro to Computer Science at McGill University

The GUI and all code needed to visualize the project was provided by the course teaching assistants.
This includes all code in the `.idea`, `out.production.finalproject.finalproject`, and `system` packages.
For grading purposes, and to ensure that all file, class, and core method names were the same for all students, 
skeleton code for the `tiles` package and remaining `.java` files was provided.

## Overview

The goal for this project was to become comfortable working with graphs as well as implementing algorithms for solving practical problems.
The main task was to consider all the different elements like desert, mountains, etc. and devise a plan to travel from a starting location to a safe house.
To achieve this, Dijkstra's algorithm was implemented to find the shortest distance path and the shortest time path,
and the Lagrangian Relaxation Based Aggregated Cost (LARAC) algorithm was implemented to find the shortest distance path subject to a health constraint.
In addition to this, waypoint setting and a metro system for faster travel were also provided.

### GUI
* **Menu:** The menu provides ways to navigate through maps and supports functionalities to modify the GUI visula output.
  * Control: basic command to manipulate the map.
  * Maps: where to initialize the maps.
  * View: utility functions related to map display.
    * Display system log: toggle whether or not to show the system log.
    * Display tile text: toggle whether or not to show text for each tile in the map.
    * Display grid: toggle whether or not to show the grid border.
    
* **Command Panel**: This is where the different commands are issued based on the users need, available buttons include:
  * Add waypoint
  * BFS traversal
  * DFS traversal
  * Shortest path
  * Fastest path
  * Safety first!
  
* **Console Panel**: This is where important messages are displayed, including system messages and user messages.

### Tiles

The map is a 2D grid-based tile design consisting of six different `Tile` regions: plains, deserts, mountains, facility, metro, and zombie infected ruins.
The `Tile` class has several fields that can be accessed directly:

* `isDestination`: a boolean variable indicating whether or not the tile is the destination.
* `isStart`: a boolean variable indicating whether or not this is the tile where the path begins.
* `xCoord` and `yCoord`: this tile's x and y coordinates in the map, starting from the top left.
* `nodeId`: a unique index number for each tile object.
* `neighbours`: an array list of all the tiles connected to this tile on the map.
* `distanceCost`, `timeCost`, and `damageCost`: the cost of travelling to this tiles in terms of damage, time, and physical damage respectively.
These are the weights used for the graph implementation.
* `predecessor`: stores the predecessor `Tile` used in Dijkstra's algorithm.
* `costEstimate`: updated and used in Dijkstra's algorithm.

Each tile type has the following costs:

| name/cost | distance | time | damage|
|:--:|:--:|:--:|:--:|
| plain | 3 | 1 | 0 |
| desert | 2 | 6 | 3 |
| mountain | 100 | 100 | 100 |
| facility | 1 | 2 | 0 |
| metro | 1 | 1 | 2 |
| zombie ruins| 1 | 3 | 5 |

Mountains are considered highly dangerous terrain and are not deemed "walkable".

### Run & Simulation

Run the `Main.java` file to bring up the GUI.
In order to generate a path according to the options listed in the Command Panel, first select one of the four map options.
Following this, any of the different traversals can be selected.
No additional action is needed to generate a different traversal, simply click on the appropriate button.
In order to simulate the path and visualize the course, press the simulation button from the Control Menu after generating a path successfully.

## Highlights

### `GraphTraversal.java`

This file contains the first two algorithms implemented, a [breadth first search](https://en.wikipedia.org/wiki/Breadth-first_search) (BFS)
and a [depth first search](https://en.wikipedia.org/wiki/Depth-first_search) (DFS).

* `BFS(Tile start)`: This method takes a `Tile` as imput which represents the starting point of the traversal and traverses the map,
finding all reachable tiles using BFS. 
It returns an array list containing the tiles in the same order in which they were visited.
* `DFS(Tile start)`: This method takes a `Tile` as imput which represents the starting point of the traversal and traverses the map,
finding all reachable tiles using DFS.
It returns an array list containing the tiles in the same order in which they were visited.

### `Graph.java`

In order to properly implement Dijkstra's algorithm, a graph and a priority queue need to be implemented.
`Graph.java` defines a data type to represent a double directed weighted graph where the cost of travelling between two tiles
connected by an edge is determined by where we are headed.
Depending on which graph we are making, the edges are able to take on different weight values (distance, cost, damage).
This class contains a constructor which takes in an `ArrayList<Tile> vertices` and the following methods:
`addEdge()`, `getAllEdges()`, `updateEdge()`, `getNeighbors()`, and `computePathCost()`.
It also contains the `Edge` subclass.

### `TilePriorityQ.java`

This class implements the priority queue necessary for Dijkstra's algorithm in the form of a min heap.
The entire data structure is implemented using an array where the elements are of type `Tile` and they are compared
based on the cost estimated to reach each tile from a source tile.
This class has a constructor which takes in an `ArrayList<Tile> verices` and a number of methods that deal with the various operations necessary for a priority queue.

### `PathFindingService.java`

This class deals with the implementation of Dijkstra's algorithm. An explanation of Dijkstra's algorithm can be found [here](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm).
There are three implementations of the `findPath()` method, containing different versions of Dijstra's algorithm.
* `findPath(Tile startNode)`: Dijkstra's algorithm to find a path to the final unknown destination
* `findPath(Tile start, Tile end)`: Dijkstra's algorithnm to find a path to a known destination.
* `findPath(Tile start, LinkedList<Tile> waypoints)`: Dijkstra's algorithm to find a path to the final destination passing through given waypoints.
This implementation makes use of the two other `findPath()` methods, first calling on `findPath(Tile start, Tile end)` for each waypoint
and then calling on `findPath(Tile startNode)` to find the path from the final waypoint to the final unknown destination.

The method `generateGraph()` which initializes the field `Graph g` is overriden in each subclasses of `PathFindingServices.java`.
This means that the algorithm is independent of which cost is used for the graph's edge weighting (distance, time, damage).

### `MetroTile.java`

The method `fixMetro()` assigns different distance and time costs to metro tiles.
By implementing this method it allows two metro tiles to be considered to be `neighbors` allowing for direct travel between the two tiles.
Previously, only adjacent tiles were `neighbors`.
Metro tiles are only currently present in Map 3 and this implementation does not allow for there to be more than two metro tiles in the same map.

### `SafestShortestPath.java`

This class deals with implementation of the LARAC algorithm.
An explanation of the algorithm can be found [here](https://cs.ou.edu/~thulasi/Misc/AKCE%20October%2025.pdf).
This implementation deals with finding the shortest path from the start to the destination that also has a damage cost less than our `health`
(i.e. the shortest safest path).
The algorithm introduces aggregated cost to replace graph cost (weight) and optimizes it through iterations until it finds the optimal cost (weight)
that satisfies the constaint.
Based on the pseudocode in the linked resource, pc would be our `costGraph` (in this case the minimum distance path but also could have been the minimum time path)
and pd would be our `damageGraph`.
Since before we run the LARAC algorithm we deal with cases in which there is no path with damage cost less than our `health`,
the algorithm will always return an answer that keeps us alive if one exists.
The health value can be adjusted on screen above the console panel.
