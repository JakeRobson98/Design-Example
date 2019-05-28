package Model.GameObjects.Cards;

import java.awt.Image;
import Model.GameObjects.*;
import View.View.CluedoCanvas;
import Controller.*;
/**
 * Represents a Suspect Card in the Game.
 * There is one room card for evey Character
 *
 * Miss Scarlett
 * Colonel Mustard
 * Mrs. White
 * The Reverend Green
 * Mrs. Peacock
 * Professor Plum
 *
 */
public class SuspectCard implements Card {

    private Player.Character character;
    private Image[] imgs = new Image[3];
    Controller controller;

    /**
     * Constructs a new SuspectCard object
     *
     * @param c - Suspect for the Card
     */
    public SuspectCard(Player.Character c, Controller controller){
        this.character = c;
        this.controller = controller;
        setImages();
    }

    /**
     * @return - String representing this Card
     */
    public String toString(){
        return character.toString();
    }

    // Getters and Setters

    public Player.Character getCharacter(){
        return character;
    }

    public Image getImage(int i){
        return imgs[i];
    }

    public void setImages(){
        imgs[0] = CluedoCanvas.loadImage("Player" + controller.getCharacterName(character) + ".jpg");
        imgs[1] = imgs[0].getScaledInstance(100, 146, 16);
        imgs[2] = imgs[0].getScaledInstance(80, 116, 16);

    }


}

