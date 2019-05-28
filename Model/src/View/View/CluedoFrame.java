package View.View;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Controller.Controller;

/**
 * 
 * Class to structure the GUI and create the window 
 *
 */
public class CluedoFrame extends JFrame{

	
	private CluedoCanvas canvas; // Canvas to draw game on
	private JPanel panelCont; // Panel to place all panels inside
	private SetupMenu setupMenu; // setup Menu to setup a game
	private String gameArgs;	// game arguments for a new instance of the game
	private MenuBar menuBar;
	private Controller controller;

	private static final Image BOARD = CluedoCanvas.loadImage("scaledGameBoard.png");
	public static final int WIDTH = BOARD.getWidth(null);

	
	/**
	 * Constructs a new CluedoFrame object and window
	 * 
	 * @param controller
	 * @param
	 * @param  - board.txt location for creating a new board
	 */
	public CluedoFrame(Controller controller){
		super("Cluedo Game");
		
		// initialise fields
		this.controller = controller;

		
		// setup JFrame
		setSize(getPreferredSize());
		setLocation(225,125);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true); 

		// create components
		setupMenu = new SetupMenu(controller, this);
		canvas = new CluedoCanvas(controller);
		panelCont = new JPanel();
		menuBar = new MenuBar(this);

		
		// add components
		panelCont.add(canvas);
		add(panelCont);
		
		// procced to Setup Menu
		setupMenuUI();
		
	}
	
	/**
	 * adds and displays the setupMenu 
	 */
	public void setupMenuUI(){
		panelCont.setLayout(new GridLayout(2,1)); // split container panel in half
		panelCont.add(setupMenu);					
		setJMenuBar(menuBar.createMenuBar());
		pack(); // pack together components
		repaint();
	}
	
	/**
	 * adds and displays the game board
	 */
	public void gameBoardUI(){
		panelCont.removeAll(); // clear the container panel
		panelCont.setLayout(new BorderLayout()); // change layout
		panelCont.add(canvas);
		pack(); // pack together components
		revalidate(); // revalidate the new JFrame
		
		// get game ready to start
		controller.setupPlayers();
		controller.dealCards();
		
		repaint();
	}
	
	
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(WIDTH,640);
	}
	
	
	@Override
	public void repaint(){
		super.repaint();
		canvas.repaint();
	}
	
	/**
	 * Creates a new Cluedo frame and CluedoGame
	 * Called when user makes a new game from the menubar
	 */
	public void newCluedoGame(){
		new Controller();
	}
	
	
	
	
	
	
}		