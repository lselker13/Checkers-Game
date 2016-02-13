import objectdraw.*;

import java.awt.*;

public class Array {
	// constants
	private static final int NROWS = BoardController.NROWS;
	private static final int NCOLUMNS = BoardController.NCOLUMNS;
	private static final int XSTART = BoardController.XSTART;
	private static final int YSTART = BoardController.YSTART;
	private static final int INDICATOR_SIZE = 15;

	// instance variables. The first element of each array is the row, then
	// column
	private State[][] grid = new State[NROWS][NCOLUMNS];
	private Position selected;
	private SelectorGraphic[][] selectors = new SelectorGraphic[NROWS][NCOLUMNS];
	private PieceGraphic[][] black = new PieceGraphic[NROWS][NCOLUMNS];
	private PieceGraphic[][] red = new PieceGraphic[NROWS][NCOLUMNS];
	private KingGraphic[][] kBlack = new KingGraphic[NROWS][NCOLUMNS];
	private KingGraphic[][] kRed = new KingGraphic[NROWS][NCOLUMNS];
	private TurnIndicatorGraphic[] turnIndicator = new TurnIndicatorGraphic[2];
	
	
	
	public Array(int separation, DrawingCanvas canvas) {
		// set the booleans and the grid
		// create and hide the turn indicators

		turnIndicator[0] = new TurnIndicatorGraphic(XSTART + NCOLUMNS * separation
				+ INDICATOR_SIZE, YSTART + NROWS * separation - INDICATOR_SIZE,
				INDICATOR_SIZE, INDICATOR_SIZE, canvas);
		turnIndicator[0].setColor(Color.RED);
		turnIndicator[1] = new TurnIndicatorGraphic(XSTART + NCOLUMNS * separation
				+ INDICATOR_SIZE, YSTART, INDICATOR_SIZE, INDICATOR_SIZE,
				canvas);
		turnIndicator[1].setColor(Color.BLACK);
		turnIndicator[0].hide();
		turnIndicator[1].hide();

		for (int i = 0; i < NROWS; i++) {
			for (int j = 0; j < NCOLUMNS; j++) {
				grid[i][j] = State.EMPTY;
				black[i][j] = new PieceGraphic(XSTART + j * separation, YSTART
						+ i * separation, separation, separation, Color.BLACK,canvas);
				red[i][j] = new PieceGraphic(XSTART + j * separation, YSTART + i
						* separation, separation, separation, Color.RED, canvas);
				kBlack[i][j] = new KingGraphic(XSTART + j * separation, YSTART
						+ i * separation, separation, separation, Color.BLACK,
						canvas);
				kRed[i][j] = new KingGraphic(XSTART + j * separation, YSTART
						+ i * separation, separation, separation, Color.RED,
						canvas);
				selectors[i][j] = new SelectorGraphic(XSTART + j * separation,
						YSTART + i * separation, separation, separation, canvas);

			}

		}
		this.set();
	}

	public State checkLocation(Position pos) {
		if (pos==(null)){
			return null;
			
		}
		if (pos.getRow() < NROWS && pos.getRow() >= 0
				&& pos.getCol() < NCOLUMNS && pos.getCol() >= 0) {
			return grid[pos.getRow()][pos.getCol()];
		} else
			return null;

	}

	public void setLocation(Position pos, State theState) {
		grid[pos.getRow()][pos.getCol()] = theState;
		this.set();
	}

	public void select(Position pos) {
		selected = pos;
		this.set();
	}

	public void deSelect() {
		selected = null;
		this.set();
	}

	public void toggleSelected(Position pos) {
		if (!this.isSelected(pos))
			this.select(pos);
		else
			this.deSelect();
		this.set();

	}

	public boolean isSelected(Position pos) {
		return (selected != null && selected.equals(pos));
	}

	public Position whichSelected() {
		if (selected != null)
			return selected;
		return null;
	}

	public void setIndicators(int i, boolean visible) {
		if (visible)
			turnIndicator[i].show();
		else
			turnIndicator[i].hide();
	}

	public void set() {
		// update the ovals
		int i, j;
		for (i = 0; i < NROWS; i++) {
			for (j = 0; j < NCOLUMNS; j++) {
				black[i][j].hide();
				red[i][j].hide();
				selectors[i][j].hide();
				kBlack[i][j].hide();
				kRed[i][j].hide();

				if (grid[i][j] == State.BLACK) {
					black[i][j].show();
				}
				if (grid[i][j] == State.RED) {
					red[i][j].show();
				}
				if (selected != null && selected.equals(new Position(i, j))) {
					selectors[i][j].show();
				}
				if (grid[i][j] == State.KBLACK) {
					kBlack[i][j].show();
				}
				if (grid[i][j] == State.KRED) {
					kRed[i][j].show();
				}

			}
		}
	}

}