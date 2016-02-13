public class Position {

	private int row, col;

	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Position shift(Direction dir, int mult) {
		return new Position(row + mult*dir.shift()[0], col + mult*dir.shift()[1]);
	
	}
	
	public boolean equals(Position pos){
		if (pos!=null) return (pos.col==this.col&&pos.row==this.row);
		return false;
	}
}
