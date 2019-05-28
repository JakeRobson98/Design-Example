package Test;
import Model.Board;
import Model.GameObjects.Player;
import Controller.Controller;
import Model.Locations.*;
import Model.GameObjects.*;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Tests for all the Cards
 */
public class CardTest{

    @Test
    public void dealPlayerCards()
    // Deal all the cards to the players, ansures the correct
    // number of cards are distributed to the players, and that
    // the correct amount are left over
    {
        // Setup
        Controller controller = new Controller();

        Board board = controller.getBoard();
        Player p1 = setupMockPlayer(controller, board.getTile(7, 8), 1);
        Player p2 = setupMockPlayer(controller, board.getTile(17, 7), 2);
        Player p3 = setupMockPlayer(controller, board.getTile(6, 17), 3);
        Player p4 = setupMockPlayer(controller, board.getTile(15, 15), 4);
        Player p5 = setupMockPlayer(controller, board.getTile(21, 19), 5);
        controller.setNumPlayers(5);
        controller.setupPlayers();
        // Precondition
        assertTrue(controller.getCards().size() == 21);

        // Action
        controller.dealCards();

        // Checks
        for (Player p : controller.getPlayers()) { // all players should have 3 ards each
            assertTrue(p.getHand().size() == 3);
        }
        assertTrue(controller.getCards().size() == 21); // all cards should be in "Cards"
        System.out.println("Size " + controller.getCardsLeft().size());
        assertEquals(controller.getCardsLeft().size(), 4); // should be 4 cards left over in the deck
    }

    @Test
    public void dealSoultionCards()
    // Makes sure the soultion is placed
    // in the basement
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();
        Player p1 = setupMockPlayer(controller, board.getTile(7, 8), 1);
        Player p2 = setupMockPlayer(controller, board.getTile(17, 7), 2);
        Player p3 = setupMockPlayer(controller, board.getTile(6, 17), 3);
        Player p4 = setupMockPlayer(controller, board.getTile(15, 15), 4);
        Player p5 = setupMockPlayer(controller, board.getTile(21, 19), 5);
        Basement basement = controller.getBasement();
        controller.setNumPlayers(5);

        // Action
        controller.dealCards();

        // Checks
        assertTrue(basement.getMurderCharacter() != null); // check that the solution is in the basement
        assertTrue(basement.getMurderRoom() != null);
        assertTrue(basement.getMurderWeapon() != null);
    }

    @Test
    public void solutionSeperateFromHands()
    {
        // Setup
        Controller controller = new Controller();
        Board board = controller.getBoard();

        Player p1 = setupMockPlayer(controller, board.getTile(7, 8), 1);
        Player p2 = setupMockPlayer(controller, board.getTile(17, 7), 2);
        Player p3 = setupMockPlayer(controller, board.getTile(6, 17), 3);
        Player p4 = setupMockPlayer(controller, board.getTile(15, 15), 4);
        Player p5 = setupMockPlayer(controller, board.getTile(21, 19), 5);
        Basement basement = controller.getBasement();
        controller.setNumPlayers(5);

        // Action
        controller.dealCards();

        // Checks
        for (Player p : controller.getPlayers()) { // checks every players hands for the solution cards
            if (p.getHand().contains(basement.getMurderCharacter()) || p.getHand().contains(basement.getMurderRoom())
                    || p.getHand().contains(basement.getMurderWeapon())) {
                assertFalse(true);
            }

        }

    }

    // Test Helper Methods //


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
    public Player setupMockPlayer(Controller controller, Location location, int playerNum) {

        // Create player
        controller.addPlayer(playerNum, Player.Character.COLONEL_MUSTARD, "");
        Player player = controller.getPlayer(playerNum);

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
     * @param //   game
     * @param location - location not to check
     */
    public void checkRooms(Controller controller, Room location) {

        // iterate through all the rooms
        for (Room r : controller.getRooms()) {
            if (location == null) // can check all the rooms
                assertTrue(r.getPlayers().size() == 0);
            else if (!r.equals(location)) // can check if its not the location
                assertTrue(r.getPlayers().size() == 0);
        }
    }

}
