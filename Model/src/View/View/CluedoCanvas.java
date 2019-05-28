package View.View;
import Controller.Controller;
import Model.Locations.Tile;
import Model.Locations.Room;
import Model.GameObjects.*;
import Model.GameObjects.Cards.*;
import Model.Locations.Location;


import java.awt.*;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


/**
 * Represents the canvas for the cluedo 
 * game where the main elemnts are drawn
 *
 */
public class CluedoCanvas extends JPanel implements MouseListener, MouseMotionListener{

	// package location for all images
	private static final String IMAGE_PATH = "images/";
	
	// images the canvas requires
	private static final Image PAGE = loadImage("setupPageHome.png"); // setup menu image
	private static final Image BLACK_OPACITY = loadImage("blackOpacity.png"); // for dark background effect
	private static final Image table = loadImage("tablePattern.png"); // table pattern next to board

	//Fields for board
	private static final Image BOARD = loadImage("scaledGameBoard.png");
	public static final int WIDTH = BOARD.getWidth(null);
	public static final int HEIGHT = BOARD.getHeight(null) + 19;

	private Controller controller;
	
	// Mouse X and Y position
	private int mouseX;
	private int mouseY;
	
	// Component classes
	private AccusationOrSuggestion AOS;
	private Dice dice;
	private Card showCard;
	private BoardButtons boardButtons;
	private JPanel boardButtonsPanel;
		
	// Tiles to remember
	private List<Tile> moveableLocations;
	private Tile selectedTile;
	
	// Hides the currently displayed hand if true
	private boolean hideHand;
	
	public CluedoCanvas(Controller controller){

		this.controller = controller;
		// Setup Canvas JPanel
		setLayout(null);
		setLocation(0,0);
		addMouseListener(this);
		addMouseMotionListener(this);
		setVisible(true);
		setOpaque(false);
		
		// initilise components
		dice = new Dice(controller, this);
		moveableLocations = new ArrayList<>();
		boardButtons = new BoardButtons(controller, this);
		AOS = new AccusationOrSuggestion(controller, this);
		boardButtonsPanel = boardButtons.createPanel();
		
		// add to canvas panel
		add(boardButtonsPanel);
		
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(WIDTH, HEIGHT);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		}
	
	/**
	 * Creates a AccusationOrSuggestion object for a accusation 
	 * or a suggestin from the user
	 * 
	 * @param acc - true if user is making an accusatio
	 * 			  - false if the user is making a suggestion
	 */
	public void accusation(Boolean acc){
		
		remove(boardButtonsPanel);// remove current components so a accisation/suggestion can be made

		// change to appropriate page
		if(acc)
			AOS.setPage(1);
		else
			AOS.setPage(3);
		
		// create and setup AOS JPrames
		JPanel room = AOS.askMurderRoom(acc);
		JPanel weapon = AOS.askMurderWeapon();
		JPanel character = AOS.askMurderCharacter();
		JPanel done = AOS.done();
		
		
				
		// add JPanels to canvas
		add(room);
		add(weapon);
		add(character);
		add(done);
		
		revalidate();
		repaint();
	}
	
	
	
	
	
	
	@Override
	public void paint(Graphics g){
		
		// Game is currently in the setupMenu
		if(Controller.gameState == Controller.State.SETUP_MENU
				|| Controller.gameState == Controller.State.SETUP_PLAYER){
			g.drawImage(PAGE, 0, 0, null);
			
		// Game is currently running
		}else if(controller.gameState == Controller.State.RUNNING){
			// dont draw board hovers if on suggestion page
			if(showCard != null || AOS.getPage() > 0)
				paintBoard(g, controller.determineMoveLocations(controller.getCurrentPlayer(), dice.getRoll()),  null);
			else
				paintBoard(g, controller.determineMoveLocations(controller.getCurrentPlayer(), dice.getRoll()), new Point(mouseX, mouseY));
			
			for(Player p: controller.getPlayers())
				paintPlayer(g,p);	// Paint all the players on their tiles
			
			for(Player p: controller.getPlayersOut())    // Paint players who have been
				paintPlayer(g,p);						   // eliminated from the game on their tile
			
			paintHeading(g); // Paint title in topLeft
			dice.paint(g);	// paint the dice
			
			Player p = controller.getCurrentPlayer();

			// Paint hand without enlargement when appropriate
			if(showCard != null || AOS.getPage() > 0){
				paintHand(g, new Point(0,0), p);
			}else{
				paintHand(g, new Point(mouseX, mouseY),p);
			}
			
			// player has clicked on a card
			if(showCard != null){
				g.drawImage(BLACK_OPACITY, 0, 0, 990, 590, null);
				g.drawImage(showCard.getImage(0),315, 33, null);
			}
			
			// Paint the player and weapon tags
			if(showCard == null && AOS.getPage() == 0){

				for(Player player: controller.getPlayers()){
						Rectangle r = new Rectangle(player.getXPos(), player.getYPos(),19, 19);
						if(r.contains(mouseX, mouseY)){
							//player.paintTag(g);
							// If mouse is hovering over paint tag
							paintPlayerTag( player, g);

						}

				}
				for(Weapon weapon: controller.getWeapons()){
					Rectangle r = new Rectangle(weapon.getXPos(), weapon.getYPos(),19, 19);
					if(r.contains(mouseX, mouseY))
						weapon.paintTag(g); // If mouse is hovering over paint tag
				}
			}
			
			// Paint the AOS page
			if(AOS != null)
				AOS.Paint(g);
			
			// Hide hand if necessary
			if(hideHand)
				g.drawImage(table, 600, 250, null);
			
			// paint components
			super.paint(g);
		
		// Game is over, declare winner
		}else if(controller.gameState == Controller.State.OVER){
			gameOverScreen(g);
		}
		
	}
	
