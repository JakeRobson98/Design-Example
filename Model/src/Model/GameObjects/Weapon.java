package Model.GameObjects;

import Model.Locations.*;
import View.View.CluedoCanvas;


import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Represents a weapon in the game
 */
public class Weapon {

    //private Game game;
    private String name;
    private String room;
    private Image img;
    private int xPos;
    private int yPos;

    /**
     * Constructs a new weapon object
     *
     * @param name - name of the weapon
     */
    public Weapon( String name){
        this.name = name;
        setImg();
    }


    /**
     *Equals method to compare to players
     */
    @Override
    public boolean equals(Object weapon){
        if( !(weapon instanceof Weapon) || (weapon == null))
            return false;

        return (((Weapon) weapon).getName().equals(getName()));
    }

    /**
     * Moves a weapon to a new room
     * SHOULD BE HANDLED BY MOVE
     * @param room - new room
     */
    public void move(Room room){
        //Room oldRoom = game.getRoom(this.room);
        //oldRoom.removeWeapon(this);
        this.room = room.getName();
        room.addWeapon(this);
    }

    /**
     * Paints the weapon tag
     *
     * @param g - graphics
     */
    public void paintTag(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        String s = name;
        Font font = new Font("Calibri", Font.PLAIN, 15);
        g2d.setFont(font);
        Rectangle2D r = font.getStringBounds(s, g2d.getFontRenderContext());
        g.setColor(Color.lightGray);
        g.fillRect(xPos + 10, yPos - 20,(int) r.getWidth() + 10, (int )r.getHeight());
        g.setColor(Color.black);
        g.drawRect(xPos + 10, yPos - 20,(int) r.getWidth() + 10, (int )r.getHeight());
        g2d.drawString(s, xPos + 15, yPos - 6);
    }


    // Getters and Setters

    public void setImg(){
        this.img = CluedoCanvas.loadImage("Weapon" + name + ".png");
    }

    public Image getImage(){
        return img;
    }

    public String getName(){
        return name;
    }

    public String getRoom(){
        return room;
    }

    public void setRoom(String room){
        this.room = room;
    }

    public int getXPos(){
        return xPos;
    }
    public int getYPos(){
        return yPos;
    }

    public void setXPos(int x){
        xPos = x;
    }
    public void setYPos(int y){
        yPos = y;
    }


}
