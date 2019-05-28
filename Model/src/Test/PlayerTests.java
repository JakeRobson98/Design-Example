package Test;


import static org.junit.Assert.*;

import Controller.Controller;
import org.junit.Test;

import Model.Board;
import Model.Locations.*;
import Model.GameObjects.*;

/**
 * tests for all the actions a player can do
 */
public class PlayerTests {

    //// Player Movement Tests ////

    @Test
    public void validTileToTile()
    // Moving a player from a Tile to another vaild Tile
    // Valid as in its within the number of squares of the diceroll
    // Should result in the player being on the new Tile, and nothing on the old tile
    {
        // Setup

        Controller controller = new Controller();
        Board board = controller.getBoard();
        Location oldLocation = (Location) board.getTile(7, 8); // 8,9
        Location newLocation = (Location) board.getTile(9, 8); // 10,9
        Player p = setupMockPlayer(controller, oldLocation, 1);
        controller.setCurrentPlayer(p);

        //Action
        controller.movePlayer(p, newLocation, 6);

        //Checks
        assertTrue(p.getLocation().equals(newLocation));		// Player location must be the new Location
        assertTrue(((Tile) newLocation).getPlayer().equals(p)); // The new Locations "player" field must equal the correct player
        checkTiles(board, (Tile) newLocation);					// The player shouldn't be on any other tile
        checkRooms(controller, null);									// The player shouldn't be in any other room
    }

    @Test
    public void invalidTileToTile()
    // Moving a player from a Tile to another Invaild Tile
    // Invalid as it is outside the number of squares of the diceroll
    // Should result in the player staying on the original tile and not moving
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();
        Location oldLocation = (Location) board.getTile(7, 8); // 8,9
        Location newLocation = (Location) board.getTile(16, 24); // 17,25
        Player p = setupMockPlayer(controller, oldLocation, 1);
        controller.setCurrentPlayer(p);

        // Action
        controller.movePlayer(p, newLocation, 6);

