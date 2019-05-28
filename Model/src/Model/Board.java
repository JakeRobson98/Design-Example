package Model;

import Model.Locations.DoorTile;
import Model.Locations.StartingTile;
import Model.Locations.Tile;

import Model.GameObjects.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Board {

    private Tile[][] board2D;



    /**
     * Constructs a new Board Object
     *
     * @param filename - file location for board.txt
     */
    public Board(String filename){
        board2D = new Tile[24][25];
        loadBoard(filename);

    }

    /**
     * Loads a new Board by reading the file and assigning the tiles to their
     * corresponding 2D array coordinate
     *
     * @param filename - the location of the file to read
     */
    public void loadBoard(String filename){
        int x = 0;
        int y = 0;

        try {

            // Scan and read file
            //final String folder = System.getProperty("user.dir");
            String workingDirectory = System.getProperty("user.dir");
            String path = workingDirectory + "/board.txt";
            Scanner scan = new Scanner(new File(path));
            while (scan.hasNext()) {
                String line = scan.nextLine();

                Scanner sc = new Scanner(line);
                sc.useDelimiter(","); // To distinguish between tiles in the file
                while(sc.hasNext()){
                    String read = sc.next();
                    Tile tile = null;
                    switch(read){

                        // Represents a particular Room Doorway
                        case "K": tile = new DoorTile(x,y, "Kitchen"); break;
                        case "D": tile = new DoorTile(x,y, "Dining Room"); break;
                        case "L": tile = new DoorTile(x,y, "Lounge"); break;
                        case "H": tile = new DoorTile(x,y, "Hall"); break;
                        case "s": tile = new DoorTile(x,y, "Study"); break;
                        case "l": tile = new DoorTile(x,y, "Library"); break;
                        case "B": tile = new DoorTile(x,y, "Billiard Room"); break;
                        case "C": tile = new DoorTile(x,y, "Conservatory"); break;
                        case "b": tile = new DoorTile(x,y, "Ball Room"); break;

                        // Represents a characters starting tile
                        case "0": tile = new StartingTile(x,y, Player.Character.MISS_SCARLETT); break;
                        case "1": tile = new StartingTile(x,y, Player.Character.COLONEL_MUSTARD); break;
                        case "2": tile = new StartingTile(x,y, Player.Character.MRS_WHITE); break;
                        case "3": tile = new StartingTile(x,y, Player.Character.THE_REVERAND_GREEN); break;
                        case "4": tile = new StartingTile(x,y, Player.Character.MRS_PEACOCK); break;
                        case "5": tile = new StartingTile(x,y, Player.Character.PROFESSOR_PLUM); break;

                        // Represents a Characters Position on the board
                        case "S": tile = new StartingTile(x,y, Player.Character.MISS_SCARLETT);break;
                        case "M": tile = new StartingTile(x,y, Player.Character.COLONEL_MUSTARD);break;
                        case "W": tile = new StartingTile(x,y, Player.Character.MRS_WHITE); break;
                        case "G": tile = new StartingTile(x,y, Player.Character.THE_REVERAND_GREEN);break;
                        case "P": tile = new StartingTile(x,y, Player.Character.MRS_PEACOCK);;break;
                        case "p": tile = new StartingTile(x,y, Player.Character.PROFESSOR_PLUM);break;

                        // Represents a null tile
                        case "n": tile = null; break;

                        // Represents an ordinary Tile
                        case "t": tile = new Tile(x,y); break;
                    }
                    board2D[x][y] = tile; // assign tile to corresponding board coordinate
                    x++;
                }
                y++;
                x = 0;
                //sc.close();
            }
            scan.close();
        } catch(IOException e){
            e.getMessage();
        }
    }




    // Getters and Setters //

    public Tile getTile(int x, int y){
        if(x<0 || x>23 || y<0 || y>24)
            return null;
        return board2D[x][y];
    }

    public void setTile(int x, int y, Tile tile){
        board2D[x][y] = tile;
    }

//    public int getWidth(){
//        return WIDTH;
//    }
//
//    public int getHeight(){
//        return HEIGHT;
//    }
}
