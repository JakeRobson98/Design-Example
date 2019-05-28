package Model.Locations;

public class DoorTile extends Tile{

    private String room;
    /**
     * Constructs a new DoorTile object
     *
     * @param x
     * @param y
     * @param room - room which the doorway enters
     */
    public DoorTile(int x, int y, String room) {
        super(x, y);
        this.room = room;
    }

    public String getRoom(){
        return this.room;
    }
}
