import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LifeControl extends JPanel implements ActionListener, SpotListener {
	
	/* Enum to identify player. */
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
	public boolean auto = false;
	private long time;
		
	LifeControl(long time, int height, int width, int surviveMin, int surviveMax, int birthMin, int birthMax) {
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
		
		RunnableManager manager = new RunnableManager(this, time);
		
		JPanel message_panel = new JPanel();
		message_panel.setLayout(new BorderLayout());
		message_panel.add(_message, BorderLayout.CENTER);
		JButton torus_button = new JButton("Torus Mode");
		torus_button.addActionListener(this);
		torus_button.setActionCommand("torus");
		
		message_panel.add(torus_button, BorderLayout.EAST);
		JButton auto_button = new JButton("Auto Mode");
		
		auto_button.addActionListener(manager);
		auto_button.setActionCommand("auto");
		message_panel.add(auto_button, BorderLayout.WEST);
		
		JPanel button_panel = new JPanel();
		button_panel.setLayout(new BorderLayout());

		/* Reset button. Add ourselves as the action listener. */
		
		JButton reset_button = new JButton("Restart");
		reset_button.addActionListener(this);
		reset_button.addActionListener(manager);
		reset_button.setActionCommand("reset");
		button_panel.add(reset_button, BorderLayout.EAST);
		
		JButton random_button = new JButton("Random");
		random_button.addActionListener(this);
		random_button.setActionCommand("random");
		button_panel.add(random_button, BorderLayout.CENTER);
		
		JButton start_button = new JButton("Advance");
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
	
	public Spot smartFind(int x, int y, Spot s) {
		if (torusMode) {
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
		} else {
			if (x > width - 1) {
				return s;
			}
			if (x < 0) {
				return s;
			}
			if (y > height - 1) {
				return s;
			}
			if (y < 0) {
				return s;
			}
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
			for (int i=-1; i<=1; i++) {
				for (int j=-1; j<=1; j++) {
					Spot spotReturned;
					spotReturned = smartFind(s.getSpotX() + i, s.getSpotY() + j, s);
					if (spotReturned != s && spotReturned.isSpotAlive()) {
						totalAlive++;
					}
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
				}
			} else {
				if (s.getSpotAliveNeighbors() >= birthMin && s.getSpotAliveNeighbors() <= birthMax) {
					s.setSpotAlive(true);
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
	
	public void autoToggle() {
		if (auto == false) {
			auto = true;
		} else {
			auto = false;
		}
		updateMessage();
	}
	
	public void torusToggle() {
		if (torusMode == false) {
			torusMode = true;
		} else {
			torusMode = false;
		}
		updateMessage();
	}
	
	private void updateMessage() {
		if (torusMode && auto) {
			_message.setText("Click to set up. Torus Mode is on and Auto Mode is on.");
		} else if (torusMode && !auto) {
			_message.setText("Click to set up. Torus Mode is on and Auto Mode is off.");
		} else if (!torusMode && auto) {
			_message.setText("Click to set up. Torus Mode is off and Auto Mode is on.");
		} else {
			_message.setText("Click to set up. Torus Mode is off and Auto Mode is off.");
		}
	}
	
	public void specialMessage() {
		for (Spot s : _board) {
			if (s.getSpotY() == 1 && (s.getSpotX() == 2 || s.getSpotX() == 3 || s.getSpotX() == 7 || s.getSpotX() == 8 || s.getSpotX() == 11 || s.getSpotX() == 15 || s.getSpotX() == 17 || s.getSpotX() == 18 || s.getSpotX() == 19 || s.getSpotX() == 24 || s.getSpotX() == 26 || s.getSpotX() == 29 || s.getSpotX() == 30 || s.getSpotX() == 33)) {
				s.setSpotColor(Color.BLACK);
				s.setBackground(Color.BLACK);
			}
			if (s.getSpotY() == 2 && (s.getSpotX() == 1 || s.getSpotX() == 4 || s.getSpotX() == 6 || s.getSpotX() == 9 || s.getSpotX() == 11 || s.getSpotX() == 11 || s.getSpotX() == 12 || s.getSpotX() == 14 || s.getSpotX() == 15 || s.getSpotX() == 17 || s.getSpotX() == 20 || s.getSpotX() == 24 || s.getSpotX() == 26 || s.getSpotX() == 28 || s.getSpotX() == 31 || s.getSpotX() == 33)) {
				s.setSpotColor(Color.BLACK);
				s.setBackground(Color.BLACK);
			}
			if (s.getSpotY() == 3 && (s.getSpotX() == 1 || s.getSpotX() == 6 || s.getSpotX() == 9 || s.getSpotX() == 11 || s.getSpotX() == 13 || s.getSpotX() == 15 || s.getSpotX() == 17 || s.getSpotX() == 20 || s.getSpotX() == 24 || s.getSpotX() == 25 || s.getSpotX() == 26 || s.getSpotX() == 28 || s.getSpotX() == 31 || s.getSpotX() == 33)) {
				s.setSpotColor(Color.BLACK);
				s.setBackground(Color.BLACK);
			}
			if (s.getSpotY() == 4 && (s.getSpotX() == 1 || s.getSpotX() == 6 || s.getSpotX() == 9 || s.getSpotX() == 11 || s.getSpotX() == 15 || s.getSpotX() == 17 || s.getSpotX() == 18 || s.getSpotX() == 19 || s.getSpotX() == 26 || s.getSpotX() == 28 || s.getSpotX() == 31 || s.getSpotX() == 33)) {
				s.setSpotColor(Color.BLACK);
				s.setBackground(Color.BLACK);
			}
			if (s.getSpotY() == 5 && (s.getSpotX() == 1 || s.getSpotX() == 4 || s.getSpotX() == 6 || s.getSpotX() == 9 || s.getSpotX() == 11 || s.getSpotX() == 15 || s.getSpotX() == 17 || s.getSpotX() == 26 || s.getSpotX() == 28 || s.getSpotX() == 31 || s.getSpotX() == 33)) {
				s.setSpotColor(Color.BLACK);
				s.setBackground(Color.BLACK);
			}
			if (s.getSpotY() == 6 && (s.getSpotX() == 2 || s.getSpotX() == 3 || s.getSpotX() == 7 || s.getSpotX() == 8 || s.getSpotX() == 11 || s.getSpotX() == 15 || s.getSpotX() == 17 || s.getSpotX() == 26 || s.getSpotX() == 29 || s.getSpotX() == 30 || s.getSpotX() == 33)) {
				s.setSpotColor(Color.BLACK);
				s.setBackground(Color.BLACK);
			}
			if ((s.getSpotX() == 12 || s.getSpotX() == 22) && s.getSpotY() >= 11 && s.getSpotY() <= 14) {
				s.setSpotColor(Color.RED);
				s.setBackground(Color.RED);
			}
			if ((s.getSpotX() == 13 || s.getSpotX() == 21) && s.getSpotY() >= 10 && s.getSpotY() <= 15) {
				s.setSpotColor(Color.RED);
				s.setBackground(Color.RED);
			}
			if ((s.getSpotX() == 14 || s.getSpotX() == 20)  && s.getSpotY() >= 9 && s.getSpotY() <= 16) {
				s.setSpotColor(Color.RED);
				s.setBackground(Color.RED);
			}
			if ((s.getSpotX() == 15 || s.getSpotX() == 19)  && s.getSpotY() >= 9 && s.getSpotY() <= 17) {
				s.setSpotColor(Color.RED);
				s.setBackground(Color.RED);
			}
			if ((s.getSpotX() == 16 || s.getSpotX() == 18)  && s.getSpotY() >= 10 && s.getSpotY() <= 18) {
				s.setSpotColor(Color.RED);
				s.setBackground(Color.RED);
			}
			if (s.getSpotX() == 17 && s.getSpotY() >= 11 && s.getSpotY() <= 19) {
				s.setSpotColor(Color.RED);
				s.setBackground(Color.RED);
			}
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
			 advanceGame();
		 }
		/* Handles reset game button. Simply reset the game. */
	}
	void advanceGame() {
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
		updateMessage();
	}
}