	/**
	 * Load an image from the file system, using a given filename.
	 * 
	 * @param filename
	 * @return
	 */
	public static Image loadImage(String filename) {
		// using the URL means the image loads when stored
		// in a jar or expanded into individual files.
		java.net.URL imageURL = CluedoCanvas.class.getResource(IMAGE_PATH
				+ filename);

		try {
			Image img = ImageIO.read(imageURL);
			return img;
		} catch (IOException e) {
			// we've encountered an error loading the image. There's not much we
			// can actually do at this point, except to abort the controller.
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}
	
	/**
	 * Screen to declare the winner once the game is over
	 * 
	 * @param g
	 */
	public void gameOverScreen(Graphics g){
		g.drawImage(PAGE, 0, 0, null);
		Graphics2D g2d = (Graphics2D) g;
		Basement b = controller.getBasement();
		Font font = new Font("Calibri", Font.PLAIN, 40);
		g2d.setFont(font);
		String s = "Congratulations, " + controller.getWinner().getName() + " you are the Winner!";
		Rectangle2D r = font.getStringBounds(s, g2d.getFontRenderContext());
		g2d.drawString(s, 495 - (int) r.getWidth()/2, 320); // center string on page
		font = new Font("Calibri", Font.PLAIN, 20);
		g2d.setFont(font);
		s = "Correct Solution Was " +controller.getCharacterName(b.getMurderCharacter()) + ", with the " + b.getMurderWeapon().getName() + ", in the " + b.getMurderRoom().getName();
		r = font.getStringBounds(s, g2d.getFontRenderContext());
		g2d.drawString(s, 495 - (int) r.getWidth()/2, 400); // center string on page
	}
	
	/**
	 * Paints the heading on the top left of the canvas,
	 * as well as their corresponding picture
	 * 
	 * @param g - canvas graphics object
	 */
	public void paintHeading(Graphics g){
		Player p = controller.getCurrentPlayer();
		g.setColor(Color.white);
		g.drawImage(p.getImage(), 590, 6,null);
		g.drawString(p.getName().toString(), 800, 20);
		g.drawString("It is your Turn", 800, 40);
	}
	public void paintPlayer(Graphics g, Player p){
            g.setColor(p.getColor());
            if (p.getLocation() instanceof Tile) {
                Tile t = (Tile) p.getLocation();
                g.fillOval(p.getXPos(), p.getYPos(), t.getSize() - 4, t.getSize() - 4);
                g.setColor(Color.black);
                g.drawOval(p.getXPos(), p.getYPos(), t.getSize() - 4, t.getSize() - 4);
            }
	}
	/**
	 * Paints the player tag
	 *
	 * @param g - graphics
	 */
	public void paintPlayerTag (Player p, Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		String s = Controller.getCharacterName(p.getCharacter());
		Font font = new Font("Calibri", Font.PLAIN, 15);
		g2d.setFont(font);
		Rectangle2D r = font.getStringBounds(s, g2d.getFontRenderContext());
		g.setColor(Color.lightGray);
		g.fillRect(p.getXPos() + 10, p.getYPos() - 20, (int) r.getWidth() + 10, (int) r.getHeight());
		g.setColor(Color.black);
		g.drawRect(p.getXPos() + 10, p.getYPos() - 20, (int) r.getWidth() + 10, (int) r.getHeight());
		g2d.drawString(s, p.getXPos() + 15, p.getYPos() - 6);
	}


	/**
	 * Paints the board on the canvas
	 *
	 * @param g - canvas graphics
	 * @param moveableLocations - tiles the player can move to
	 * @param p - mouse point
	 */
	public void paintBoard(Graphics g, List<Tile> moveableLocations, Point p){
		// board image
		g.drawImage(BOARD, 0, 0, BOARD.getWidth(null), BOARD.getHeight(null), null);

		// paint rooms
		paintRooms(g);
		// paint tiles
		List<Tile> tiles = controller.getTiles();
		for (Tile t : tiles ){
			if (t != null){
				paintTile(t, g, moveableLocations, p);
			}
		}
		for(Room r: controller.getRooms()){
			paintRoomObjects(g,r); // Paint all the players and weapons in the rooms
		}

	}
	/**
	 * Paints a door tile on the board
	 */
	public void paintTile(Tile t, Graphics g , List<Tile> moveableLocations, Point p ){


		Graphics2D g2D = (Graphics2D) g;
			// is currently a moveable location
			if(moveableLocations.contains(t)){

				g.setColor(Color.green);
				g.fillRect(t.getXPos(), t.getYPos(), t.getSize(), t.getSize());
				g.setColor(Color.black);
				g.drawRect(t.getXPos(), t.getYPos(), t.getSize(), t.getSize());

				// paint tile normally
			}else{
				g.setColor(new Color(224,232,185));
				g.fillRect(t.getXPos(), t.getYPos(), t.getSize(), t.getSize());
				g.setColor(Color.black);
				g.drawRect(t.getXPos(), t.getYPos(), t.getSize(), t.getSize());
			}

			Rectangle r = new Rectangle(t.getXPos(), t.getYPos(), t.getSize(), t.getSize());
			if(p != null){
				// tile is currently selected
				if(r.contains(p)){
					g2D.setStroke(new BasicStroke(2));
					g2D.setColor(Color.red);
					g2D.drawRect(t.getXPos()+2, t.getYPos()+2, t.getSize()-3, t.getSize()-3);
					g2D.setStroke(new BasicStroke(1));
				}
			}

	}
	public void paintHand (Graphics g, Point p, Player player){
		boolean selected = false;
		List<Card> hand = player.getHand();
		if(hand.isEmpty()){return;}
		for (int i = 0; i < 3; i++) {
			Rectangle r = new Rectangle(654 + i * 92, 254, 80, 116);
			Card c = hand.get(i);
			if (r.contains(p)) {
				selected = true;
				player.setSelectedCard(c);
				g.drawImage(c.getImage(1), 640 + i * 100, 230, null);
				g.setColor(Color.BLACK);
				g.drawRect(640 + i * 100, 230, 100, 146);
			} else {
				g.drawImage(c.getImage(2), 650 + i * 100, 254, null);
				g.setColor(Color.BLACK);
				g.drawRect(650 + i * 100, 254, 80, 116);
			}
		}
		int j = 0;
		for (int i = 3; i < hand.size(); i++) {
			Rectangle r = new Rectangle(654 + j * 92, 380, 80, 116);
			Card c = hand.get(i);
			if (r.contains(p)) {
				selected = true;
				player.setSelectedCard(c);
				g.drawImage(c.getImage(1), 640 + j * 100, 376, null);
				g.setColor(Color.black);
				g.drawRect(640 + j * 100, 376, 100, 146);
				j++;
			} else {
				g.drawImage(c.getImage(2), 650 + j * 100, 390, null);
				g.setColor(Color.black);
				g.drawRect(650 + j * 100, 390, 80, 116);
				j++;
			}

			if (!selected){
				player.setSelectedCard(null);
			}
		}
	}

    public void paintRoomObjects(Graphics g, Room r) {
        // for every room
        for(int i = 0; i < 6; i++){
		List<Player> playerTodraw = r.getPlayers();
		List<Weapon> weaponsToDraw = r.getWeapons();
            // paint every player in the room
            if(i < playerTodraw.size()){
                Player player = playerTodraw.get(i);
                player.setXPos(r.getX() + i*20 + 1);
                player.setYPos(r.getY() + 1);
                g.setColor(player.getColor());
                g.fillOval(r.getX() + i*20 + 1, r.getY() + 1, 18, 18);
                g.setColor(Color.BLACK);
                g.drawOval(r.getX() + i*20 + 1, r.getY() + 1, 18, 18);
            }

            // paint every weapon in the room
            if(i < weaponsToDraw.size()){
                Weapon weapon = weaponsToDraw.get(i);
                weapon.setXPos(r.getX() + i*20 + 1);
                weapon.setYPos(r.getY()+ 21);
                g.drawImage(weaponsToDraw.get(i).getImage(), r.getX() + i*20 + 1, r.getY() + 21, null);

            }
        }
    }
	/**
	 * Paints over all the rooms of the board image
	 *
	 * @param g - canvas graphics
	 */
	public void paintRooms(Graphics g){

		// Create polygons for each room
		int[] KitchenXS = {23,46,46,85,85,94,94,131,131,131,153,153,45,45,23,23,20,20,23,23,20,20,23};
		int[] KitchenYS = {31,31,28,28,31,31,28,28,31,53,53,159,159,138,138,118,118,92,92,81,81,54,54};
		int[] BallRoomXS = {211,211,256,256,274,275,291,291,301,301,319,319,336,336,382,382};
		int[] BallRoomYS = {182,59,59,26,21,19,17,19,19,17,19,21,26,59,59,182};
		int[] ConservatoryXS = {438,438,445,445,498,498,508,508,561,561,567,567,571,571,567,567,571,571,567,567,571,571,567,567,525,525,462,462};
		int[] ConservatoryYS = {118,30,30,27,27,30,30,27,27,30,30,38,38,55,55,66,66,79,79,89,89,106,106,114,114,138,138,118};
		int[] BillardRoomXS = {439,439,569,569,573,573,570,570,573,573,570,570,573,573,570,570,573,573,570,570};
		int[] BillardRoomYS = {299,192,192,196,196,214,214,226,226,240,240,250,250,266,266,276,276,293,293,299};
		int[] LibraryXS = {440,440,417,417,439,439,547,547,558,558,570,570,573,573,570,570,573,573,570,570,558,558,547,547};
		int[] LibraryYS = {436,414,414,354,354,331,331,354,354,351,351,354,354,378,378,388,388,413,413,416,416,413,413,436};
		int[] StudyXS = {440,440,417,417,550,550,570,573,573,570,570,573,573,570,570,546,546,522,522,511,511,493,493,482,482,458,458};
		int[] StudyYS = {577,555,555,492,492,514,514,514,527,527,537,537,551,551,577,577,580,580,577,577,580,580,577,577,580,580,577};
		int[] HallXS = {252,252,233,233,358,358,342,342};
		int[] HallYS = {583,570,570,423,423,570,570,583};
		int[] LoungeXS = {22,22,19,19,22,22,19,19,22,44,44,174,174,155,155,135,135,110,110,100,100,45,45};
		int[] LoungeYS = {578,554,554,517,517,506,506,468,468,468,446,446,554,554,578,578,580,580,578,578,580,580,578};
		int[] DiningRoomXS = {22,130,130,199,199,  22 ,22 ,18 ,18 ,22 ,22 ,18 ,18 ,22 ,22 ,18 ,18 ,22 ,22 ,18,18,22};
		int[] DiningRoomYS = {216,216,238,238,366, 366,363,363,332,332,322,322,296,296,286,286,260,260,250,250,220,220};

		g.setColor(new Color(224,232,185));

		// fill in the polygons for each room
		g.fillPolygon(KitchenXS, KitchenYS, KitchenXS.length);
		g.fillPolygon(BallRoomXS, BallRoomYS, BallRoomXS.length);
		g.fillPolygon(ConservatoryXS, ConservatoryYS, ConservatoryXS.length);
		g.fillPolygon(BillardRoomXS, BillardRoomYS, BillardRoomXS.length);
		g.fillPolygon(LibraryXS, LibraryYS, LibraryXS.length);
		g.fillPolygon(StudyXS, StudyYS, StudyXS.length);
		g.fillPolygon(HallXS, HallYS, HallXS.length);
		g.fillPolygon(LoungeXS, LoungeYS, LoungeXS.length);
		g.fillPolygon(DiningRoomXS, DiningRoomYS, DiningRoomXS.length);

		// draw the names for each room
		g.setColor(Color.black);
		g.drawString("Kitchen", 65, 45);
		g.drawString("Ball Room", 265, 45);
		g.drawString("Conservatory", 460, 45);
		g.drawString("Dining Room", 30, 235);
		g.drawString("Billard Room", 470, 210);
		g.drawString("Library", 470, 365);
		g.drawString("Study", 480, 505);
		g.drawString("Hall", 285, 460);
		g.drawString("Lounge", 65, 460);
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(controller.gameState != Controller.State.RUNNING){
			return;
		}
		
		
		// A card is currently displayed on screen
		if(showCard != null){
			Rectangle r = new Rectangle(315, 33, 360, 524);
			if(!r.contains(mouseX, mouseY)){ 
				showCard = null;		// if the player clicks outside the card
				add(boardButtonsPanel); // go back to the main screen
			}
		
		// Currently at the main game screen
		}else{
			Player player = controller.getCurrentPlayer();
	
			// if player clicks on the dice and its ready to roll
			if(dice.contains(new Point(getMouseX(), getMouseY())) 
					&& dice.getState() == Dice.state.TO_ROLL){
				int roll = dice.diceClicked();

				moveableLocations = controller.determineMoveLocations(player, roll);
					
			// check if a tile was clicked
			}else if(!moveableLocations.isEmpty()){
				for(Tile t: controller.getTiles()){
					if(t != null){
						if(t.contains(new Point(getMouseX(), getMouseY()))){ // tile must have been clicked
							//System.out.println(t.getXPos() + " "+  t.getYPos());
							if(moveableLocations.contains(t)){ 
								 //player.move(selectedTile);
								 controller.movePlayer(player, selectedTile, dice.getRoll());
								 moveableLocations.clear(); // empty until dice is rolled again
							}
						}
					}	
				}
			
			// check if a card was clicked
			}if(player.getSelectedCard() != null){ // if player was hovering over a card
				Card c = player.getSelectedCard(); // then make that the selected card
				showCard = c;
				remove(boardButtonsPanel);
			}
		}
		
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseReleased(MouseEvent e){}

	@Override
	public void mouseEntered(MouseEvent e){}

	@Override
	public void mouseExited(MouseEvent e){}

	@Override
	public void mouseDragged(MouseEvent e){}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(controller.getGameState() != Controller.State.RUNNING){
			return;
		}
		mouseX = e.getX();
		mouseY = e.getY();
		selectedTile = null;
		for(Tile t: controller.getTiles()){
			if(t != null)
				if(t.contains(new Point(mouseX, mouseY)))
					selectedTile = t; // if hovering over a tile remember it
		}
		repaint();
	}

	/**
	 * resets the dice back to 0
	 */
	public void resetDice(){
		dice.resetDice();
	}
	
	// Getters and Setters
	
	public int getMouseX(){
		return mouseX;
	}
	
	public int getMouseY(){
		return mouseY;
	}

	public Dice getDice(){
		return dice;
	}
	
	public List<Tile> getMoveableLocations(){
		return moveableLocations;
	}
	
	public void resetMoveableLocations(){
		moveableLocations.clear();
	}
	
	public Tile getSelectedTile(){
		return selectedTile;
	}
	
	public JPanel getBoardButtonPanel(){
		return boardButtonsPanel;
	}
	
	public BoardButtons getBoardButtons(){
		return boardButtons;
	}

	public Card getShowCard(){
		return showCard;
	}
	public AccusationOrSuggestion getAOS(){
		return AOS;
	}
	public void setHideHand(Boolean b){
		hideHand = b;
	}

}
