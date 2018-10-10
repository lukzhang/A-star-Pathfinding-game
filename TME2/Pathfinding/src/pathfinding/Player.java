package pathfinding;

import java.util.LinkedList;
import java.util.List;

/*
The ant of the game that has a fixing and walking sequence. The fixing aligns it
to the center of the Node and walking moves it along the path to the goal. 
*/

public class Player
{
        //Coordinates that correspond to the Node
	private int x;
	private int y;
        //The values in between each node as it moves
	private int sx;
	private int sy;

        //Speed
	private int speed;

        //Determines if the ant is walking or fixing
	private boolean walking;
	private boolean fixing;
        //Path to the goal node
	private List<Node> path;

	public Player(int x, int y)
	{
		this.x = x;
		this.y = y;
		sx = 0;
		sy = 0;
		speed = 2;

		walking = false;
		fixing = false;
		path = null;
	}

	public void update()
	{
		if (fixing)
		{
			fix();
		}
		if (walking)
		{
  			walk();
		}
	}

	public void followPath(List<Node> path)
	{
		this.path = path;               
                walking = true;
                
		if (walking)
		{
			fixing = true;
			walking = false;
		}
		else
		{
			walking = true;
		}

	}

        //Adjust the ant so it is center of the node
	private void fix()
	{
		if (sx > 0)
		{
			sx -= speed;
			if (sx < 0)
			{
				sx = 0;
			}
		}
		if (sx < 0)
		{
			sx += speed;
			if (sx > 0)
			{
				sx = 0;
			}
		}
		if (sy > 0)
		{
			sy -= speed;
			if (sy < 0)
			{
				sy = 0;
			}
		}
		if (sy < 0)
		{
			sy += speed;
			if (sy > 0)
			{
				sy = 0;
			}
		}
		if (sx == 0 && sy == 0)
		{
			fixing = false;
			walking = true;
		}
	}
	
        //Moves the ant along the path by going to the next node in the linked
        //list and adjusting its sx and sy to the speed
	private void walk()
	{
		if (path == null)
		{
			walking = false;
			return;
		}
		if (path.size() <= 0)
		{
			walking = false;
			path = null;
			return;
		}
		Node next = ((LinkedList<Node>) path).getFirst();
		if (next.getX() != x)
		{
			sx += (next.getX() < x ? -speed : speed);
			if (sx % 32 == 0)
			{
				((LinkedList<Node>) path).removeFirst();
				if (sx > 0)
					x++;
				else
					x--;
				sx %= 32;
			}
		}
		else if (next.getY() != y)
		{
			sy += (next.getY() < y ? -speed : speed);
			if (sy % 32 == 0)
			{
				((LinkedList<Node>) path).removeFirst();
				if (sy > 0)
					y++;
				else
					y--;
				sy %= 32;
			}
		}
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

	public int getSx()
	{
		return sx;
	}

	public void setSx(int sx)
	{
		this.sx = sx;
	}

	public int getSy()
	{
		return sy;
	}

	public void setSy(int sy)
	{
		this.sy = sy;
	}

}
