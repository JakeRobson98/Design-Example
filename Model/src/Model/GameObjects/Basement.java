package Model.GameObjects;
import Model.Locations.*;
/**
 * Represents the basement which holds the 'solution
 */
public class Basement{

    // Solution
    private Room murderRoom;
    private Player.Character murderCharacter;
    private Weapon murderWeapon;

    /**
     * Constructs a new Basement object
     *
     * @param r - Solution room
     * @param r - Solution character
     * @param r - Solution weapon
     *
     */
    public Basement(Room r,Player.Character c, Weapon w){
        murderRoom = r;
        murderCharacter = c;
        murderWeapon = w;
    }

    // Getters and Setters

    public Room getMurderRoom(){
        return murderRoom;
    }

    public Player.Character getMurderCharacter(){
        return murderCharacter;
    }

    public Weapon getMurderWeapon(){
        return murderWeapon;
    }

    public void setMurderRoom(Room room){
        this.murderRoom = room ;
    }

    public void setMurderCharacter(Player.Character character){
        this.murderCharacter = character;
    }

    public void setMurderWeapon(Weapon weapon){
        this.murderWeapon = weapon;
    }


}
