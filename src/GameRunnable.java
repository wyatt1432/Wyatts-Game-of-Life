
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

    private synchronized boolean keepRunning() {
        return this.doStop == false;
    }
	
	@Override
	public void run() {
		while(keepRunning()) {
			game.advanceGame();
			try {
			    Thread.sleep(time);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		}
	}
}
