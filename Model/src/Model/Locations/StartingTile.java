package Model.Locations;
import Model.GameObjects.Player;

/**
 * Represents a tile on the board where a character
 * starts
 */
public class StartingTile extends Tile{

    private Player.Character startingCharacter;

    /**
     *Constructs a new starting tile
     *
     * @param x
     * @param y
     * @param startingCharacter
     */
    public StartingTile(int x, int y, Player.Character startingCharacter) {
        super(x, y);
        this.startingCharacter = startingCharacter;
    }


    // Getters and Setters

    public Player.Character getCharacter(){
        return startingCharacter;
    }
}