
//enum to pass directions
public enum Direction {
	UP(-1,0),RIGHT(0,1),DOWN(1,0),LEFT(0,-1);
	private int row, col;
	private Direction(int row, int col){
		this.row=row; this.col=col;
		
	}
	public int[] shift(){
		int[] ret={row,col};
		return ret;
	}
}
