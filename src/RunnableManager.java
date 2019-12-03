import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunnableManager implements ActionListener {
	
	private LifeControl game;
	ResetRunnable resetRunnable;
	Thread resetThread;
	GameRunnable autoRunnable;
	Thread autoThread;
	long time;
	
	RunnableManager(LifeControl game, long time) {
		this.game = game;
		this.time = time;
		this.resetRunnable = new ResetRunnable(game);
		this.resetThread = new Thread(resetRunnable, "Reset Thread");
		this.autoRunnable = new GameRunnable(game, time);
		this.autoThread = new Thread(autoRunnable, "Auto Thread");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("reset".equals(e.getActionCommand())) {
			autoRunnable.doStop();
			game.resetGame();
			if (game.auto) {
				game.auto = false;
				autoRunnable = new GameRunnable(game, time);
				autoThread = new Thread(autoRunnable, "Auto Thread");
			}
		} else if ("auto".equals(e.getActionCommand())) {
			game.autoToggle();
			if (game.auto) {
				autoThread.start();
			} else {
				autoRunnable.doStop();
				autoRunnable = new GameRunnable(game, time);
				autoThread = new Thread(autoRunnable, "Auto Thread");
			}
		}
	}
}
