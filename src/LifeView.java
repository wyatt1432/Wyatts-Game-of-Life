import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LifeView {
	
	public static void main(String[] args) {
		/* Create top level window. */
		
		JFrame main_frame = new JFrame();
		main_frame.setTitle("Game of Life");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Create panel for content. Uses BorderLayout. */
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BorderLayout());
		main_frame.setContentPane(top_panel);

		/* Create ExampleWidget component and put into center
		 * of content panel.
		 */
		
		String heightString = JOptionPane.showInputDialog("What would you like your height and width to be?");
		int height = Integer.parseInt(heightString);
		if (height < 10) {
			height = 10;
		}
		if (height > 500) {
			height = 500;
		}
		int width = height;
		
		boolean done = false;
		int survivalMinimum = 2;
		int survivalMaximum = 3;
		int birthMinimum = 3;
		int birthMaximum = 3;
		long time = 500;
		while (done == false) {
			String defaultString = JOptionPane.showInputDialog("Would you like to use the default settings? (Type 'Yes' or 'No')");
			System.out.println(defaultString);
			if (defaultString.contentEquals("No")) {
				String survivalMinimumString = JOptionPane.showInputDialog("What would you like the survival minimum to be?");
				survivalMinimum = Integer.parseInt(survivalMinimumString);
				String survivalMaximumString = JOptionPane.showInputDialog("What would you like the survival maximum to be?");
				survivalMaximum = Integer.parseInt(survivalMaximumString);
				String birthMinimumString = JOptionPane.showInputDialog("What would you like the birth minimum to be?");
				birthMinimum = Integer.parseInt(birthMinimumString);
				String birthMaximumString = JOptionPane.showInputDialog("What would you like the birth maximum to be?");
				birthMaximum = Integer.parseInt(birthMaximumString);
				String timeString = JOptionPane.showInputDialog("What would you like the auto mode update time between frames in miliseconds to be?");
				time = Integer.parseInt(timeString);
				done = true;
			} else if (defaultString.contentEquals("Yes")) {
				done = true;
			}
		}
		
		if (time < 10) {
			time = 10;
		}
		if (time > 1000) {
			time = 1000;
		}
		
		LifeControl ttt = new LifeControl(time, height, width, survivalMinimum, survivalMaximum, birthMinimum, birthMaximum);
		top_panel.add(ttt, BorderLayout.CENTER);


		/* Pack main frame and make visible. */
		
		main_frame.pack();
		main_frame.setVisible(true);		
	}
}
