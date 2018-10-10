package pathfinding;

/**
 * The coordinates of where the Food should be placed. There is only one instance
 * of Food for this game.
 */
public class Food {
    
    private int x;
    private int y;
    
    Food(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    
}
