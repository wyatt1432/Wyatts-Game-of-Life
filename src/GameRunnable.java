
public class GameRunnable implements Runnable {
	
	private LifeControl game;
	private long time = 500;

	GameRunnable(LifeControl game, long time) {
		this.game = game;
		this.time = time;
	}
	
	private boolean doStop = false;

    public synchronized void doStop() {
        this.doStop = true;
    }
	
	@Override
	public void run() {
		while(!doStop) {
			game.advanceGame();
			try {
			    Thread.sleep(time);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		}
	}
}
