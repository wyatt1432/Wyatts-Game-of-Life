
public class SpotModel {

	private boolean isAlive = false;
	private int livingNeighbors = 0;
	private int x;
	private int y;
	
	public SpotModel(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getSpotX() {
		return x;
	}

	public int getSpotY() {
		return y;
	}

	public boolean isSpotAlive() {
		return isAlive;
	}
	public void setSpotAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	public int getSpotAliveNeighbors() {
		return livingNeighbors;
	}
	public void setSpotAliveNeighbors(int livingNeighbors) {
		if (livingNeighbors < 0 || livingNeighbors > 8) throw new IllegalArgumentException();
		
		this.livingNeighbors = livingNeighbors;
	}
	
}
