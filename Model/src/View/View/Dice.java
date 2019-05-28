package View.View;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import Controller.*;


/**
 * Represents the dice for the game
 * 
 *
 */
public class Dice {

	// dice selected and unselected images
	private static final Image DICE = CluedoCanvas.loadImage("rollingDie.png").getScaledInstance(94, 94, 0);
	private static final Image SELECTED_DICE = CluedoCanvas.loadImage("selectedDie.png").getScaledInstance(94, 94, 0);
	
	// each Dice face image
	private static final Image FACE1 = CluedoCanvas.loadImage("diceface1.png").getScaledInstance(94, 94, 0);
	private static final Image FACE2 = CluedoCanvas.loadImage("diceface2.png").getScaledInstance(94, 94, 0);
	private static final Image FACE3 = CluedoCanvas.loadImage("diceface3.png").getScaledInstance(94, 94, 0);
	private static final Image FACE4 = CluedoCanvas.loadImage("diceface4.png").getScaledInstance(94, 94, 0);
	private static final Image FACE5 = CluedoCanvas.loadImage("diceface5.png").getScaledInstance(94, 94, 0);
	private static final Image FACE6 =  CluedoCanvas.loadImage("diceface6.png").getScaledInstance(94, 94, 0);

	public void setRoll(int roll) {
		this.roll = roll;
	}

	private int roll; // current dice roll
	
	// position
	private int x = 840;
	private int y = 60;
	
	// dice polygon to determine when its been clicked/hovered
	private int[] diceXS = {x+4,x+20,x+50,x+66,x+70,x+07,x+138,x+130,x+94,x+78,x+72,x+39};
	private int[] diceYS = {y+71,y+30,y+25,y+34,y+15,y+5,y+29,y+68,y+81,y+71,y+85,y+89};
	private Polygon dicePolygon = new Polygon(diceXS, diceYS, diceXS.length);
	
	private CluedoCanvas canvas;
	private Controller controller;
	private int currentRoll;
	
	private static state state; // current state of the dice
	
	// different states the dice can be in
	public static enum state{
		TO_ROLL,
		ROLLING,
		ROLLED
	}
	
	/**
	 * Creates a new dice object
	 * 
	 * @param controller
	 * @param canvas
	 */
	public Dice(Controller controller, CluedoCanvas canvas){
		this.canvas = canvas;
		this.controller = controller;
		this.state = Dice.state.TO_ROLL;
		this.roll = 0;
	}

	/**
	 * paints the dice with the given graphics object
	 * @param g - paint object
	 */
	public void paint(Graphics g){
		// Dice hasn't been rolled this turn
		if(state.equals(Dice.state.TO_ROLL)){
			
			// if mouse is hovering over the dice, draw the selected dice image
			if(dicePolygon.contains(new Point(canvas.getMouseX(), canvas.getMouseY())) 
					&& canvas.getShowCard() == null && canvas.getAOS().getPage() == 0){
					g.drawImage(SELECTED_DICE, x, y, null);
			
			// if mouse is hovering over the dice, draw the selected dice image
			}else
				g.drawImage(DICE, x, y, null);
		
		// Dice has been rolled or is rolling
		}else	
			g.drawImage(getDiceFace(roll), x, y, null);
	}
	
	/**
	 * Dice has been clicked on
	 * so roll the dice if neccessary
	 * 
	 * @return - the new dice roll
	 */
	public int diceClicked(){
		// Dice hasn't been rolled this turn
		if(state.equals(state.TO_ROLL)){
			// Roll the dice and change the roll and image accordingly
			roll = controller.rollDice();
			state = state.ROLLING;
			animateDice(); 
			state = state.ROLLED;

			return roll;
		}
		// Dice has already been rolled this turn
		return 0;
	}
	
	/**
	 * reset the dice back to 0
	 */
	public void resetDice(){
		state = state.TO_ROLL;
		roll = 0;
	}

	/**
	 * Flicks through random dice faces to 
	 * animate the dice roll
	 */
	public void animateDice(){
		int actualRoll = roll;
		
		// flick through 10 diceface images
		for(int i = 0; i < 10; i++){
			roll = controller.rollDice();
			paint(canvas.getGraphics());
			
			// wait 0.2 seconds between images
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		roll = actualRoll; // set roll to what was actually rolled
	}
	
	/**
	 * Get the corresponding dice face image for a particular dice value
	 * 
	 * @param i - dice value
	 * @return image - the image for that dice value
	 */
	public Image getDiceFace(int i){
		switch(i){
			case 1: return FACE1;
			case 2: return FACE2;
			case 3: return FACE3;
			case 4: return FACE4;
			case 5: return FACE5;
			case 6: return FACE6;
		}
		return null;
	}
	
	/**
	 * Checks if the point is contained within the dice polygon
	 * 
	 * @param p - the given point
	 * @return - true if point is within the polygon
	 */
	public boolean contains(Point p){
		return dicePolygon.contains(p);
	}
	
	// Getters and Setters
	public state getState(){
		return state;
	}

	public int getRoll(){
		return this.roll;
	}

}
