package finitestatemachine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

/*
Initializes and updates the game. Renders the map grid and the UI panel to the 
right. Implements mouse listener so that user can click on the buttons and choose
which cells to convert to.
*/
public class Game extends JPanel implements MouseListener
{

	private Map map;
	private Player player;                  //The ant of the game
        //The food. Note, food is shown differently where each node keeps track
        //if there is food on it
        private Food theFood;                   
        private House theHouse;                 //The house where ants start off
	private List<Node> path;                //The path to the goal node
        ArrayList<List<Node>> pathEvolution;    //How the path evolves(Not used here)
        
        //UI to start the game, and toggle number of starting ants
        private Rectangle Go;
        private Rectangle Up;
        private Rectangle Down;
        
        //Fills the UI
        private Rectangle Filler;
        
        //Sprites
        private BufferedImage apple_img = null;
        private BufferedImage ant_img = null;
        private BufferedImage go_img = null;
        private BufferedImage house_img = null;
        private BufferedImage up_img = null;
        private BufferedImage down_img = null;
        
        //All the ants
        LinkedList<Player> antColony;
        //Checks whether to add or remove new ant
        boolean addAntToColony = false;
        boolean removeAntFromColony = false;
        int removeIndex;
        //Number of ants
        int numberOfAnts;
        //ready to play
        boolean readyToPlay;
        
        //used to intialize the map. 99 means it is not part of the map
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
		
		map = new Map(m);
                //Initalize the first ant
		player = new Player(5, 5);
                player.setWandering(true);
                //Food which is hidden (not used here)
                theFood = new Food(100, 1);
                //House started at top left corner
                theHouse = new House(8,7);
                
                //Last button is wider
                Go = new Rectangle(16*32, 6*64, 96, 128);
                //Filler for the right hand part of map
                Filler = new Rectangle(16*32, 0*64, 96, 400);
                //Toggles number of ants
                Up = new Rectangle(16*32, 4*64, 96, 64);
                Down = new Rectangle(16*32, 5*64, 96, 64);
                
                
                //Sprites
                BufferedImageLoader loader = new BufferedImageLoader();
                apple_img = loader.loadImage("/apple.png");
                ant_img = loader.loadImage("/ant.png");
                go_img = loader.loadImage("/go.png");
                house_img = loader.loadImage("/house.png");
                up_img = loader.loadImage("/up.png");
                down_img = loader.loadImage("/down.png");
                