        // Checks
        assertTrue(p.getLocation().equals(oldLocation));	 // Player location must be the old Location
        assertTrue(((Tile) newLocation).getPlayer() == null);// The old Locations "player" field must still equal the same player
        checkTiles(board, (Tile) oldLocation);				 // The player shouldn't be on any other tile
        checkRooms(controller, null);								 // The player shouldn't be in any other room
    }

    @Test
    public void invalidTileToTile2()
    // Moving a player from a Tile to another Invaild Tile
    // Invalid as another player is already on the Tile
    // Should result in the both players staying on thier
    // original tiles and not moving
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();
        Location oldLocation = (Location) board.getTile(7, 8); // 8,9
        Location newLocation = (Location) board.getTile(9, 8); // 10,9
        Player p = setupMockPlayer(controller, oldLocation, 1);
        Player p2 = setupMockPlayer(controller, newLocation, 2);
        controller.setCurrentPlayer(p);

        // Action
        controller.movePlayer(p, newLocation, 6);

        //Checks
        assertTrue(p.getLocation().equals(oldLocation));		  // Player location must be the old Location
        assertTrue(((Tile) newLocation).getPlayer().equals(p2));  // The old Locations "player" field must still equal the same player
        checkTiles(board, (Tile) oldLocation, (Tile) newLocation);// There shouldn't be any players on any other Tiles
        checkRooms(controller, null); 								  // There shouldn't be any players in any rooms
    }

    @Test
    public void validTileToRoom()
    // Moving a player from a Tile to a Valid Room
    // Valid as in it has a door tile within the
    // number of tiles of the dice roll
    // Should result in the player moving into the room
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();
        Location oldLocation = (Location) board.getTile(7, 8); // 8,9
        Location newLocation = controller.getRoom("Ball Room");
        Player p = setupMockPlayer(controller, oldLocation, 1);
        controller.setCurrentPlayer(p);

        // Action
        controller.movePlayer(p, newLocation, 6);

        // Checks

        checkRooms(controller, (Room) newLocation);					  // The player shouldn't be on any other tile
        checkTiles(board, null);								  // The player shouldn't be in any other room
    }

    @Test
    public void invalidTileToRoom()
    // Moving a player from a Tile to a Invalid Room
    // Invalid as in it does not have a door tile within
    // the number of tiles as the dice roll
    // Should result in the player not moving
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();
        Location oldLocation = (Location) board.getTile(7, 8); // 8,9
        Location newLocation = controller.getRoom("Hall");
        Player p = setupMockPlayer(controller, oldLocation, 1);
        controller.setCurrentPlayer(p);

        // Action
        controller.movePlayer(p, newLocation, 6);

        // Checks
        assertTrue(p.getLocation().equals(oldLocation));	   // Player location must be the old Location
        assertTrue(((Tile) oldLocation).getPlayer().equals(p));// The old Locations "player" field must equal the correct player
        checkTiles(board, (Tile) oldLocation);				   // The player shouldn't be on any other tile
        checkRooms(controller, null);								   // The player shouldn't be in any other room
    }

    @Test
    public void validRoomToTile()
    // Moving a player from a Room to a Valid tile
    // Valid as in it is withn the number of tiles
    // of the dice from any one of the rooms door tiles
    // Should result in the player moving out of the room
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();
        Location oldLocation = controller.getRoom("Ball Room");
        Location newLocation = (Location) board.getTile(7, 8); // 8,9
        Player p = setupMockPlayer(controller, oldLocation, 1);
        controller.setCurrentPlayer(p);

        // Action
        controller.movePlayer(p, newLocation, 6);

        // Checks
        assertTrue(p.getLocation().equals(newLocation));	   // Player location must be the new Location
        assertTrue(((Tile) newLocation).getPlayer().equals(p));// The new Locations "player" field must equal the correct player
        checkTiles(board, (Tile) newLocation);				   // The player shouldn't be on any other tile
        checkRooms(controller, null);								   // The player shouldn't be in any other room
    }

    @Test
    public void invalidRoomToTile()
    // Moving a player from a Room to a Invalid tile
    // Invalid as in it is not withn the number of tiles
    // of the dice from any one of the rooms door tiles
    // Should result in the staying in the room
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();
        Location oldLocation = controller.getRoom("Study");
        Location newLocation = (Location) board.getTile(7, 8); // 8,9
        Player p = setupMockPlayer(controller, oldLocation, 1);
        controller.setCurrentPlayer(p);

        // Action
        controller.movePlayer(p, newLocation, 6);

        // Checks
        assertTrue(p.getLocation().equals(oldLocation));		  // Player location must be the old Location
        assertTrue(((Room) oldLocation).getPlayers().contains(p));// The old Locations "players" list must contain the correct player
        checkTiles(board, null);								  // The player shouldn't be on any other tile
        checkRooms(controller, (Room) oldLocation);					  // The player shouldn't be in any other room

    }

    @Test
    public void validRoomToRoom()
    // Moving a player from a Room to a Valid Room
    // Valid as in it is withn the number of tiles
    // of the dice from any one of the original rooms
    // door tiles
    // Should result in the player moving to the new
    // room
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();
        Location oldLocation = controller.getRoom("Study");
        Location newLocation = controller.getRoom("Hall");
        Player p = setupMockPlayer(controller, oldLocation, 1);
        controller.setCurrentPlayer(p);

        // Action
        controller.movePlayer(p, newLocation, 6);

        // Checks

        assertTrue(((Room) newLocation).getPlayers().contains(p));// The new Locations "players" list must contain the correct player
        checkTiles(board, null);								  // The player shouldn't be on any other tile
        checkRooms(controller, (Room) newLocation);					  // The player shouldn't be in any other room
    }

    @Test
    public void invalidRoomToRoom()
    // Moving a player from a Room to a Invalid Room
    // Invalid as in it is not withn the number of tiles
    // of the dice from any one of the original rooms
    // door tiles
    // Should result in the player not moving
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();
        Location oldLocation = controller.getRoom("Study");
        Location newLocation = controller.getRoom("Dining Room");
        Player p = setupMockPlayer(controller, oldLocation, 1);
        controller.setCurrentPlayer(p);

        // Action
        controller.movePlayer(p, newLocation, 6);

        // Checks
        assertTrue(p.getLocation().equals(oldLocation));		  // Player location must be the old Location
        assertTrue(((Room) oldLocation).getPlayers().contains(p));// The old Locations "players" list must contain the correct player
        checkTiles(board, null);								  // The player shouldn't be on any other tile
        checkRooms(controller, (Room) oldLocation);					  // The player shouldn't be in any other room
    }

    @Test
    public void validStairwayRoomToRoom()
    // Moving a player from a Room to a Valid Room
    // Using the stairway
    // Valid as in it is the correct corresponding room
    // Should result in the player moving to the new
    // room
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();
        Location oldLocation = controller.getRoom("Lounge");
        Location newLocation = controller.getRoom("Conservatory");
        Player p = setupMockPlayer(controller, oldLocation, 1);
        controller.setCurrentPlayer(p);

        // Action
        controller.movePlayer(p, newLocation, 6);

        // Checks
        assertTrue(p.getLocation().equals(newLocation));		  // Player location must be the new Location
        assertTrue(((Room) newLocation).getPlayers().contains(p));// The new Locations "players" list must contain the correct player
        checkTiles(board, null);								  // The player shouldn't be on any other tile
        checkRooms(controller, (Room) newLocation);					  // The player shouldn't be in any other room
    }

    @Test
    public void validMultiplePlayerInRoom()
    // Moving a player from a Room to a Valid Room
    // Already containing a player
    // alid as in it is withn the number of tiles
    // of the dice from any one of the original rooms
    // door tiles
    // Should result in the player moving to the new
    // room
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();
        Location oldLocation = (Location) board.getTile(7, 8); // 8,9
        Location newLocation = controller.getRoom("Ball Room");
        Player p = setupMockPlayer(controller, oldLocation, 1);
        Player p2 = setupMockPlayer(controller, newLocation, 2);
        Player p3 = setupMockPlayer(controller, newLocation, 3);
        controller.setCurrentPlayer(p);

        // Action
        controller.movePlayer(p, newLocation, 6);

        // Checks
        assertTrue(p.getLocation().equals(newLocation)); 		  // Player location must be the new Location
        assertTrue(((Room) newLocation).getPlayers().contains(p));// The new Locations "players" list must contain the correct player
        assertTrue(((Room) newLocation).getPlayers().size() == 3);// There should now be three players in the Room
        checkRooms(controller, (Room) newLocation);					  // The player shouldn't be on any other tile
        checkTiles(board, null);								  // The player shouldn't be in any other room
    }


    // TestHelperMethods //

    /**
     * Adds a new player to the game at a specific location with a
     * particular player number
     *
     * @param game
     * @param location - player location
     * @param playerNum
     *
     * @return - return the player
     */
    public Player setupMockPlayer(Controller game, Location location, int playerNum) {

        // Create player
        game.addPlayer(playerNum, Player.Character.COLONEL_MUSTARD, "");
        Player player = game.getPlayer(playerNum);

        // set the players location
        player.setLocation(location);

        // set the locations player field
        if (location instanceof Room)
            ((Room) location).addPlayer(player);
        else
            ((Tile) location).setPlayer(player);
        return player;
    }

    /**
     * Checks every tile on the bard to see if there is any players on it
     * Assert will fail if there is a player on a tile that is not passed
     * in to the locations parameter
     *
     * @param board
     * @param locations - the locations where players CAN be
     */
    public void checkTiles(Board board, Tile... locations) {

        // Iterate through board
        for (int x = 0; x < 24; x++) {
            for (int y = 0; y < 25; y++) {
                if (board.getTile(x, y) instanceof Tile) { // make sure current tile is a tile(not null)
                    if (locations == null){ // means we can check every tile
                        assertTrue(((Tile) board.getTile(x, y)).getPlayer() == null);
                    }else{// means we must look through locations tiles
                        boolean inLocations = false;
                        for(int i = 0; i < locations.length; i++){
                            if(board.getTile(x, y).equals(locations[i])){
                                inLocations = true;
                            }
                        }
                        if(inLocations == false) // cant possibly be in "locations" so can check it
                            assertTrue(((Tile) board.getTile(x, y)).getPlayer() == null); // check if it is holding a player
                    }
                }
            }
        }
    }

    /**
     * Checks every room in the game to see if there is any players in it
     * Assert will fail if there is a player in a room that is not passed
     * in to the location parameter
     *
     * @param game
     * @param location - location not to check
     */
    public void checkRooms(Controller game, Room location) {

        // iterate through all the rooms
        for (Room r : game.getRooms()) {
            if (location == null) // can check all the rooms
                assertTrue(r.getPlayers().size() == 0);
            else if (!r.equals(location)) // can check if its not the location
                assertTrue(r.getPlayers().size() == 0);
        }
    }

    /**
     * print all the rooms and the players inside them
     *
     * @param game
     */
    public void printRooms(Controller game) {
        System.out.println("////////    Rooms    ////////");
        System.out.println();
        for (Room r : game.getRooms()) { // iterate through all the rooms
            System.out.println(r.getName());
            for (Player p : r.getPlayers()) { // print all the players in the room
                System.out.println("  Player: " + p.getCharacter());
            }
            System.out.println();
            for (Weapon w : r.getWeapons()) { // print all the weapons in the room
                System.out.println("  Weapon: " + w.getName());
            }
            System.out.println();
        }
        System.out.println();
    }

}

