package Model.GameObjects.Cards;
import Model.Locations.*;
import View.View.CluedoCanvas;

import java.awt.Image;

/**
 * Represents a Room Card in the Game.
 * There is one room card for evey room object
 *
 *  Kitchen
 *  Dining Room
 *  Lounge
 *  Hall
 *  Study
 *  Library
 *  Billiard Room
 *  Conservatory
 *  Ball Room
 *
 */
public class RoomCard implements Card{

    private Room room;
    private Image imgs[] = new Image[3];

    /**
     * Constucts a new roomCard object
     *
     * @param room
     */
    public RoomCard(Room room){
        this.room = room;
        setImages();
    }

    /**
     * @return - String representing this card
     */
    public String toString(){
        return room.getName();
    }

    // Getters and Setters

    public Room getRoom(){
        return room;
    }

    public Image getImage(int i){
        return imgs[i];
    }
    public void setImages(){
        imgs[0] = CluedoCanvas.loadImage(room.getName() +".jpg");
        imgs[1] = imgs[0].getScaledInstance(100, 146, 16);
        imgs[2] = imgs[0].getScaledInstance(80, 116, 16);
    }

}

