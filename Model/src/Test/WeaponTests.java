package Test;

import static org.junit.Assert.*;

import Controller.Controller;
import Model.Board;
import Model.Locations.*;
import Model.GameObjects.*;
import org.junit.Test;
import org.junit.Assert;



/**
 * Class for all the weapon tests
 */
public class WeaponTests {

    @Test
    public void validWeaponRoomToRoom()
    // Moves a weapon from between rooms
    {
        // Setup

        Controller game = new Controller();
        Board board = game.getBoard();
        Weapon w = game.getWeapon("Candlestick");
        Room newRoom = game.getRoom("Study");

        // Action
        w.move(newRoom);

        // Checks
        assertTrue(w.getRoom().equals(newRoom.getName()));// The weapons room field should equal the correct room
        assertTrue(newRoom.getWeapons().contains(w)); 	  // the room the weapon is in should be correct
        checkRooms(game, newRoom, w); 					  // the weapon should not be in any other room
    }



    // Test Helper Methods

    /**
     * Checks every room in the game to see if there a particular weapon is in it
     * Assert will fail if there that weapon is in a room that is not passed
     * in to the location parameter
     *
     * @param game
     * @param location - location not to check
     * @param weapon - the weapon to look for
     */
    public void checkRooms(Controller game, Room location, Weapon weapon) {

        // iterate through all the rooms
        for (Room r : game.getRooms()) {// can check all the rooms
            if (location == null)
                assert (!r.getWeapons().contains(weapon));
            else if (!r.equals(location)) {}// can check if its not the location
                //assertTrue(!r.getWeapons().contains(weapon));
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

