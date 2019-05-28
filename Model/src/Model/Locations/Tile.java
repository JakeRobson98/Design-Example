package Model.Locations;

import Model.GameObjects.*;
import java.awt.Point;
import java.awt.Rectangle;


/**
 * Represents a Tile on the board
 */
public class Tile implements Location{

    // Tile coordinates
    protected int x;
    protected int y;

    protected int xPos;
    protected int yPos;

    // player at this tile, null if none
    protected Player player;

    protected final int SIZE = 23;

    /**
     * Constructs a new Tile object
     *
     * @param x
     * @param y
     */
    public Tile(int x, int y){
        this.x = x;
        this.y = y;
        this.xPos = (x * SIZE) + 20;
        this.yPos = (y * SIZE) + 4;
    }

    public boolean contains(Point p){
        Rectangle r = new Rectangle(xPos, yPos, SIZE, SIZE);
        if(r != null && p != null)
            if(r.contains(p)){

                return true;
            }

        return false;
    }

    /**
     * Returns the Letter to represent the tiles starting character
     *
     * @return letter
     */
    public String getLetter(){
        switch(player.getCharacter()){
            case MISS_SCARLETT: return"S";
            case COLONEL_MUSTARD: return "M";
            case MRS_WHITE: return "W";
            case THE_REVERAND_GREEN: return "G";
            case MRS_PEACOCK: return "P";
            case PROFESSOR_PLUM: return "p";
        }return " ";
    }

    // Getters and Setters

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getSize(){
        return SIZE;
    }

    public Player getPlayer(){
        return player;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public int getXPos(){
        return xPos;
    }

    public int getYPos(){
        return yPos;
    }

}

