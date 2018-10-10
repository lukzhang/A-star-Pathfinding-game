package pathfinding;

/*
Each node corresponds to an index, its movement cost, and its parent. If it is 
an obstacle, 'walkable' is set to false
 */
public class Node
{
        //movement cost
	private int MOVEMENT_COST;

	//Indices
	private int x;
	private int y;
        
        //Is this Node walkable?
	private boolean walkable;

	//Used to get the path
	private Node parent;

	//The cost of getting from the first node to this node.
	private int g;

	//A heuristic that estimates the cost of the cheapest path from this node to the goal.
	private int h;
        
        //For buttons that are not part of the grid
        private boolean notCell;
        //Boolean to indicate we want to show its path evolution
        private boolean showPathEvo;

	/*
	 Initializes the node with indices, if it is walkable(not an obstacle), 
        movement cost, and if it is part of the map (or just the UI)
	 */
	public Node(int x, int y, boolean walkable, int MOVEMENT_COST, boolean notCell)
	{
		this.x = x;
		this.y = y;
		this.walkable = walkable;
                this.MOVEMENT_COST=MOVEMENT_COST;
                this.notCell=notCell;
	}

    public int getMOVEMENT_COST() {
        return MOVEMENT_COST;
    }

    public void setMOVEMENT_COST(int MOVEMENT_COST) {
        this.MOVEMENT_COST = MOVEMENT_COST;
    }
        

	//Sets the G score based on the parent's G score and the movement cost.
	public void setG(Node parent)
	{
		g = (parent.getG() + MOVEMENT_COST);
	}

	//Calculates and return the G score
	public int calculateG(Node parent)
	{
		return (parent.getG() + MOVEMENT_COST);
	}

	//Heuristic h score is found by taking the difference of the x and y values of the goal
        //and multiplying the movement cost
	public void setH(Node goal)
	{
		h = (Math.abs(getX() - goal.getX()) + Math.abs(getY() - goal.getY())) * MOVEMENT_COST;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	
	public int getY()
	{
		return y;
	}

	
	public void setY(int y)
	{
		this.y = y;
	}

	public boolean isWalkable()
	{
		return walkable;
	}

	
	public void setWalkable(boolean walkable)
	{
		this.walkable = walkable;
	}

	
	public Node getParent()
	{
		return parent;
	}

	
	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	
	public int getF()
	{
		return g + h;
	}

	
	public int getG()
	{
		return g;
	}

	
	public int getH()
	{
		return h;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		if (!(o instanceof Node))
			return false;
		if (o == this)
			return true;

		Node n = (Node) o;
		if (n.getX() == x && n.getY() == y && n.isWalkable() == walkable)
			return true;
		return false;
	}

    public boolean isNotCell() {
        return notCell;
    }

    public void setNotCell(boolean notCell) {
        this.notCell = notCell;
    }

    public boolean isShowPathEvo() {
        return showPathEvo;
    }

    public void setShowPathEvo(boolean showPathEvo) {
        this.showPathEvo = showPathEvo;
    }
        
    
        

}