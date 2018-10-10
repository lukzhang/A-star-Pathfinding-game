package pathfinding;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
* Contains the nodes within the grid of the map. Incorporates the A* algorithm to find
* the path to the goal node. Stores each evolution step towards the goal node. 
 */
public class Map
{
	//The width of the map, in columns.
	private int width;
	
	//The height of the map, in rows.
	private int height;

	//Array full of nodes to be used for the pathfinding.
	private Node[][] nodes;
        
        //Shows the evolution of the path
        private List<Node> Evolution;
        //Each item in list contains a list for each node in evolution
        private ArrayList<List<Node>> listOfLists;
        
        //Sprites
        private BufferedImage grass_img = null;
        private BufferedImage clearpath_img = null;
        private BufferedImage obstacle_img = null;
        private BufferedImage mud_img = null;

	/*
	Map is created from a 2D array. The number corresponds to its movement cost. 
        99 means that it is not part of the map.
	 */
	public Map(int[][] map)
	{
		this.width = map[0].length;
		this.height = map.length;
		nodes = new Node[width][height];

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
                            //Make cost 1 to start off with
			    nodes[x][y] = new Node(x, y, map[y][x] != 0, map[y][x], map[y][x] == 99);
			}
		}
                
                Evolution = new LinkedList<Node>();
                listOfLists = new ArrayList<List<Node>>();
                
                //Sprites
                BufferedImageLoader loader = new BufferedImageLoader();
                grass_img = loader.loadImage("/grassland.png");
                clearpath_img = loader.loadImage("/clearpath.png");
                obstacle_img = loader.loadImage("/obstacle.png");
                mud_img = loader.loadImage("/mud.png");
	}

	/*
        Draws the map where each node is filled with a certain sprite depending on its
        movement cost. 
	 */
	public void drawMap(Graphics g, List<Node> path)
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
                                //Obstacle
				if (!nodes[x][y].isWalkable())
				{
                                    g.drawImage(obstacle_img, nodes[x][y].getX()*32, 
                                            nodes[x][y].getY()*32, 32, 32, null);
				}
                                //Clear path
                                else if(nodes[x][y].getMOVEMENT_COST() == 1){
                                    g.drawImage(clearpath_img, nodes[x][y].getX()*32, 
                                            nodes[x][y].getY()*32, 32, 32, null);
                                }
                                //Grass
                                else if(nodes[x][y].getMOVEMENT_COST() == 3){
                                    g.drawImage(grass_img, nodes[x][y].getX()*32, 
                                            nodes[x][y].getY()*32, 32, 32, null);
                                }
                                //Swamp
                                else if(nodes[x][y].getMOVEMENT_COST() == 4){
                                    g.drawImage(mud_img, nodes[x][y].getX()*32, 
                                            nodes[x][y].getY()*32, 32, 32, null);
                                }
                                //Default color
				else
				{
					g.setColor(Color.BLACK);
				}
                                
                                //For showing the path evolution. If a particular cell
                                //is within the current path on display, it is hilighted 
                                //with a transparent color
                                if(nodes[x][y].isShowPathEvo()){
                                    int alpha = 60; // 25% transparent
                                    Color myColour = new Color(0, 0, 0, alpha);
                                    g.setColor(myColour);
                                    g.fillRect(x * 32, y * 32, 32, 32);
                                }
			}
		}
	}

        
	/**
         * Not used
         * 
	 * Prints the map to the standard out, where each walkable node is simply
	 * not printed, each non-walkable node is printed as a '#' (pound sign) and
	 * each node that is in the path as a '@' (at sign).
	 * 
	 * @param path
	 *            Optional parameter. List containing the nodes to be drawn as
	 *            path nodes.
	 */
	public void printMap(List<Node> path)
	{
		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < width; i++)
			{
				if (!nodes[i][j].isWalkable())
				{
					System.out.print(" #");
				}
				else if (path.contains(new Node(i, j, true, 1, false)))
				{
					System.out.print(" @");
				}
				else
				{
					System.out.print("  ");
				}
			}
			System.out.print("\n");
		}
	}

	/*
        Return node with indices
	 */
	public Node getNode(int x, int y)
	{
		if (x >= 0 && x < width && y >= 0 && y < height)
		{
			return nodes[x][y];
		}
		else
		{
			return null;
		}
	}

	/**
	 * Tries to calculate a path from the start and end positions.
	 * 
	 * @param startX
	 *            The X coordinate of the start position.
	 * @param startY
	 *            The Y coordinate of the start position.
	 * @param goalX
	 *            The X coordinate of the goal position.
	 * @param goalY
	 *            The Y coordinate of the goal position.
	 * @return A list containing all of the visited nodes if there is a
	 *         solution, an empty list otherwise.
	 */
	public final List<Node> findPath(int startX, int startY, int goalX, int goalY)
	{
            
                //Clear arraylist for path evolution
                Evolution.clear();
                listOfLists.clear();
            
		// If our start position is the same as our goal position ...
		if (startX == goalX && startY == goalY)
		{
			// Return an empty path, because we don't need to move at all.
			return new LinkedList<Node>();
		}
                

		// The set of nodes already visited.
		List<Node> openList = new LinkedList<Node>();
		// The set of currently discovered nodes still to be visited.
		List<Node> closedList = new LinkedList<Node>();

		// Add starting node to open list.
		openList.add(nodes[startX][startY]);
                
		// This loop will be broken as soon as the current node position is
		// equal to the goal position.
		while (true)
		{
			// Gets node with the lowest F score from open list.
			Node current = lowestFInList(openList);
			// Remove current node from open list.
			openList.remove(current);
			// Add current node to closed list.
			closedList.add(current);
                        
                        //Add the current node to the evolution path
                        Evolution.add(current);
                        // Add the path to the node for each node in the list
                        List<Node> evoPath = new LinkedList<Node>();
                        Node curr = current;
                        while(curr != null){
                            evoPath.add(curr);
                            curr = curr.getParent();
                        }
                        listOfLists.add(evoPath);

			// If the current node position is equal to the goal position ...
			if ((current.getX() == goalX) && (current.getY() == goalY))
			{
				// Return a LinkedList containing all of the visited nodes.
				return calcPath(nodes[startX][startY], current);
			}

			List<Node> adjacentNodes = getAdjacent(current, closedList);
			for (Node adjacent : adjacentNodes)
			{
				// If node is not in the open list ...
				if (!openList.contains(adjacent))
				{
					// Set current node as parent for this node.
					adjacent.setParent(current);
					// Set H costs of this node (estimated costs to goal).
					adjacent.setH(nodes[goalX][goalY]);
					// Set G costs of this node (costs from start to this node).
					adjacent.setG(current);
					// Add node to openList.
					openList.add(adjacent);
				}
				// Else if the node is in the open list and the G score from
				// current node is cheaper than previous costs ...
				else if (adjacent.getG() > adjacent.calculateG(current))
				{
					// Set current node as parent for this node.
					adjacent.setParent(current);
					// Set G costs of this node (costs from start to this node).
					adjacent.setG(current);
				}
			}

			// If no path exists ...
			if (openList.isEmpty())
			{
				// Return an empty list.
				return new LinkedList<Node>();
			}
			// But if it does, continue the loop.
                        
		}
	}

	/**
	 * @param start
	 *            The first node on the path.
	 * @param goal
	 *            The last node on the path.
	 * @return a list containing all of the visited nodes, from the goal to the
	 *         start.
	 */
	private List<Node> calcPath(Node start, Node goal)
	{
		LinkedList<Node> path = new LinkedList<Node>();

		Node node = goal;
		boolean done = false;
		while (!done)
		{
			path.addFirst(node);
			node = node.getParent();
			if (node.equals(start))
			{
				done = true;
			}
		}
		return path;
	}

	/**
	 * @param list
	 *            The list to be checked.
	 * @return The node with the lowest F score in the list.
	 */
	private Node lowestFInList(List<Node> list)
	{
		Node cheapest = list.get(0);
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).getF() < cheapest.getF())
			{
				cheapest = list.get(i);
			}
		}
		return cheapest;
	}

	/**
	 * @param node
	 *            The node to be checked for adjacent nodes.
	 * @param closedList
	 *            A list containing all of the nodes already visited.
	 * @return A LinkedList with nodes adjacent to the given node if those
	 *         exist, are walkable and are not already in the closed list.
	 */
	private List<Node> getAdjacent(Node node, List<Node> closedList)
	{
		List<Node> adjacentNodes = new LinkedList<Node>();
		int x = node.getX();
		int y = node.getY();

		Node adjacent;

		// Check left node
		if (x > 0)
		{
			adjacent = getNode(x - 1, y);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check right node
		if (x < width)
		{
			adjacent = getNode(x + 1, y);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check top node
		if (y > 0)
		{
			adjacent = this.getNode(x, y - 1);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check bottom node
		if (y < height)
		{
			adjacent = this.getNode(x, y + 1);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}
		return adjacentNodes;
	}

    public List<Node> getEvolution() {
        return Evolution;
    }

    public void setEvolution(List<Node> Evolution) {
        this.Evolution = Evolution;
    }

    public ArrayList<List<Node>> getListOfLists() {
        return listOfLists;
    }

    public void setListOfLists(ArrayList<List<Node>> listOfLists) {
        this.listOfLists = listOfLists;
    }

    
    
        
        

}
