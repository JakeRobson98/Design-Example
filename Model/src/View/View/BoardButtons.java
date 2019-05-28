package View.View;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Controller.Controller;
import Model.Locations.*;
/**
 * Class to create the JButtons that are on the main board screen
 *
 */
public class BoardButtons implements ActionListener {

	private JButton[] buttons;
	private CluedoCanvas canvas;
	private Controller controller;
	private boolean stairwayUsed; // true if a stairway has been used this turn
	private boolean suggestionMade; // true if a suggestion has been made this turn
	
	/**
	 * Constructs a new BoardButtons object
	 * 
	 * @param controller
	 * @param canvas
	 */
	public BoardButtons(Controller controller, CluedoCanvas canvas){
		
		// initialise fields
		this.controller = controller;
		this.canvas = canvas;
		buttons = new JButton[4];

		// fill array with buttons
		buttons[0] = new JButton("Accusation");
		buttons[1] = new JButton("Suggestion");
		buttons[2] = new JButton("Stairway");
		buttons[3] = new JButton("Finish Turn");
	}
	
	/**
	 * Creates and returns a new JPanel holding all the four board buttons
	 * 
	 * @return JPanel
	 */
	public JPanel createPanel(){
		
		// create the panel, using a grid layout
		JPanel panel = new JPanel(new GridLayout(2,3,5,0));
	
		// iterate through the buttons
		for(int i = 0; i < buttons.length; i++){
			//setup button and add to panel
			buttons[i].setActionCommand(buttons[i].getText());
			buttons[i].addActionListener(this);
			buttons[i].setSelected(false);
			panel.add(buttons[i]);
		}
		//setup panel
		panel.setSize(380, 60);
		panel.setLocation(595, 525);
		panel.setOpaque(false);
		
		return panel;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String button = e.getActionCommand();
		
		// Accusation button has been pressed	
		if(button.equals("Accusation")){
			canvas.accusation(true);
		
		// Suggestion button has been pressed
		}else if(button.equals("Suggestion")){
			if(suggestionMade)// already made a suggestion this turn
				JOptionPane.showMessageDialog(canvas, "Can only make once suggestion per turn");
			else{
				if(controller.getCurrentPlayer().getLocation() instanceof Room) // make sure they're in a room
					canvas.accusation(false);
				else
					JOptionPane.showMessageDialog(canvas, "Must be in a room to make a suggestion");
			}
		
		// Stairway button has been pressed			
		}else if(button.equals("Stairway")){
			if(stairwayUsed) // already used the stairway this turn
				JOptionPane.showMessageDialog(canvas, "Can only use the stairway once per turn");
			else{
				Location loc = controller.getCurrentPlayer().getLocation();
				if(loc instanceof Room){	
					
					// current room does have a stairway
					if(!((Room) loc).getStairwayTo().equals("")){
						int n = JOptionPane.showConfirmDialog(canvas, // confirm dialog
								"Are you sure you want to use the stairway to the " + ((Room) loc).getStairwayTo() + "\n" 
								+ "This will count as your Move","Are you sure?",JOptionPane.YES_NO_OPTION);
						
						if(n == 0){ // have confirmed player wants to use the stairway, so use it
							controller.movePlayer(controller.getCurrentPlayer(), controller.getRoom(((Room) loc).getStairwayTo()), 0);
							stairwayUsed = true;
						}
					// players in a room without a stairway
					}else
						JOptionPane.showMessageDialog(canvas, "Must be in a room with a stairway to use a stairway");
				// players not in a room
				}else
					JOptionPane.showMessageDialog(canvas, "Must be in a room with a stairway to use a stairway");
				
			}
		
		// Finish Turn button has been pressed	
		}else if(button.equals("Finish Turn")){
			canvas.resetMoveableLocations();
			canvas.setHideHand(true); // hide hand until next player is ready
			canvas.repaint();
			JOptionPane.showMessageDialog(canvas,
					controller.getNextPlayer().getName() + ", your turn is next\n" + " Are you ready?");
			canvas.setHideHand(false); // unhide hand
			controller.setCurrentPlayer(controller.nextTurn());
			canvas.resetDice();
			stairwayUsed = false;
			suggestionMade = false;
		}
		canvas.repaint();
	}
	
	// Getters and Setters
	
	public JButton getButton(int i){
		return buttons[i];
	}
	
	
	public void setSuggestionMade(Boolean b){
		suggestionMade = b;
	}
	

}
