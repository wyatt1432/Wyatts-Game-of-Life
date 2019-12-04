import java.util.Iterator;

public class BoardModel implements Iterable<SpotModel> {

	private SpotModel[][] boardModel;
	private int height;
	private int width;
	
	public BoardModel(int height, int width) {
		boardModel = new SpotModel[width][height];
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				boardModel[i][j] = new SpotModel(i, j);
			}
		}
		
		this.height = height;
		this.width = width;
	}
	
	public SpotModel getSpotAt(int x, int y) {
		return boardModel[x][y];
	}

	@Override
	public Iterator<SpotModel> iterator() {
		return new Iterator<SpotModel>() {
			
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < height * width;
			}

			@Override
			public SpotModel next() {
				int x = i % width;
				int y = (i - x) / width;
				i++;
				return boardModel[x][y];
			}
			
		};
	}
	
}
