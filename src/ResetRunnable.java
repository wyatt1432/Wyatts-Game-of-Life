
public class ResetRunnable implements Runnable {

	private LifeControl game;

	ResetRunnable(LifeControl game) {
		this.game = game;
	}
	
	@Override
	public void run() {
		game.resetGame();
	}
}
