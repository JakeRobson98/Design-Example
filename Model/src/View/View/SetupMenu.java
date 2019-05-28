package View.View;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import Controller.Controller;
import Model.GameObjects.*;
import Model.Locations.Tile;

/**
 * Represents the Setup menu to help setup the game
 *
 */
public class SetupMenu extends javax.swing.JPanel implements ActionListener{
	
	// SetupMenu Components
	private JComboBox selectNumPlayers;
	private JButton nextButton;
	private JTextField nameField;
	private JRadioButton[] characterButtons = new JRadioButton[6];
	private static final Image BOARD = CluedoCanvas.loadImage("scaledGameBoard.png");
	public static final int WIDTH = BOARD.getWidth(null);

	private ArrayList<Integer> selectedCharacterIndexes; // indexes of characters that have been selected
	private int page; // the current page
	public boolean warning = false;
	private int playersLeft; // players left to pick a character
	
	private Controller controller;
	private CluedoFrame frame;
	
	/**
	 * Creates a new setup menu
	 * 
	 * @param controller
	 * @param frame
	 */
	public SetupMenu(Controller controller, CluedoFrame frame){
		super();
		this.controller = controller;
		this.frame = frame;
		selectedCharacterIndexes = new ArrayList<Integer>();
		homePage();
	}

	/**
	 * Creates a new home page for the setupMenu
	 */
	public void homePage(){
		setLayout(new GridBagLayout()); //create a Panel with a layout
		page = 0;
		
		// create and setup comboBox
		String[] playerOptions = { "3", "4", "5", "6"};
		selectNumPlayers = new JComboBox(playerOptions);
		selectNumPlayers.setSelectedIndex(0);
		selectNumPlayers.addActionListener(this);
		
		// create and setup button
		nextButton = new JButton("next");
		nextButton.addActionListener(this);
		nextButton.setActionCommand("doneHome");
		
		// add to setupMenu panel
		add(selectNumPlayers);
		add(nextButton);
	}
	
	/**
	 *  Creates a new player page for selecting a character for a player
	 */
	public void loadPlayerPage(){
		// remove all previous components
		removeAll();
		revalidate();
		repaint();
		
		page++;
		
		// create and setup buttons
		nextButton = new JButton("next");
		nameField = new JTextField(30);
		nextButton.addActionListener(this);
		
		// create some constraints for buttons
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 3;
		c.weighty = 1;
		
		// add to setupMenu
		add(nameField,c);
		addCharacterButtons();
		
		// modify constraints
		c.gridx = 1;
		c = new GridBagConstraints();
		c.gridx = 1;
		c.weighty = 1;
		
		// add to setupMenu
		add(nextButton,c);
		
		repaint();
	}
	
	/**
	 * Create the character radio buttons and add them to the canvas
	 */
	public void addCharacterButtons(){
		
		// create and setup the radio buttons
		for(int i = 0; i < 6; i++){
			JRadioButton rb = new JRadioButton(controller.getCharacterName(controller.getCharacters().get(i)));
			rb.setOpaque(false);
			rb.setActionCommand("" + i);
			rb.addActionListener(this);
			characterButtons[i] = rb; // add to the array
		}
		
		// create constraints and add to the panel (first row)
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 6;
		for(int i = 0; i < characterButtons.length/2; i++){
			add(characterButtons[i],c);
			if(selectedCharacterIndexes.contains(i)) // if character has been selected before
				characterButtons[i].setEnabled(false);
		}
		
		// create constraints and add to the panel (second row)
		c.gridy = 7;
		for(int i = characterButtons.length/2; i < characterButtons.length; i++){
			add(characterButtons[i],c);
			if(selectedCharacterIndexes.contains(i)) // if character has been selected before
				characterButtons[i].setEnabled(false);;
		}
		
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, 500);
		if(warning){ // player hasn't selected a character
			g.setColor(Color.red);
			g.setFont(new Font("Times New Roman",Font.PLAIN, 15));
			g.drawString("Must select a character!", 440, 200);
		}
		g.setColor(Color.BLACK);
		if(page == 0){ // text for home page
			g.setFont(new Font("Times New Roman",Font.PLAIN, 20));
			g.drawString("How Many Players", 415, 105);
		}else if(page > 0){ // text for player page
			g.setFont(new Font("Times New Roman",Font.PLAIN, 20));
			g.drawString("Player " + page, 475, 15);
			g.setFont(new Font("Times New Roman",Font.PLAIN, 15));
			g.drawString("What is your name?", 440, 40);
			g.drawString("Chose your character ", 440, 100);
		
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		warning = false;
		
		// player has pressed the 'next' button
		if(e.getSource() == nextButton){
			// if on the home page, setup the first player page
			if(e.getActionCommand().equals("doneHome")){
				nextButton1();		
			
			// if on a player page
			}else{
				boolean valid = false;
				// ensure player has selected a character
				for(int i = 0; i < characterButtons.length ; i++)
					if(characterButtons[i].isSelected())
						valid = true;
				if(valid) // if so proceed to next player page
					nextButton2();
				else // otherwise show warning
					warning = true;
			}
		}
		
		// if on a player page
		if(page > 0){
			for(int i = 0; i < 6; i++){
				if(e.getActionCommand().equals(characterButtons[i].getActionCommand())){
					for(int j = 0; j < 6; j++){
						if(!e.getActionCommand().equals(characterButtons[j].getActionCommand())){
							characterButtons[j].setSelected(false); // reset all the character buttons
						}											// apart from the selected one
					}
				}
			}
		}
		repaint();
	}
	
	/**
	 * Called when the 'next' button is pressed on the first page
	 */
	public void nextButton1(){
		// set the number of players in the game
		String selected = (String) selectNumPlayers.getSelectedItem();
		playersLeft = Integer.valueOf(selected);
		controller.setNumPlayers(playersLeft);
		
		controller.gameState = Controller.State.SETUP_PLAYER;
		
		loadPlayerPage();	// start to assign the players characters
	}
	
	/**
	 * Called when the 'next' button is pressed a player page
	 */
	public void nextButton2(){
		
		// get correponding name and character from textField and buttons
		String name = (String) nameField.getText();
		Player.Character character = null;
		for(int i = 0; i < characterButtons.length; i++){
			if(characterButtons[i].isSelected()){
				characterButtons[i].setEnabled(false);
				selectedCharacterIndexes.add(i);
				switch(i){
					case 0: character = Player.Character.MISS_SCARLETT; break;
					case 1: character = Player.Character.COLONEL_MUSTARD; break;
					case 2: character = Player.Character.MRS_WHITE; break;
					case 3: character = Player.Character.THE_REVERAND_GREEN; break;
					case 4: character = Player.Character.MRS_PEACOCK; break;
					case 5: character = Player.Character.PROFESSOR_PLUM; break;
				}
			}	
		}
		
		// set players name to the character name if they leave it blank
		if(name.equals(""))
			name = controller.getCharacterName(character);
			
		controller.addPlayer(page, character, name); // add player to game
		playersLeft--; // reduce the number of player left to assign by 1
	
		// if all player have been assigned a character
		if(playersLeft == 0){
			controller.gameState = Controller.State.RUNNING;
			removeAll();
			revalidate();
			repaint();
			frame.gameBoardUI();
		
		// otherwise continue assigning character
		}else{
			loadPlayerPage();
		}
	}

}