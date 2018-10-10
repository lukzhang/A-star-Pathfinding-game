package pathfinding;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/*
Initializes and updates the game. Renders the map grid and the UI panel to the 
right. Implements mouse listener so that user can click on the buttons and choose
which cells to convert to.
*/
public class Game extends JPanel implements MouseListener
{
        
	private Map map;
	private Player player;                  //The ant
        private Food theFood;                   //The object placed in the goal node
	private List<Node> path;                //The path to the goal node
	private List<Node> evo;                 //The path obtained from each step in the A* search algorithm
        ArrayList<List<Node>> pathEvolution;    //Contains all the paths within the search algorithm
        
        //Determines which object should be placed on grid
        private boolean openterrain, grassland, swampland, obstacles, startpoint, food;
        //To start the game, the ant and food must be placed on the map
        private boolean startPointPlaced, foodPlaced;
        
        //The buttons to determine which object should be placed on grid
        private Rectangle Swampland;
        private Rectangle Grassland;
        private Rectangle Openland;
        private Rectangle Obstacle;
        private Rectangle Startpoint;
        private Rectangle Food;
        private Rectangle Go;
        
        //Sprites
        private BufferedImage grass_img = null;
        private BufferedImage clearpath_img = null;
        private BufferedImage obstacle_img = null;
        private BufferedImage mud_img = null;
        private BufferedImage apple_img = null;
        private BufferedImage ant_img = null;
        private BufferedImage go_img = null;
        
