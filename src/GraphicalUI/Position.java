package GraphicalUI;


public class Position {

    private int xPos;
    private int yPos;


    public  Position (int xpos, int ypos){
        this.xPos = xpos;
        this.yPos = ypos;
    }
    public int getX (){ return xPos; }

    public int getY () { return yPos; }

    public String toString(){
        return "X = " + xPos + " Y = " + yPos;
    }
}
