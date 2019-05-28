package View.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * The menu bar for the game
 *
 */
public class MenuBar extends JMenuBar implements ActionListener{

	private CluedoFrame frame;
	
	// the menu bar object
	private JMenuBar menuBar;
   
	// the menu within the menubar "Cluedo"
	private JMenu menu;
	
	// the menu bar items
    private JMenuItem menuItem1; // "New Game"
    private JMenuItem menuItem2; // "Quit Game"
    
	/**
	 * Constructs a new MenuBar "Class"
	 * 
	 * @param frame
	 */
	public MenuBar(CluedoFrame frame){
		this.frame = frame;
	 
	}
	
	/**
	 * Creates and returns a new Cluedo game JMenuBar
	 * 
	 * @return - JMenuBar
	 */
	public JMenuBar createMenuBar() {
       
        //Create the menu bar.
        menuBar = new JMenuBar();
 
        //Build the menu.
        menu = new JMenu("Cluedo");
        menuBar.add(menu);
 
        //Create and setup the JMenuItems
        menuItem1 = new JMenuItem("New Game");
        menuItem2 = new JMenuItem("Quit Game");
        menuItem1.addActionListener(this);
        menuItem2.addActionListener(this);
        menu.add(menuItem1);
        menu.add(menuItem2);
        
        menuBar.setOpaque(false);

        return menuBar;
    }
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// player has selected "New Game"
		if(e.getSource().equals(menuItem1)){
			int n = JOptionPane.showConfirmDialog(frame, // dialog confirming a new game
					"Are you sure you want to start a new game?\n"
					+ "All progress will be lost", "Are you sure?"
					,JOptionPane.YES_NO_OPTION);
			if(n == 0) // if yes then create a new game
				frame.newCluedoGame();
		}
		
		//Player has selected"Quit Game"
		else if(e.getSource().equals(menuItem2)){
			int n = JOptionPane.showConfirmDialog(frame, // dialog confirming to quit the game
					"Are you sure you want to quit?\n"
					+ "All progress will be lost", "Are you sure?"
					,JOptionPane.YES_NO_OPTION);
			if(n == 0) // if yes then quit the game
				System.exit(0);
		}
	}
 
    
}
