package Test;

import static org.junit.Assert.*;

import Controller.Controller;
import Model.Board;
import Model.GameObjects.Basement;
import Model.GameObjects.Player;
import Model.GameObjects.Weapon;
import Model.Locations.Location;
import Model.Locations.Room;
import Model.Locations.Tile;
import org.junit.Test;


/**
 * Class for all the Accusing and Suggestion tests
 */
public class AccusingAndSuggestingTests {

    @Test
    public void validAccusation()
    // checks that a valid accusation returns true
    {
        //Setup
        Controller game = new Controller();

        Board board = game.getBoard();
        Player p1 = setupMockPlayer(game, board.getTile(7, 8), 1);
        Player p2 = setupMockPlayer(game, board.getTile(17, 7), 2);
        game.setNumPlayers(2);
        Basement basement = game.getBasement();
        dealCards(game, p1, p2);
        game.setCurrentPlayer(p1);


        // Action / Check
        assertTrue( // make a correct accusation
                game.accusation(basement.getMurderCharacter(), basement.getMurderWeapon(), basement.getMurderRoom()));
    }

    @Test
    public void invalidAccusation()
    // checks that an invalid accusation returns false,
    // and removes the player from the game
    {
        Controller game = new Controller();

        Board board = game.getBoard();
        Player p1 = setupMockPlayer(game, board.getTile(7, 8), 1);
        Player p2 = setupMockPlayer(game, board.getTile(17, 7), 2);
        game.setNumPlayers(2);
        Basement basement = game.getBasement();
        dealCards(game, p1, p2);
        game.setCurrentPlayer(p1);



        assertFalse(
                game.accusation(basement.getMurderCharacter(),game.getWeapon("Revolver"),basement.getMurderRoom()));


        // Checks
        assertTrue(game.getPlayersOut().contains(p1)); // should contain the player who made the accusation
        assertTrue(game.getPlayersOut().size() == 1); // all players should be in "players"
    }



    @Test
    public void validRefutedSuggestion()
    // Valid suggestion that is refuted,
    // Valid in that it is done within a room
    // Should be refuted
    {
        //Setup
        Controller game = new Controller();

        Board board = game.getBoard();
        Room location = game.getRoom("Lounge");
        Player p1 = setupMockPlayer(game, location, 1,Player.Character.MISS_SCARLETT);
        Player p2 = setupMockPlayer(game, board.getTile(17, 7), 2, Player.Character.COLONEL_MUSTARD);
        game.setNumPlayers(2);
        Basement basement = game.getBasement();
        dealCards(game, p1, p2);
        game.setCurrentPlayer(p1);


        // ensures candlestick must be moved into the correct room
        Weapon weapon = game.getWeapon("Candlestick");
        Room oldRoom = game.getRoom(weapon.getRoom());
        oldRoom.removeWeapon(weapon);
        game.getRoom("Study").addWeapon(weapon);

        // Check / Action
        assertFalse(game.suggestion(Player.Character.COLONEL_MUSTARD, weapon, location).equals(""));

        // Checks
        assertTrue(game.getRoom(weapon.getRoom()).equals(location)); // weapon should have moved into the room
    }

    @Test
    public void validUnrefutedSuggestion()
    // Valid suggestion that is unrefuted,
    // Valid in that it is done within a room
    // Should be unrefuted
    {
        // Setup
        Controller game = new Controller();

        Board board = game.getBoard();
        Location location = game.getRoom("Hall");
        Player p1 = setupMockPlayer(game, location, 1,Player.Character.MRS_PEACOCK);
        Player p2 = setupMockPlayer(game, board.getTile(17, 7), 2, Player.Character.MISS_SCARLETT);
        game.setNumPlayers(2);
        Basement basement = game.getBasement();
        dealCards(game, p1, p2);
        game.setCurrentPlayer(p1);

        // Check / Action

        // Checks
        assertTrue(game.getRoom(basement.getMurderWeapon().getRoom()).equals(game.getRoom(basement.getMurderWeapon().getRoom()))); // weapon should have moved into the room
    }

    @Test
    public void invalidSuggestion()
    // Invalid suggestion
    // Invalid in that it is not done within a room
    {
        Controller game = new Controller();

        Board board = game.getBoard();
        Player p1 = setupMockPlayer(game, board.getTile(7, 8), 1);
        Player p2 = setupMockPlayer(game, board.getTile(17, 7), 2);
        game.setNumPlayers(2);
        Basement basement = game.getBasement();
        dealCards(game, p1, p2);
        game.setCurrentPlayer(p1);


        // Check / Action
        assertTrue(game.suggestion(basement.getMurderCharacter(), basement.getMurderWeapon(), basement.getMurderRoom()) == null);
        // Should be null because suggestion is invalid
    }

    @Test
    public void invalidSuggestion2()
    // Invalid suggestion
    // Invalid in that the room suggested is not
    // the room the player is in
    {
        // Setup
        Controller game = new Controller();

        Board board = game.getBoard();
        Room location = game.getRoom("Lounge");
        Player p1 = setupMockPlayer(game, location, 1);
        Player p2 = setupMockPlayer(game, board.getTile(17, 7), 2);
        game.setNumPlayers(2);
        Basement basement = game.getBasement();
        dealCards(game, p1,p2);
        game.setCurrentPlayer(p1);

        // Check / Action
        assertTrue(game.suggestion(basement.getMurderCharacter(), basement.getMurderWeapon(), game.getRoom("Hall")) == null);
        // Should be null because suggestion is invalid
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
     * Adds a new player to the game at a specific location with a
     * particular player number
     *
     * @param game
     * @param location - player location
     * @param playerNum
     *
     * @return - return the player
     */
    public Player setupMockPlayer(Controller game, Location location, int playerNum, Player.Character character) {

        // Create player
        game.addPlayer(playerNum, character, "");
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

    public void dealCards(Controller game, Player player1, Player player2){
        Basement basement = game.getBasement();
        basement.setMurderCharacter(Player.Character.MISS_SCARLETT);
        basement.setMurderWeapon(game.getWeapon("Candlestick"));
        basement.setMurderRoom(game.getRoom("Hall"));
        player1.addCard(game.getCard(game.getRoom("Dining Room")));
        player1.addCard(game.getCard(game.getWeapon("Rope")));
        player1.addCard(game.getCard(Player.Character.MRS_PEACOCK));
        player1.addCard(game.getCard(game.getRoom("Hall")));
        player1.addCard(game.getCard(game.getWeapon("Candlestick")));
        player1.addCard(game.getCard(Player.Character.MISS_SCARLETT));

        player2.addCard(game.getCard(game.getRoom("Library")));
        player2.addCard(game.getCard(game.getWeapon("Dagger")));
        player2.addCard(game.getCard(Player.Character.COLONEL_MUSTARD));
        player2.addCard(game.getCard(game.getRoom("Conservatory")));
        player2.addCard(game.getCard(game.getWeapon("Lead Pipe")));
        player2.addCard(game.getCard(Player.Character.PROFESSOR_PLUM));



    }
}

