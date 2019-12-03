import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LifeControl extends JPanel implements ActionListener, SpotListener {
	
/* Enum to identify player. */
	
	private enum Player {BLACK, WHITE};
	private JSpotBoard _board;		/* SpotBoard playing area. */
	private JLabel _message;		/* Label for messages. */
	private boolean _game_won;		/* Indicates if games was been won already.*/
	private int height;
	private int width;
	private int surviveMin = 2;
	private int surviveMax = 3;
	private int birthMin = 3;
	private int birthMax = 3;
	private boolean torusMode = false;
		
	LifeControl(int height, int width, int surviveMin, int surviveMax, int birthMin, int birthMax) {
		/* Create SpotBoard and message label. */
		
		_board = new JSpotBoard(height,width);
		_message = new JLabel();
		this.height = height;
		this.width = width;
		this.surviveMin = surviveMin;
		this.birthMin = birthMin;
		this.surviveMax = surviveMax;
		this.birthMax = birthMax;
		
		/* Set layout and place SpotBoard at center. */
		
		setLayout(new BorderLayout());
		add(_board, BorderLayout.CENTER);

		/* Create subpanel for message area and reset button. */
		
		JPanel message_panel = new JPanel();
		message_panel.setLayout(new BorderLayout());
		message_panel.add(_message, BorderLayout.WEST);
		JButton torus_button = new JButton("Torus Mode");
		torus_button.addActionListener(this);
		torus_button.setActionCommand("torus");
		message_panel.add(torus_button, BorderLayout.EAST);
		
		JPanel button_panel = new JPanel();
		button_panel.setLayout(new BorderLayout());

		/* Reset button. Add ourselves as the action listener. */
		
		JButton reset_button = new JButton("Restart");
		reset_button.addActionListener(this);
		reset_button.setActionCommand("reset");
		button_panel.add(reset_button, BorderLayout.EAST);
		
		JButton random_button = new JButton("Random");
		random_button.addActionListener(this);
		random_button.setActionCommand("random");
		button_panel.add(random_button, BorderLayout.CENTER);
		
		JButton start_button = new JButton("Start");
		start_button.addActionListener(this);
		button_panel.add(start_button, BorderLayout.WEST);

		/* Add subpanel in south area of layout. */
		
		add(message_panel, BorderLayout.NORTH);
		add(button_panel, BorderLayout.SOUTH);

		/* Add ourselves as a spot listener for all of the
		 * spots on the spot board.
		 */
		_board.addSpotListener(this);

		/* Reset game. */
		resetGame();
	}
	
	public Spot smartFind(int x, int y) {
		if (x > width - 1) {
			x = x - (width);
		}
		if (x < 0) {
			x = x + (width);
		}
		if (y > height - 1) {
			y = y - (height);
		}
		if (y < 0) {
			y = y + (height);
		}
		return _board.getSpotAt(x, y);
	}
	
	public void determineSpotStatus() {
		for (Spot s : _board) {
			if (s.getSpotColor() == Color.BLACK) {
				s.setSpotAlive(true);
			} else {
				s.setSpotAlive(false);
			}
		}
		for (Spot s : _board) {
			int totalAlive = 0;
			boolean awayFromTop = false;
			boolean awayFromBottom = false;
			boolean awayFromLeft = false;
			boolean awayFromRight = false;
			if (s.getSpotY() > 0) {
				awayFromTop = true;
			} else {
				System.out.println(s.getCoordString() + "is too close to the top");
			}
			if (s.getSpotY() < (height - 1)) {
				awayFromBottom = true;
			} else {
				System.out.println(s.getCoordString() + "is too close to the bottom");
			}
			if (s.getSpotX() > 0) {
				awayFromLeft = true;
			} else {
				System.out.println(s.getCoordString() + "is too close to the left");
			}
			if (s.getSpotX() < (width - 1)) {
				awayFromRight = true;
			} else {
				System.out.println(s.getCoordString() + "is too close to the right");
			}
			if (awayFromTop) {
				if(_board.getSpotAt(s.getSpotX(), s.getSpotY() - 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from above");
				}
			} else if (torusMode) {
				if(smartFind(s.getSpotX(), s.getSpotY() - 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from above");
				}
			}
			if (awayFromBottom) {
				if(_board.getSpotAt(s.getSpotX(), s.getSpotY() + 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from below");
				}
			} else if (torusMode) {
				if(smartFind(s.getSpotX(), s.getSpotY() + 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from below");
				}
			}
			if (awayFromLeft) {
				if(_board.getSpotAt(s.getSpotX() - 1, s.getSpotY()).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from left");
				}
			} else if (torusMode) {
				if(smartFind(s.getSpotX() - 1, s.getSpotY()).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from left");
				}
			}
			if (awayFromRight) {
				if(_board.getSpotAt(s.getSpotX() + 1, s.getSpotY()).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from right");
				}
			} else if (torusMode) {
				System.out.println(s.getCoordString() + " triggered right torus mode");
				System.out.println(s.getCoordString() + " checked " + smartFind(s.getSpotX() + 1, s.getSpotY()).getCoordString());
				if(smartFind(s.getSpotX() + 1, s.getSpotY()).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from right");
				}
			}
			if (awayFromTop && awayFromLeft) {
				if(_board.getSpotAt(s.getSpotX() - 1, s.getSpotY() - 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from up left");
				}
			} else if (torusMode) {
				if(smartFind(s.getSpotX() - 1, s.getSpotY() - 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from up left");
				}
			}
			if (awayFromTop && awayFromRight) {
				if(_board.getSpotAt(s.getSpotX() + 1, s.getSpotY() - 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from up right");
				}
			} else if (torusMode) {
				if(smartFind(s.getSpotX() + 1, s.getSpotY() - 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from up right");
				}
			}
			if (awayFromBottom && awayFromLeft) {
				if(_board.getSpotAt(s.getSpotX() - 1, s.getSpotY() + 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from down left");
				}
			} else if (torusMode) {
				if(smartFind(s.getSpotX() - 1, s.getSpotY() + 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from down left");
				}
			}
			if (awayFromBottom && awayFromRight) {
				if(_board.getSpotAt(s.getSpotX() + 1, s.getSpotY() + 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from down right");
				}
			} else if (torusMode) {
				if(smartFind(s.getSpotX() + 1, s.getSpotY() + 1).isSpotAlive()) {
					totalAlive++;
					System.out.println(s.getCoordString() + "got a point from down right");
				}
			}
			s.setSpotAliveNeighbors(totalAlive);
		}
	}
	
	public void implimentSpotStatus() {
		for (Spot s : _board) {
			if (s.isSpotAlive()) {
				if(s.getSpotAliveNeighbors() < surviveMin || s.getSpotAliveNeighbors() > surviveMax) {
					s.setSpotAlive(false);
					System.out.println(s.getCoordString() + " was killed because it had " + s.getSpotAliveNeighbors());
				}
			} else {
				if (s.getSpotAliveNeighbors() >= birthMin && s.getSpotAliveNeighbors() <= birthMax) {
					s.setSpotAlive(true);
					System.out.println(s.getCoordString() + " was born because it had " + s.getSpotAliveNeighbors());
				}
			}
			if (s.isSpotAlive() == false) {
				s.setBackground(Color.WHITE);
				s.setSpotColor(Color.WHITE);
			} else {
				s.setBackground(Color.BLACK);
				s.setSpotColor(Color.BLACK);
			}
		}
	}
	
	public boolean isSameColor(Spot origional, int X, int Y) {
		for (Spot s : _board) {
			if (s.getSpotX() == X && s.getSpotY() == Y) {
				if(s.getSpotColor() == origional.getSpotColor()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void randomizeGame() {
		for (Spot s : _board) {
			int randomInt = (int) ((Math.random()*2));
			if (randomInt == 0) {
				s.setSpotAlive(true);
			} else {
				s.setSpotAlive(false);
			}
			if (s.isSpotAlive() == false) {
				s.setBackground(Color.WHITE);
				s.setSpotColor(Color.WHITE);
			} else {
				s.setBackground(Color.BLACK);
				s.setSpotColor(Color.BLACK);
			}
		}
	}
	
	@Override
	public void spotClicked(Spot spot) {
		if(spot.getSpotColor() == Color.WHITE) {
			spot.setSpotColor(Color.BLACK);	
			spot.setBackground(Color.BLACK);
		} else {
			spot.setSpotColor(Color.WHITE);	
			spot.setBackground(Color.WHITE);	
		}
	}

	@Override
	public void spotEntered(Spot spot) {
		/* Highlight spot if game still going on. */
		
		if (_game_won) {
			return;
		}
		spot.highlightSpot();
	}

	@Override
	public void spotExited(Spot spot) {
		/* Unhighlight spot. */
		
		if (_game_won) {
			return;
		}
		spot.unhighlightSpot();
		for (Spot s : _board) {
			if (s.getSpotX() -1 != spot.getSpotX()) {
				s.unhighlightSpot();
			}
		}
	}
	
	public void torusToggle() {
		if (torusMode == false) {
			torusMode = true;
		} else {
			torusMode = false;
		}
		if (torusMode == true) {
			_message.setText("Click to set up. Torus Mode is on.");
		} else {
			_message.setText("Click to set up. Torus Mode is off.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		 if ("reset".equals(e.getActionCommand())) {
			 resetGame();
		 } else if ("random".equals(e.getActionCommand())) {
			 randomizeGame();
		 } else if ("torus".equals(e.getActionCommand())) {
			 torusToggle();
		 } else {
			 startGame();
		 }
		/* Handles reset game button. Simply reset the game. */
	}
	private void startGame() {
		determineSpotStatus();
		implimentSpotStatus();
	}


	public void resetGame() {
		
		for (Spot s : _board) {
			s.clearSpot();
			s.unhighlightSpot();
			s.setSpotColor(Color.WHITE);
			s.setBackground(Color.WHITE);
			s.setSpotAlive(false);
		}
		if (torusMode == true) {
			_message.setText("Click to set up. Torus Mode is on.");
		} else {
			_message.setText("Click to set up. Torus Mode is off.");
		}
	}
}