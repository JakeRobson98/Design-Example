package Model.GameObjects.Cards;


import java.awt.Image;
import Model.GameObjects.*;
import View.View.CluedoCanvas;

/**
 * Represents a Weapon Card in the Game.
 * There is one weapon card for every Weapon Object
 *
 * Candlestick
 * Dagger
 * Lead Pipe
 * Revolver
 * Rope
 * Spanner
 *
 */
public class WeaponCard implements Card{

    private Weapon weapon;
    private Image[] imgs = new Image[3];

    /**
     * Constructs a new WeaponCard Object
     *
     * @param weapon - Weapon for Card
     */
    public WeaponCard(Weapon weapon){
        this.weapon = weapon;
        setImages();
    }

    /**
     * @return - String representing this card
     */
    public String toString(){
        return weapon.getName();
    }

    // Getters and Setters

    public Weapon getWeapon(){
        return weapon;
    }

    public Image getImage(int i){
        return imgs[i];
    }

    public void setImages(){
        imgs[0] = CluedoCanvas.loadImage(weapon.getName() + ".jpg");
        imgs[1] = imgs[0].getScaledInstance(100, 146, 16);
        imgs[2] = imgs[0].getScaledInstance(80, 116, 16);

    }

}

