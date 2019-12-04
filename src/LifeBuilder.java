import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LifeBuilder {
	
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
		
		String heightString = JOptionPane.showInputDialog("What would you like the size of the field to be? (Setting this over 25 will be very intensive.)");
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
		String specialString = "K";
		while (done == false) {
			String defaultString = JOptionPane.showInputDialog("Would you like to use the default settings? (Type 'Yes' or 'No')");
			if (defaultString.contentEquals("No")) {
				String survivalMinimumString = JOptionPane.showInputDialog("What would you like the survival minimum to be? (Default 2)");
				survivalMinimum = Integer.parseInt(survivalMinimumString);
				String survivalMaximumString = JOptionPane.showInputDialog("What would you like the survival maximum to be? (Default 3)");
				survivalMaximum = Integer.parseInt(survivalMaximumString);
				String birthMinimumString = JOptionPane.showInputDialog("What would you like the birth minimum to be? (Default 3)");
				birthMinimum = Integer.parseInt(birthMinimumString);
				String birthMaximumString = JOptionPane.showInputDialog("What would you like the birth maximum to be? (Default 3)");
				birthMaximum = Integer.parseInt(birthMaximumString);
				String timeString = JOptionPane.showInputDialog("What would you like the auto mode update time between frames in miliseconds to be? (Default 500)");
				time = Integer.parseInt(timeString);
				specialString = JOptionPane.showInputDialog("Would you like a special message? (Type 'Yes' or 'No')");
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
		LifeControl ttt;
		if (specialString.contentEquals("Yes")) {
			ttt = new LifeControl(time, 35, 35, survivalMinimum, survivalMaximum, birthMinimum, birthMaximum);
			ttt.specialMessage();
		} else {
			ttt = new LifeControl(time, height, width, survivalMinimum, survivalMaximum, birthMinimum, birthMaximum);
		}
		top_panel.add(ttt, BorderLayout.CENTER);


		/* Pack main frame and make visible. */
		
		main_frame.pack();
		main_frame.setVisible(true);		
	}
}