                //ant colony
                antColony = new LinkedList<Player>();
                
	}

	public void update()
	{
            if(readyToPlay){
                
            /*
            Should add and remove ants before the loop as not to cause errors
            */
            //Add ant to colony if needed
            if(addAntToColony){
                antColony.add(new Player(theHouse.getX(), theHouse.getY()));
                addAntToColony = false;
            }            
            //Remove ant from colony if needed
            if(removeAntFromColony){
                antColony.remove(removeIndex);
                removeAntFromColony = false;
            }
            
            
            //For each ant in the colony, update
            for(Player curr: antColony){
                curr.update();
                
                if(curr.isWandering()){
                    
                    if(!curr.isInMotion()){
                    
                        path = map.findPath(curr.getX(), curr.getY(), 
                                    curr.getTargetX(), curr.getTargetY());  //Make random in the future
                    
                        curr.followPath(path);
                        curr.setInMotion(true);
                    }
                    else{
                        
                        if(curr.getTargetX()==curr.getX()
                            && curr.getTargetY()==curr.getY()){
                            
                            curr.setInMotion(false);
                            curr.setWandering(true);
                            
                            randomPath(curr);
                            
                            //If player is not looking for water
                            if(!curr.isLookingForWater()){
                                //If ant goes across node with food...
                                if(map.getNode(curr.getX(), curr.getY()).isHasFood()){
                                    //ant will return to home
                                    curr.setWandering(false);
                                
                                    path = map.findPath(curr.getX(), curr.getY(), 
                                        theHouse.getX(), theHouse.getY());  //Make random in the future
                    
                                    curr.followPath(path);
                                
                                    //Ant picks up the food
                                    map.getNode(curr.getX(), curr.getY()).setHasFood(false);
                                
                                    System.out.println("RETURNING FOOD!");
                            }
                            }
                            else{
                                if(map.getNode(curr.getX(), curr.getY()).isHasWater()){
                                
                                    //Ant not looking for water anymore
                                    curr.setLookingForWater(false);
                                    
                                    System.out.println("WATER HAS BEEN DRUNK!");
                            }
                            
                            
                        }
                
                    }       
                    }
                }  
                else{
                    //If carrying food or water, if returns home begin wandering again
                    if(curr.getX()==theHouse.getX() && curr.getY()==theHouse.getY()){
                        
                        if(!curr.isLookingForWater()){
                            curr.setWandering(true);
                        
                            System.out.println("FOOD RETURNED");
                        
                            curr.setWandering(true);
                            
                            //Now player is looking for water
                            curr.setLookingForWater(true);
                            
                            randomPath(curr);   
                            
                            //Spawn new ant at home position
                            addAntToColony = true;
                        }
                         
                    }
                    
                }
                
                //If ant runs into poison, remove from colony
                if(map.getNode(curr.getX(), curr.getY()).isHasPoison()){
                    removeAntFromColony = true;
                    removeIndex = antColony.indexOf(curr);
                    System.out.println("Poisoned");
                }                
            }
            }
	}

        //Draws the map and its contents
	public void render(Graphics2D g)
	{
		map.drawMap(g, path);
                //Draws the grid
		g.setColor(Color.GRAY);
		for (int x = 0; x < getWidth(); x += 32)
		{
			g.drawLine(x, 0, x, getHeight());
		}
		for (int y = 0; y < getHeight(); y += 32)
		{
			g.drawLine(0, y, getWidth(), y);
		}
                
                //Filler for UI
                g.setColor(Color.ORANGE);
                g.fill(Filler);
                
                //up arrow
                g.drawImage(up_img, Up.x + 16, Up.y, Up.width - 32, Up.height, null);
                //down arrow
                g.drawImage(down_img, Down.x + 16, Down.y, Down.width - 32, Down.height, null);
                
                g.setColor(Color.GRAY);
                g.fill(Go);
                g.drawImage(go_img, Go.x, Go.y + 16, Go.width, Go.width, null);
                
                //Food to be drawn (an apple)
                g.drawImage(apple_img, theFood.getX()*32, theFood.getY()*32, 32, 32, null);                
                
                //Ant to be drawn
                for(Player curr: antColony){
                    g.drawImage(ant_img, curr.getX() * 32 + curr.getSx(), 
                        curr.getY() * 32 + curr.getSy(), 32, 32, null);
                }
                
                //House to be drawn
                g.drawImage(house_img, theHouse.getX()*32, theHouse.getY()*32, 32, 32, null);  
                
                //Text for number of ants
                g.setColor(Color.blue);
                g.setFont(new Font(null, Font.BOLD, 12));
                g.drawString("Starting Ants ", 16*32 + 4, 100);
                g.setFont(new Font(null, Font.BOLD, 14));
                g.drawString("" + numberOfAnts, 16*32 + 40, 125);
                
                g.setFont(new Font(null, Font.BOLD, 12));
                g.drawString("Ant Population ", 16*32 + 4, 160);
                g.setFont(new Font(null, Font.BOLD, 14));
                g.drawString("" + antColony.size(), 16*32 + 40, 185);
	}
        
        //Finds a neighbor cell at random within the map
        public void randomPath(Player curr){
                            //Finds number -1, 0, or 1
                            Random generator = new Random(); 
                            int i = generator.nextInt(3) - 1;
                            int j = generator.nextInt(3) - 1;
                            
                            //If outside bounds, pull opposite way
                            if(curr.getX()+i < 0){
                                i = 1;
                            }
                            else if(curr.getX()+i > 15){
                                i = - 1;
                            }
                            
                            if(curr.getY()+j < 0){
                                j = 1;
                            }
                            else if(curr.getY()+j > 15){
                                j = - 1;
                            }
                            
                            System.out.println(i + "," + j);
                        
                        //Set the target node to the new indices
                        curr.setTargetX(curr.getX()+i);
                        curr.setTargetY(curr.getY()+j);
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
                //Divides by 32 to find right index for each Node
		int mx = e.getX() / 32;
		int my = e.getY() / 32;
                
                //If mouse pressed outside of main map
                if(map.getNode(mx, my).isNotCell()){
                    
                    Point2D.Float p1 = new Point2D.Float(e.getX(), e.getY());
                    
                    //If waiting for player to set up number of ants
                    if(!readyToPlay){
                        if(Up.contains(p1)){
                            //limit to 100
                            if(numberOfAnts != 99){
                                numberOfAnts++;
                            }                            
                        }
                        else if(Down.contains(p1)){
                            if(numberOfAnts != 0)
                                numberOfAnts--;
                        }
                        else if(Go.contains(p1)){
                            readyToPlay=true; 
                            
                            //Add number of ants
                            for(int i=0; i<numberOfAnts; i++){
                                antColony.add(new Player(theHouse.getX(),theHouse.getY()));
                            }
                        }
                    }
                
                    
                }
               
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}
        

    
    
        
        
        

}