        //2D array to initalized map. Integers on right with '99' indicate that these
        //cells are not part of the main map. This is determined in the 'Node' class
        int[][] m4 = { //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 }, //
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 99, 99, 99 },  };

	public Game()
	{
		int[][] m = m4;
                
		setPreferredSize(new Dimension(m[0].length * 32, m.length * 32));
		addMouseListener(this);
		
                //Initializes the map with the 2d array
		map = new Map(m);
                //Hide at first by setting it to 100 (which is off the map)
		player = new Player(100, 1);
                //Food which is hidden by setting it outside of map
                theFood = new Food(100, 1);
                
                //Buttons
                Swampland = new Rectangle(16*32, 0*64, 96, 64);
                Grassland = new Rectangle(16*32, 1*64, 96, 64);
                Openland = new Rectangle(16*32, 2*64, 96, 64);
                Obstacle = new Rectangle(16*32, 3*64, 96, 64);
                Startpoint = new Rectangle(16*32, 4*64, 96, 64);
                Food = new Rectangle(16*32, 5*64, 96, 64);
                //Last button is wider
                Go = new Rectangle(16*32, 6*64, 96, 128);
                
                //Sprites
                BufferedImageLoader loader = new BufferedImageLoader();
                grass_img = loader.loadImage("/grassland.png");
                clearpath_img = loader.loadImage("/clearpath.png");
                obstacle_img = loader.loadImage("/obstacle.png");
                mud_img = loader.loadImage("/mud.png");
                apple_img = loader.loadImage("/apple.png");
                ant_img = loader.loadImage("/ant.png");
                go_img = loader.loadImage("/go.png");
                
	}

	public void update()
	{
		player.update();
	}

	public void render(Graphics2D g)
	{
		map.drawMap(g, path);
                //Draws grid
		g.setColor(Color.GRAY);
		for (int x = 0; x < getWidth(); x += 32)
		{
			g.drawLine(x, 0, x, getHeight());
		}
		for (int y = 0; y < getHeight(); y += 32)
		{
			g.drawLine(0, y, getWidth(), y);
		}
                
                /*
                Draws buttons
                */
                g.setColor(Color.ORANGE);
                g.fill(Swampland);
                
                //Grass
                g.drawImage(grass_img, Grassland.x, Grassland.y, Grassland.width, Grassland.height, null);
                
                //Mud
                g.drawImage(mud_img, Swampland.x, Swampland.y, Swampland.width, Swampland.height, null);
                
                //Openland
                g.drawImage(clearpath_img, Openland.x, Openland.y, Openland.width, Openland.height, null);
                
                //Obstacle
                g.drawImage(obstacle_img, Obstacle.x, Obstacle.y, Obstacle.width, Obstacle.height, null);
                
                //Food
                g.setColor(Color.BLACK);
                g.fill(Food);
                g.drawImage(apple_img, Food.x, Food.y, Food.width, Food.height, null);
                
                //Ant
                g.setColor(Color.GRAY);
                g.fill(Startpoint);
                g.drawImage(ant_img, Startpoint.x, Startpoint.y, Startpoint.width, Startpoint.height, null);
                
                g.setColor(Color.GRAY);
                g.fill(Go);
                g.drawImage(go_img, Go.x, Go.y + 16, Go.width, Go.width, null);
		
		/*
                Food then ant get rendered last so they are on top of everything
                */
                
                //Food to be drawn (an apple)
                g.drawImage(apple_img, theFood.getX()*32, theFood.getY()*32, 32, 32, null);                
                
                //Ant to be drawn
                g.drawImage(ant_img, player.getX() * 32 + player.getSx(), 
                        player.getY() * 32 + player.getSy(), 32, 32, null);
                
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{            
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
            //Coordinates divided by 32 so they correspond to the Node index
		int mx = e.getX() / 32;
		int my = e.getY() / 32;
                
                //For clicking on the buttons to the right
                if(map.getNode(mx, my).isNotCell()){
                    //Don't do anything (or press the button)
                    //Perhaps put rect here for the buttons
                    
                    Point2D.Float p1 = new Point2D.Float(e.getX(), e.getY());
                    
                    if(Swampland.contains(p1)){
                        setSwampland(true);
                        setGrassland(false);
                        setOpenterrain(false);
                        setStartpoint(false);
                        setFood(false);                        
                    }
                    else if(Grassland.contains(p1)){
                        setSwampland(false);
                        setGrassland(true);
                        setOpenterrain(false);
                        setObstacles(false);
                        setStartpoint(false);
                        setFood(false);
                    }
                    else if(Openland.contains(p1)){
                        setSwampland(false);
                        setGrassland(false);
                        setOpenterrain(true);
                        setObstacles(false);
                        setStartpoint(false);
                        setFood(false);
                    }
                    else if(Obstacle.contains(p1)){
                        setSwampland(false);
                        setGrassland(false);
                        setOpenterrain(false);
                        setObstacles(true);
                        setStartpoint(false);
                        setFood(false);
                    }
                    else if(Startpoint.contains(p1)){
                        setSwampland(false);
                        setGrassland(false);
                        setOpenterrain(false);
                        setObstacles(false);
                        setStartpoint(true);
                        setFood(false);
                    }
                    else if(Food.contains(p1)){
                        setSwampland(false);
                        setGrassland(false);
                        setOpenterrain(false);
                        setObstacles(false);
                        setStartpoint(false);
                        setFood(true);
                    }
                    //Starts the game
                    else if(Go.contains(p1)){
                        //Must place both ant and food on map first to start the game
                         if(isStartPointPlaced() && isFoodPlaced()){
                             
                            //Reset the parent node from previous run
                            for (int x = 0; x < 16; x++)
                            {
                                for (int y = 0; y < 16; y++)
                                {
                                        map.getNode(x, y).setParent(null);
                                }
                            }
                            
                            //Old position so that once evolution is done, the ant
                            //can be placed back
                            int oldX = player.getX();
                            int oldY = player.getY();
                            
                            path = map.findPath(player.getX(), player.getY(), 
                                    theFood.getX(), theFood.getY());
                            
                            evo = map.getEvolution();
                            ArrayList<List<Node>> listOfLists = map.getListOfLists();
                            
                            //Counts which element in the evolution we are in
                            int counter = 0;
                            for(Node curr : evo){                                
                                
                                long timer = System.currentTimeMillis();                                
                                while(System.currentTimeMillis() - timer < 200){                                    
                                }                               
                                player.setX(curr.getX());
                                player.setY(curr.getY());
                                
                                //Reset all the paths to show path evo to false
                                for (int x = 0; x < 16; x++)
                                {
                                    for (int y = 0; y < 16; y++)
                                    {
                                        map.getNode(x, y).setShowPathEvo(false);
                                    }
                                }
                                
                                //Indicates each Node in the evolution
                                for(int i=0; i<listOfLists.get(counter).size(); i++){
                                    
                                    int currX = listOfLists.get(counter).get(i).getX();
                                    int currY = listOfLists.get(counter).get(i).getY();
                                    
                                    map.getNode(currX, currY).setShowPathEvo(true);
                                }
                                
                                counter++;
                            }
                            
                            //Reset all the paths to show path evo to false
                            for (int x = 0; x < 16; x++)
                            {
                                for (int y = 0; y < 16; y++)
                                {
                                    map.getNode(x, y).setShowPathEvo(false);
                                }
                            }
                            
                            //Put the ant back in its old position and move along the path
                            player.setX(oldX);
                            player.setY(oldY);
                            player.followPath(path);
                        }
                         else{
                             System.out.println("You need to place a starting point and food source");
                         }
                    }
                }
                else if(isSwampland()){
                    map.getNode(mx, my).setMOVEMENT_COST(4);
                    map.getNode(mx, my).setWalkable(true);
                }
                else if(isGrassland()){
                    map.getNode(mx, my).setMOVEMENT_COST(3);
                    map.getNode(mx, my).setWalkable(true);
                }
                else if(isOpenterrain()){
                    map.getNode(mx, my).setMOVEMENT_COST(1);
                    map.getNode(mx, my).setWalkable(true);
                }
                else if(isObstacles()){
                    map.getNode(mx, my).setMOVEMENT_COST(0);
                    map.getNode(mx, my).setWalkable(false);                    
                }
                else if(isStartpoint()){
                    if(map.getNode(mx, my).isWalkable()){
                        player.setX(mx);
                        player.setY(my);
                        setStartPointPlaced(true);
                    }
                }
                else if(isFood()){
                    if(map.getNode(mx, my).isWalkable()){
                        theFood.setX(mx);
                        theFood.setY(my);
                        setFoodPlaced(true);
                    }
                }		
               
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}
        

    public boolean isOpenterrain() {
        return openterrain;
    }

    public void setOpenterrain(boolean openterrain) {
        this.openterrain = openterrain;
    }

    public boolean isGrassland() {
        return grassland;
    }

    public void setGrassland(boolean grassland) {
        this.grassland = grassland;
    }

    public boolean isSwampland() {
        return swampland;
    }

    public void setSwampland(boolean swampland) {
        this.swampland = swampland;
    }

    public boolean isObstacles() {
        return obstacles;
    }

    public void setObstacles(boolean obstacles) {
        this.obstacles = obstacles;
    }

    public boolean isStartpoint() {
        return startpoint;
    }

    public void setStartpoint(boolean startpoint) {
        this.startpoint = startpoint;
    }

    public boolean isFood() {
        return food;
    }

    public void setFood(boolean food) {
        this.food = food;
    }

    public boolean isStartPointPlaced() {
        return startPointPlaced;
    }

    public void setStartPointPlaced(boolean startPointPlaced) {
        this.startPointPlaced = startPointPlaced;
    }

    public boolean isFoodPlaced() {
        return foodPlaced;
    }

    public void setFoodPlaced(boolean foodPlaced) {
        this.foodPlaced = foodPlaced;
    }
       
    
    
    
        
        
        

}